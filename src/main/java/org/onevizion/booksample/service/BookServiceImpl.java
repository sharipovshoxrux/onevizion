package org.onevizion.booksample.service;

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

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<BookDTO> getAllBooksSortedByTitleDesc() {
        return bookRepository.findAllByOrderByTitleDesc().stream()
                .map(MappingUtil::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void addBook(BookDTO bookDTO) {
        Book book = MappingUtil.convertToEntity(bookDTO);
        bookRepository.save(book);
    }

    @Override
    public Map<String, List<BookDTO>> getBooksGroupedByAuthor() {
        return bookRepository.findAll().stream()
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.mapping(MappingUtil::convertToDTO, Collectors.toList())));
    }

    @Override
    public List<AuthorBookCountDTO> getTopAuthorsByCharacterCount(char character) {
        character = Character.toLowerCase(character);
        char finalCharacter = character;
        return bookRepository.findAll().stream()
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
    }
}
