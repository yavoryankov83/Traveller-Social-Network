package com.telerikacademy.socialnetwork.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Sorry, the User/Friend/Post/Comment or the other resources already exist!")
public class ConflictException extends RuntimeException {
  public ConflictException(String message) {
    super(message);
  }
}
