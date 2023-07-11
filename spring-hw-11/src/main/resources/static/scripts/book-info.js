function renderBookInfoFormData() {
    document.getElementById('new-comment-text').value = '';
    const bookId = document.getElementById('id-input').textContent;
    getBookPromise(bookId).then(book => {
        document.getElementById('book-title').textContent = book.title;
        document.getElementById('book-author').textContent = book.author.fullName;
        document.getElementById('book-description').textContent = book.description;
        renderBookCommentTable(book);
    });
};

function renderBookCommentTable(book) {
    const table = document.getElementById('comments-table');
    const tbody = table.querySelector('tbody');
    getBookCommentsPromise(book).then(comments => {
        comments.forEach(comment => {
            const tableRow = document.createElement('tr');
            tableRow.setAttribute('id', `comment-${comment.id}`);
            const buttonsCell = generateCommentButtonsCell(comment);
            tableRow.innerHTML = `
                <td><pre class="comment-pre">${comment.text}</pre></td>
                ${buttonsCell.outerHTML}
            `;
            tbody.appendChild(tableRow);
        })
    });
};

function generateCommentButtonsCell(comment) {
    const buttonsCell = document.createElement('td');
    buttonsCell.innerHTML = `
        <button title="${editButtonTitle}" onclick="renderCommentEditRow('${comment.id}')" class="book-table-button">
            <img  src="/images/edit-icon.png"/>
        </button>
        <button title="${deleteButtonTitle}" onclick="deleteBookComment('${comment.id}')" class="book-table-button">
            <img  src="/images/delete-icon.png"/>
        </button>
    `;
    return buttonsCell;
};

function renderCommentEditRow(id) {
    const rows = document.getElementById('comments-table').querySelector('tbody').rows;
    Array.from(rows).forEach(row => {
        row.cells[1].setAttribute('hidden', 'hidden');
    });
    document.getElementById("new-comment-form").setAttribute('hidden', 'hidden');
    const rowId = 'comment-'+ id;
    const commentRow = document.getElementById(rowId);
    const oldCommentText = commentRow.cells[0].textContent;
    commentRow.innerHTML = `
        <td>
        <form action="book-info.html">
            <div>
                <textarea name="text" id="editing-comment-text" class="comment-text-area">${oldCommentText}</textarea>
                <div hidden id="text-errors" class="errors"></div>
            </div>
            <button type="button" title="${acceptButtonTitle}" onclick="saveComment('${id}')" class="book-table-button">
                <img  src="/images/accept-icon.png"/>
            </button>
            <button type="button" title="${cancelButtonTitle}" onclick="cancelCommentEdit()" class="book-table-button">
               <img  src="/images/cancel-icon.png"/>
            </button>
        </form>
        </td>
    `;
};

function saveComment(id) {
    var comment = new Object();
    if (id) {
        comment.id = id;
        comment.text = document.getElementById('editing-comment-text').value;
        updateBookComment(comment);
    } else {
        comment.text = document.getElementById('new-comment-text').value;
        comment.bookId = document.getElementById('id-input').textContent;
        createBookComment(comment);
    }
};

function cancelCommentEdit() {
    location.reload();
}


