package com.fsad.bookservice.repository;

import com.fsad.bookservice.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
  List<Book> findByGenreAndAuthorAndTitle(String genre, String author, String title);
}
