package org.onevizion.booksample.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AuthorBookCountDTO {
    private String author;
    private long count;

    public AuthorBookCountDTO(String author, long count) {
        this.author = author;
        this.count = count;
    }
}
