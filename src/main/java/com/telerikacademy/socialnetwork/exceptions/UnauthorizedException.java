package com.telerikacademy.socialnetwork.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason =
        "Sorry, the User/Friend/Post/Comment or the other resources you are looking for are not accessible. You do not have the appropriate rights!")
public class UnauthorizedException extends RuntimeException {
  public UnauthorizedException(String message) {
    super(message);
  }
}