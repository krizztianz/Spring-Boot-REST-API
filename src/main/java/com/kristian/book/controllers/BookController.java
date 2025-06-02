package com.kristian.book.controllers;

import com.kristian.book.entities.Book;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BookController {

    private final List<Book> books = new ArrayList<>();

    public BookController() {
        initializeBooks();
    }

    private void initializeBooks()
    {
        books.addAll(
                List.of(
                        new Book("Title One", "Author One", "Science"),
                        new Book("Title Two", "Author Two", "Science"),
                        new Book("Title Three", "Author Three", "History"),
                        new Book("Title Four", "Author Four", "Math"),
                        new Book("Title Five", "Author Five", "Math"),
                        new Book("Title Six", "Author Six", "Math")
                )
        );
    }

    @GetMapping("/api/books")
    public List<Book> getBooks(@RequestParam(required = false) String category)
    {
        if(category == null)
        {
            return books;
        }

        return books.stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    @GetMapping("/api/books/{title}")
    public Book getBookByTitle(@PathVariable String title)
    {
        return books.stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    @PostMapping("/api/books")
    public void createBook(@RequestBody Book newBook)
    {
        boolean isNewBook = books.stream().noneMatch(book -> book.getTitle().equalsIgnoreCase(newBook.getTitle()));

        if(isNewBook)
        {
            books.add(newBook);
        }
    }

    @PutMapping("/api/books/{title}")
    public void updateBook(@PathVariable String title, @RequestBody Book updatedBook)
    {
        for (int i = 0; i < books.size(); i++)
        {
            if (books.get(i).getTitle().equalsIgnoreCase(updatedBook.getTitle()))
            {
                books.set(i, updatedBook);
                return;
            }
        }
    }

    @DeleteMapping("/api/books/{title}")
    public void deleteBook(@PathVariable String title)
    {
        books.removeIf(book -> book.getTitle().equalsIgnoreCase(title));
    }
}
