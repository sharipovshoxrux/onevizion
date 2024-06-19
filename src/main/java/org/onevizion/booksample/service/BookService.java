package org.onevizion.booksample.service;

import org.onevizion.booksample.dto.AuthorBookCountDTO;
import org.onevizion.booksample.dto.BookDTO;

import java.util.List;
import java.util.Map;

public interface BookService {
    List<BookDTO> getAllBooksSortedByTitleDesc();
    void addBook(BookDTO bookDTO);
    Map<String, List<BookDTO>> getBooksGroupedByAuthor();
    List<AuthorBookCountDTO> getTopAuthorsByCharacterCount(char character);
}