package com.fsad.userservice.entities;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.StringJoiner;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column(nullable = false)
  private String firstName;
  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true)
  private String userName;

  @Column(nullable = false)
  private String password;

  @CreatedDate
  private LocalDateTime createdDate;

  @LastModifiedDate
  private LocalDateTime modifiedDate;

  @Version
  private Long version;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public LocalDateTime getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(LocalDateTime modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("firstName='" + firstName + "'")
        .add("lastName='" + lastName + "'")
        .add("email='" + email + "'")
        .add("userName='" + userName + "'")
        .add("password='" + password + "'")
        .add("createdDate=" + createdDate)
        .add("modifiedDate=" + modifiedDate)
        .add("version=" + version)
        .toString();
  }
}
