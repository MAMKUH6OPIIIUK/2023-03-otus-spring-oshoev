function getAllBooksPromise() {
    const books = fetch('/api/book')
        .then(response => response.json());
    return books;
};

function getBookPromise(id) {
    const book = fetch(`/api/book/${id}`)
         .then(response => {
            if (response.status == 404) {
                window.location.replace('/book');
            } else {
                return response.json();
            }
         });
    return book;
};

function createBook(book) {
    fetch('/api/book', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(book)})
    .then(response => {
        handleSaveBookResponse(response);
    })
};

function updateBook(book) {
    fetch(`/api/book/${book.id}`, {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(book)})
        .then(response => {
            handleSaveBookResponse(response);
        })
};

function handleSaveBookResponse(response) {
    if (response.status == 200 || response.status == 201) {
        window.location.replace('/book');
    } else if (response.status == 400) {
        response.json().then(error => {handleValidationError(error)});
    } else {
        console.error('Error saving book');
    }
};

function deleteBook(id) {
    fetch(`/api/book/${id}`, {method: 'DELETE'})
        .then(response => location.reload());
};

function getAllAuthorsPromise() {
    const authors = fetch('/api/author')
        .then(response => response.json());
    return authors;
};

function getAllGenresPromise() {
    const genres = fetch('/api/genre')
        .then(response => response.json());
    return genres;
};

function getBookCommentsPromise(book) {
    var queryParams = new URLSearchParams({bookId: book.id});
    const comments = fetch('/api/comment?' + queryParams)
            .then(response => response.json());
        return comments;
};

function createBookComment(comment) {
    fetch('/api/comment', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(comment)})
    .then(response => {
        handleSaveCommentResponse(response);
    });
};

function updateBookComment(comment) {
    fetch(`/api/comment/${comment.id}`, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(comment)})
    .then(response => {
        handleSaveCommentResponse(response);
    });
};

function handleSaveCommentResponse(response) {
    if (response.status == 200 || response.status == 201) {
        location.reload();
    } else if (response.status == 400) {
        // тут прирастут результаты валидации
        response.json().then(error => {handleValidationError(error)});
    } else {
        console.error('Error saving comment');
    }
};

function deleteBookComment(id) {
    fetch(`/api/comment/${id}`, {method: 'DELETE'})
        .then(response => location.reload());
};

/* метод для отрисовки ошибок валидации на странице. Принимает тело ответа, которое должно представлять собой ErrorDto*/
function handleValidationError(error) {
    const fields = error.fields;
    if (fields) {
        const fieldApiToHtmlIdMap = new Map([
            ['title', 'title-errors'],
            ['description', 'description-errors'],
            ['authorId', 'author-errors'],
            ['genres', 'genres-errors'],
            ['text', 'text-errors']
        ]);
        fieldApiToHtmlIdMap.forEach(function(errorContainerId, fieldName) {
            const fieldMessages = fields[fieldName];
            const errorContainer = document.getElementById(errorContainerId);
            if (fieldMessages) {
                if (errorContainer) {
                    errorContainer.removeAttribute('hidden');
                    errorContainer.innerHTML = fieldMessages.join('<br/>');
                }
            } else {
                if (errorContainer) {
                    errorContainer.setAttribute('hidden', 'hidden');
                    errorContainer.innerHTML = '';
                }
            }
        });
    } else {
        console.error('Bad user request');
    }
};