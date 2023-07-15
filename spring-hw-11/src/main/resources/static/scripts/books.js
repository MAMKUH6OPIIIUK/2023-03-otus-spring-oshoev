function renderAllBooks() {
    getAllBooksPromise()
        .then(books => renderBooksTableRows(books))
};

function renderBooksTableRows(books) {
    const table = document.getElementById('library-table');
    const tbody = table.querySelector('tbody');
    books.forEach(book => renderBooksTableRow(book, tbody))
};

function renderBooksTableRow(book, tableRows) {
    const genresCell = document.createElement('td');
    book.genres.forEach(genre => {
        const genreElem = document.createElement('p');
        genreElem.innerHTML = `${genre.name}`;
        genresCell.appendChild(genreElem)});
    const buttonsCell = generateNavigationButtonsCell(book);
    const tableRow = document.createElement('tr');
    tableRow.innerHTML = `
        <td hidden>${book.id}</td>
        <td>${book.title}</td>
        <td>${book.author.fullName}</td>
        ${genresCell.outerHTML}
        ${buttonsCell.outerHTML}
    `;
    tableRows.appendChild(tableRow);
};

function generateNavigationButtonsCell(book) {
    const buttonsCell = document.createElement('td');
    buttonsCell.innerHTML = `
        <button title="${viewButtonTitle}" class="book-table-button">
            <a href="/book/${book.id}">
                <img  src="/images/view-icon.png"/>
            </a>
        </button>
        <button title="${editButtonTitle}" class="book-table-button">
            <a href="/book/edit/${book.id}">
                <img  src="/images/edit-icon.png"/>
            </a>
        </button>
        <button title="${deleteButtonTitle}" class="book-table-button" onclick="deleteBook('${book.id}')">
            <img  src="/images/delete-icon.png"/>
        </button>
    `;
    return buttonsCell;
};