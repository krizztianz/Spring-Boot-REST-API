package com.kristian.book.controllers;

import com.kristian.book.entities.Book;
import com.kristian.book.requests.BookRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Books REST API Endpoints", description = "Operations related to Books")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final List<Book> books = new ArrayList<>();

    public BookController() {
        InitializeBooks();
    }

    private void InitializeBooks() {
        books.addAll(
                List.of(
                        new Book(1,"Computer Science Pro", "Chad Derby", "Computer Science", 5),
                        new Book(2,"Java Spring Master", " Eric Roby", "Computer Science", 5),
                        new Book(3,"Why 1+1 Rocks", "Adil A.", "Math", 5),
                        new Book(4,"How Bears Hibernate", "Bob B.", "Science", 2),
                        new Book(5,"A Pirate's Treasure", "Curt C.", "History", 3),
                        new Book(6,"Why 2+2 is Better", "Dan D.", "Math", 1)
                )
        );
    }

    @Operation(summary = "Get all books", description = "Retreive a list of available books")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public  List<Book> getBooks(@Parameter(description = "Optional query parameter")
                                    @RequestParam(required = false) String category)
    {
        if(category == null){
            return books;
        }

        return books.stream()
                .filter(book -> book.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    @Operation(summary = "Get a book by id", description = "Retreive a specific book by id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Book getBookByTitle(@Parameter(description = "Id of the book to be retreived")
                                   @PathVariable long id)
    {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Operation(summary = "Create a new book", description = "Add a new book to the list")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createBook(@Valid @RequestBody BookRequest newBook)
    {
        long id = books.isEmpty() ? 1 : books.get(books.size() - 1).getId() + 1;
        Book book = convertToBook(id, newBook);
        books.add(book);
    }

    @Operation(summary = "Update a book", description = "Update the details of an existing book")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateBook(@Parameter(description = "Id of the book to update")
                               @PathVariable long id,
                           @Valid @RequestBody BookRequest updatedBook)
    {
        for (int i = 0; i < books.size(); i++)
        {
            if (books.get(i).getId() == id) {
                Book book = convertToBook(id, updatedBook);
                books.set(i, book);
                return;
            }
        }
    }

    @Operation(summary = "Delete a book", description = "Remove a book from the list")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBook(@Parameter(description = "Id of the book to delete")
                               @PathVariable long id)
    {
        books.removeIf(book -> book.getId() == id);
    }

    private Book convertToBook(long id, BookRequest newBook) {
        return new Book(id, newBook.getTitle(), newBook.getAuthor(), newBook.getCategory(), newBook.getRating());
    }

}
