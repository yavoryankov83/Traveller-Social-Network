package com.telerikacademy.socialnetwork.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason =
        "Sorry, something went wrong. It is our fault. Server not responding. We're fixing it!")
public class InternalServerException extends RuntimeException {
  public InternalServerException(String message) {
    super(message);
  }
}
