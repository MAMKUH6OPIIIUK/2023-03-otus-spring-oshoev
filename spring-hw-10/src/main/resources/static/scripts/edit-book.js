function renderBookEditionFormData() {
    const bookId = document.getElementById('id-input').value;
    if (bookId) {
        getBookPromise(bookId).then(book => {
        document.getElementById('new-book-title').value = book.title;
        document.getElementById('new-book-description').value = book.description;
        renderAuthorSelectData(book);
        renderGenresSelectData(book);
        });
    } else {
        renderAuthorSelectData();
        renderGenresSelectData();
    }
};

function renderAuthorSelectData(book) {
    const authorSelect = document.getElementById('new-book-author');
    getAllAuthorsPromise().then(authors => {
        authors.forEach(author => {
            const option = document.createElement('option');
            option.value = author.id;
            option.textContent = author.fullName;
            if (book && book.author.id == author.id) {
                option.setAttribute('selected', 'selected');
            }
            authorSelect.appendChild(option);
        });
    });
};

function renderGenresSelectData(book) {
    const genresSelect = document.getElementById('new-book-genres');
    getAllGenresPromise().then(genres => {
        genres.forEach(genre => {
            const option = document.createElement('option');
            option.value = genre.id;
            option.textContent = genre.name;
            if (book) {
                book.genres.forEach(bookGenre => {
                   if (bookGenre.id == genre.id) {
                       option.setAttribute('selected', 'selected');
                       return;
                   }
                });
            }
            genresSelect.appendChild(option);
        })
    });
};

function saveBook() {
    var book = new Object();
    book.id = document.getElementById('id-input').value;
    book.title = document.getElementById('new-book-title').value;
    book.description = document.getElementById('new-book-description').value;
    var selectAuthorElem = document.getElementById('new-book-author');
    book.authorId = selectAuthorElem.options[selectAuthorElem.selectedIndex].value;
    var selectGenresElem = document.getElementById('new-book-genres');
    book.genreIds = Array.from(selectGenresElem.selectedOptions).map(option => option.value);
    if (book.id) {
        updateBook(book);
    } else {
        createBook(book);
    }
};