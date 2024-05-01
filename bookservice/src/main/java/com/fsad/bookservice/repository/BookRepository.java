package com.fsad.bookservice.repository;

import com.fsad.bookservice.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

  List<Book> findByGenreAndAuthorAndTitleAndUserIDNot(String genre, String author, String title, Long userId);

  List<Book> findBookByUserID(long userID);
}
