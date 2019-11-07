package com.jaheer.bookstore.service.author;

import com.jaheer.bookstore.entity.Author;
import com.jaheer.bookstore.repository.AuthorRepository;
import com.jaheer.bookstore.servicedto.request.AddAuthorRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public String addAuthor(AddAuthorRequest addAuthorRequest) {
        return addAuthorToRepository(addAuthorRequest);
    }

    private String addAuthorToRepository(AddAuthorRequest addAuthorRequest) {
        return authorRepository.save(toAuthor(addAuthorRequest)).getId();
    }

    private Author toAuthor(AddAuthorRequest addAuthorRequest) {
        Author author = new Author();
        BeanUtils.copyProperties(addAuthorRequest, author);
        author.setId(UUID.randomUUID().toString());
        return author;
    }
}
