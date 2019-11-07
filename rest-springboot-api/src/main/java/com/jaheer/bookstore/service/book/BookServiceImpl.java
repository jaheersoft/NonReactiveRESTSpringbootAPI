package com.jaheer.bookstore.service.book;

import com.jaheer.bookstore.entity.Author;
import com.jaheer.bookstore.entity.Book;
import com.jaheer.bookstore.repository.AuthorRepository;
import com.jaheer.bookstore.repository.BookRepository;
import com.jaheer.bookstore.servicedto.request.AddBookRequest;
import com.jaheer.bookstore.servicedto.request.UpdateBookRequest;
import com.jaheer.bookstore.servicedto.response.BookResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public String addBook(AddBookRequest addBookRequest) {
        return saveBookToRepository(addBookRequest);
    }

    private String saveBookToRepository(AddBookRequest addBookRequest) {
        Optional<Author> optionalAuthor = authorRepository.findById(addBookRequest.getAuthorId());
        if (!optionalAuthor.isPresent())
            throw new EntityNotFoundException();
        else {
            return bookRepository.save(toBook(addBookRequest)).getId();
        }
    }

    private Book toBook(AddBookRequest addBookRequest) {
        Book book = new Book();
        BeanUtils.copyProperties(addBookRequest, book);
        book.setId(UUID.randomUUID().toString());
        book.setAuthor(Author.builder()
                .id(addBookRequest.getAuthorId())
                .build());
        return book;
    }

    @Override
    public Book updateBook(UpdateBookRequest updateBookRequest) {
        return updateBookToRepository(updateBookRequest);
    }

    private Book updateBookToRepository(UpdateBookRequest updateBookRequest) {
        Optional<Book> optionalBook = bookRepository.findById(updateBookRequest.getId());
        if (!optionalBook.isPresent())
            throw new EntityNotFoundException();
        else {
            Book book = optionalBook.get();
            book.setTitle(updateBookRequest.getTitle());
            return bookRepository.save(book);
        }
    }

    @Override
    public List<BookResponse> getAllBooks(int limit, int page) {
    	return bookRepository.findAll(PageRequest.of(page, limit))
    					.getContent()
    					.stream()
				        .map(this::toBookResponse)
				        .collect(Collectors.toList());
    }

	/*
	 * private List<Book> findAllBooksInRepository(int limit, int page) { return
	 * bookRepository.findAll(PageRequest.of(page, limit)).getContent(); }
	 * 
	 * private List<BookResponse> toBookResponseList(List<Book> bookList) { return
	 * bookList .stream() .map(this::toBookResponse) .collect(Collectors.toList());
	 * }
	 */

    private BookResponse toBookResponse(Book book) {
        BookResponse bookResponse = new BookResponse();
        BeanUtils.copyProperties(book, bookResponse);
        bookResponse.setAuthorName(book.getAuthor().getName());
        return bookResponse;
    }

    @Override
    public BookResponse getBookDetail(String id) {
        return findBookDetailInRepository(id);
    }

    private BookResponse findBookDetailInRepository(String id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (!optionalBook.isPresent())
        	throw new EntityNotFoundException();
        else {
            return toBookResponse(optionalBook.get());
        }
    }

    @Override
    public void deleteBook(String id) {
        deleteBookInRepository(id);
    }

    private void deleteBookInRepository(String id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (!optionalBook.isPresent())
            throw new EntityNotFoundException();
        else {
            bookRepository.delete(optionalBook.get());
        }
    }
}
