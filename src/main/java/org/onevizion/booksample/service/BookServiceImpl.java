package org.onevizion.booksample.service;

import lombok.extern.slf4j.Slf4j;
import org.onevizion.booksample.domain.Book;
import org.onevizion.booksample.dto.AuthorBookCountDTO;
import org.onevizion.booksample.dto.BookDTO;
import org.onevizion.booksample.mapper.BookRowMapper;
import org.onevizion.booksample.util.MappingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<BookDTO> getAllBooksSortedByTitleDesc() {
        log.debug("Fetching all books sorted by title in descending order.");
        String sql = "SELECT * FROM book ORDER BY title DESC";
        List<BookDTO> books = jdbcTemplate.query(sql, new BookRowMapper())
                .stream()
                .map(MappingUtil::convertToDTO)
                .collect(Collectors.toList());
        log.info("Fetched {} books.", books.size());
        return books;
    }

    @Override
    public void addBook(BookDTO bookDTO) {
        Book book = MappingUtil.convertToEntity(bookDTO);
        String sql = "INSERT INTO book (title, author, description) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getDescription());
        log.info("Added new book: {}", book);
    }

    @Override
    public Map<String, List<BookDTO>> getBooksGroupedByAuthor() {
        log.debug("Grouping books by author.");
        String sql = "SELECT * FROM book";
        List<Book> books = jdbcTemplate.query(sql, new BookRowMapper());
        Map<String, List<BookDTO>> groupedBooks = books.stream()
                .collect(Collectors.groupingBy(Book::getAuthor,
                        Collectors.mapping(MappingUtil::convertToDTO, Collectors.toList())));
        log.info("Grouped books by author: {}", groupedBooks.keySet());
        return groupedBooks;
    }

    @Override
    public List<AuthorBookCountDTO> getTopAuthorsByCharacterCount(char character) {
        log.debug("Getting top authors by character count in book titles.");
        char finalCharacter = Character.toLowerCase(character);
        String sql = "SELECT * FROM book";
        List<Book> books = jdbcTemplate.query(sql, new BookRowMapper());

        List<AuthorBookCountDTO> topAuthors = books.stream()
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
