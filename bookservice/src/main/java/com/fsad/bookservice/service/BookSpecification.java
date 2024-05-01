package com.fsad.bookservice.service;

import com.fsad.bookservice.entities.Book;
import com.fsad.bookservice.enums.SearchFilter;
import com.fsad.bookservice.model.SearchFilterParams;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification implements Specification<Book> {

  private SearchFilterParams searchFilterParams;

  public BookSpecification(SearchFilterParams searchFilterParams) {
    this.searchFilterParams = searchFilterParams;
  }

  @Override
  public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    return byAttribute(SearchFilter.GENRE.getName(), searchFilterParams.getGenre())
        .and(byAttribute(SearchFilter.AUTHOR.getName(), searchFilterParams.getAuthor()))
        .and(byAttribute(SearchFilter.TITLE.getName(), searchFilterParams.getTitle()))
        .and(byUserID(searchFilterParams.getUserID()))
        .toPredicate(root, query, criteriaBuilder);
  }

  public static Specification<Book> byAttribute(String attributeName, String value) {
    return (root, query, criteriaBuilder) -> {
      Predicate predicate = null;
      if (StringUtils.isNotBlank(value)) {
        predicate = criteriaBuilder.equal(root.get(attributeName), value);
      }
      return predicate;
    };
  }

  public static Specification<Book> byUserID(Long userID) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("userID"), userID);
  }
}
