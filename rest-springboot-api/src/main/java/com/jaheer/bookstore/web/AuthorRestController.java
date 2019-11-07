package com.jaheer.bookstore.web;

import com.jaheer.bookstore.service.author.AuthorService;
import com.jaheer.bookstore.servicedto.request.AddAuthorRequest;
import com.jaheer.bookstore.webdto.request.AddAuthorWebRequest;
import com.jaheer.bookstore.webdto.response.BaseWebResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/authors")
public class AuthorRestController {

    @Autowired
    private AuthorService authorService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseWebResponse> addAuthor(@RequestBody AddAuthorWebRequest addAuthorWebRequest) {
    	String authorId = authorService.addAuthor(toAddAuthorRequest(addAuthorWebRequest));
        return ResponseEntity.created(URI.create("/api/authors/" + authorId)).body(BaseWebResponse.successNoData());
        
    }

    private AddAuthorRequest toAddAuthorRequest(AddAuthorWebRequest addAuthorWebRequest) {
        AddAuthorRequest addAuthorRequest = new AddAuthorRequest();
        BeanUtils.copyProperties(addAuthorWebRequest, addAuthorRequest);
        return addAuthorRequest;
    }
}
