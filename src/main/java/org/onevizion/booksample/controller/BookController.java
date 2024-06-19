package org.onevizion.booksample.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.onevizion.booksample.dto.AuthorBookCountDTO;
import org.onevizion.booksample.dto.BookDTO;
import org.onevizion.booksample.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book Management System", description = "Operations pertaining to book management")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "View a list of available books", description = "Get a list of all books, sorted by title in descending order")
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooksSortedByTitleDesc());
    }

    @Operation(summary = "Add a new book", description = "Add a new book to the database")
    @PostMapping
    public ResponseEntity<Void> addBook(@RequestBody @Valid BookDTO bookDTO) {
        bookService.addBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "View books grouped by author", description = "Get a list of all books, grouped by author")
    @GetMapping("/grouped-by-author")
    public ResponseEntity<Map<String, List<BookDTO>>> getBooksGroupedByAuthor() {
        return ResponseEntity.ok(bookService.getBooksGroupedByAuthor());
    }

    @Operation(summary = "Get top authors by character count in book titles", description = "Get the top authors by the number of times a specified character appears in their book titles")
    @GetMapping("/top-authors-by-char")
    public ResponseEntity<List<AuthorBookCountDTO>> getTopAuthorsByCharacterCount(@RequestParam char character) {
        return ResponseEntity.ok(bookService.getTopAuthorsByCharacterCount(character));
    }
}
