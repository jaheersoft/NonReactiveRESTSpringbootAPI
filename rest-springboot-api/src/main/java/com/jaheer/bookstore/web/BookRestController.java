package com.jaheer.bookstore.web;

import com.jaheer.bookstore.entity.Book;
import com.jaheer.bookstore.service.book.BookService;
import com.jaheer.bookstore.servicedto.request.AddBookRequest;
import com.jaheer.bookstore.servicedto.request.UpdateBookRequest;
import com.jaheer.bookstore.servicedto.response.BookResponse;
import com.jaheer.bookstore.webdto.request.AddBookWebRequest;
import com.jaheer.bookstore.webdto.request.UpdateBookWebRequest;
import com.jaheer.bookstore.webdto.response.BaseWebResponse;
import com.jaheer.bookstore.webdto.response.BookWebResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/books")
public class BookRestController {

    @Autowired
    private BookService bookService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseWebResponse> addBook(@RequestBody AddBookWebRequest addBookWebRequest) {
        String bookId = bookService.addBook(toAddBookRequest(addBookWebRequest));
        return ResponseEntity.created(URI.create("/api/books/" + bookId)).body(BaseWebResponse.successNoData());
    }

    private AddBookRequest toAddBookRequest(AddBookWebRequest addBookWebRequest) {
        AddBookRequest addBookRequest = new AddBookRequest();
        BeanUtils.copyProperties(addBookWebRequest, addBookRequest);
        return addBookRequest;
    }

    @PutMapping(
            value = "/{bookId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseWebResponse> updateBook(@PathVariable(value = "bookId") String bookId,
                                                              @RequestBody UpdateBookWebRequest updateBookWebRequest) {
        Book book = bookService.updateBook(toUpdateBookRequest(bookId, updateBookWebRequest));
        return ResponseEntity.ok(BaseWebResponse.successNoData());
    }

    private UpdateBookRequest toUpdateBookRequest(String bookId, UpdateBookWebRequest updateBookWebRequest) {
        UpdateBookRequest updateBookRequest = new UpdateBookRequest();
        BeanUtils.copyProperties(updateBookWebRequest, updateBookRequest);
        updateBookRequest.setId(bookId);
        return updateBookRequest;
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseWebResponse<List<BookWebResponse>>> getAllBooks(@RequestParam(value = "limit", defaultValue = "5") int limit,
                                                                                      @RequestParam(value = "page", defaultValue = "0") int page) {
    	List<BookResponse> bookResponses =  bookService.getAllBooks(limit, page);
        return ResponseEntity.ok(BaseWebResponse.successWithData(toBookWebResponseList(bookResponses)));
    }

    private List<BookWebResponse> toBookWebResponseList(List<BookResponse> bookResponseList) {
        return bookResponseList
                .stream()
                .map(this::toBookWebResponse)
                .collect(Collectors.toList());
    }

    private BookWebResponse toBookWebResponse(BookResponse bookResponse) {
        BookWebResponse bookWebResponse = new BookWebResponse();
        BeanUtils.copyProperties(bookResponse, bookWebResponse);
        return bookWebResponse;
    }

    @GetMapping(
            value = "/{bookId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseWebResponse<BookWebResponse>> getBookDetail(@PathVariable(value = "bookId") String bookId) {
        BookResponse bookResponse = bookService.getBookDetail(bookId);
        return ResponseEntity.ok(BaseWebResponse.successWithData(toBookWebResponse(bookResponse)));
    }

    @DeleteMapping(
            value = "/{bookId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseWebResponse> deleteBook(@PathVariable(value = "bookId") String bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.ok(BaseWebResponse.successNoData());
    }

}
