package com.jaheer.bookstore.service.book;

import com.jaheer.bookstore.entity.Book;
import com.jaheer.bookstore.servicedto.request.AddBookRequest;
import com.jaheer.bookstore.servicedto.request.UpdateBookRequest;
import com.jaheer.bookstore.servicedto.response.BookResponse;

import java.util.List;

public interface BookService {
    String addBook(AddBookRequest addBookRequest);

    Book updateBook(UpdateBookRequest updateBookRequest);

    List<BookResponse> getAllBooks(int limit, int page);

    BookResponse getBookDetail(String id);

    void deleteBook(String id);
}
