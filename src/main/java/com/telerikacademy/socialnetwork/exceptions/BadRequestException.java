package com.telerikacademy.socialnetwork.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Sorry, the request you are trying to execute in not valid!")
public class BadRequestException extends RuntimeException {
  public BadRequestException(String message) {
    super(message);
  }
}
