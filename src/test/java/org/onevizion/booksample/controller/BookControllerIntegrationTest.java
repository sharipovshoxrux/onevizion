package org.onevizion.booksample.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.onevizion.booksample.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllBooks() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[0].title", is("War and Peace")));
    }

    @Test
    public void testAddBook() throws Exception {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("New Book");
        bookDTO.setAuthor("New Author");

        mockMvc.perform(post("/api/books")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(bookDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetBooksGroupedByAuthor() throws Exception {
        mockMvc.perform(get("/api/books/grouped-by-author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['F. Dostoevsky']", hasSize(2)));
    }

    @Test
    public void testGetTopAuthorsByCharacterCount() throws Exception {
        mockMvc.perform(get("/api/books/top-authors-by-char").param("character", "a"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }
}
