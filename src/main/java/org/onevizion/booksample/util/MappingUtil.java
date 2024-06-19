package org.onevizion.booksample.util;

import org.onevizion.booksample.domain.Book;
import org.onevizion.booksample.dto.BookDTO;

public class MappingUtil {

    public static BookDTO convertToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setDescription(book.getDescription());
        return dto;
    }

    public static Book convertToEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setDescription(bookDTO.getDescription());
        return book;
    }
}
