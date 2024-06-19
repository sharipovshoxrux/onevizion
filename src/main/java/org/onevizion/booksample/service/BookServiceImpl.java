package org.onevizion.booksample.service;

import lombok.extern.slf4j.Slf4j;
import org.onevizion.booksample.domain.Book;
import org.onevizion.booksample.dto.AuthorBookCountDTO;
import org.onevizion.booksample.dto.BookDTO;
import org.onevizion.booksample.repository.BookRepository;
import org.onevizion.booksample.util.MappingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<BookDTO> getAllBooksSortedByTitleDesc() {
        log.debug("Fetching all books sorted by title in descending order.");
        var books = bookRepository.findAllByOrderByTitleDesc().stream()
                .map(MappingUtil::convertToDTO)
                .collect(Collectors.toList());
        log.info("Fetched {} books.", books.size());
        return books;
    }

    @Override
    public void addBook(BookDTO bookDTO) {
        Book book = MappingUtil.convertToEntity(bookDTO);
        bookRepository.save(book);
        log.info("Added new book: {}", book);
    }

    @Override
    public Map<String, List<BookDTO>> getBooksGroupedByAuthor() {
        log.debug("Grouping books by author.");
        Map<String, List<BookDTO>> groupedBooks = bookRepository.findAll().stream()
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.mapping(MappingUtil::convertToDTO, Collectors.toList())));
        log.info("Grouped books by author: {}", groupedBooks.keySet());
        return groupedBooks;
    }

    @Override
    public List<AuthorBookCountDTO> getTopAuthorsByCharacterCount(char character) {
        log.debug("Getting top authors by character count in book titles.");
        char finalCharacter = Character.toLowerCase(character);
        List<AuthorBookCountDTO> topAuthors = bookRepository.findAll().stream()
                .collect(Collectors.groupingBy(Book::getAuthor))
                .entrySet().stream()
                .map(entry -> {
                    long count = entry.getValue().stream()
                            .flatMap(book -> book.getTitle().chars().mapToObj(c -> (char) c))
                            .filter(c -> Character.toLowerCase(c) == finalCharacter)
                            .count();
                    return new AuthorBookCountDTO(entry.getKey(), count);
                })
                .filter(authorBookCount -> authorBookCount.getCount() > 0)
                .sorted(Comparator.comparingLong(AuthorBookCountDTO::getCount).reversed())
                .limit(10)
                .collect(Collectors.toList());
        log.info("Top authors by character count '{}': {}", character, topAuthors);
        return topAuthors;
    }
}
