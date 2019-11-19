package com.telerikacademy.socialnetwork.exceptions;//package com.telerikacademy.socialnetwork.exceptions;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//
//import java.util.Date;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//  @ExceptionHandler(NotFoundException.class)
//  public ResponseEntity<?> notFoundException(NotFoundException ex, WebRequest request) {
//    ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
//    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
//  }
//
//  @ExceptionHandler(ConflictException.class)
//  public ResponseEntity<?> conflictException(ConflictException ex, WebRequest request) {
//    ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
//    return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
//  }
//
//  @ExceptionHandler(BadRequestException.class)
//  public ResponseEntity<?> badRequestException(BadRequestException ex, WebRequest request) {
//    ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
//    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
//  }
//
//  @ExceptionHandler(UnauthorizedException.class)
//  public ResponseEntity<?> unauthorizedException(UnauthorizedException ex, WebRequest request) {
//    ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
//    return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
//  }
//
//  @ExceptionHandler(Exception.class)
//  public ResponseEntity<?> globuleException(Exception ex, WebRequest request) {
//    ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
//    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
//  }
//}