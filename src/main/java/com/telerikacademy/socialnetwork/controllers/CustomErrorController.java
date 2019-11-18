package com.telerikacademy.socialnetwork.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

  @RequestMapping(path = "error")
  public String handleError(HttpServletRequest request, Model model) {
    Object errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

    model.addAttribute("errorMessage", errorMessage);

    if (status != null) {
      Integer statusCode = Integer.valueOf(status.toString());

      if (statusCode == HttpStatus.NOT_FOUND.value()) {
        return "error-404";
      }
      if (statusCode == HttpStatus.BAD_REQUEST.value()) {
        return "error-400";
      }
      if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
        return "error-401";
      }
      if (statusCode == HttpStatus.CONFLICT.value()) {
        return "error-409";
      }
      if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
        return "error-500";
      }
    }
    return "error";
  }

  @Override
  public String getErrorPath() {
    return "error";
  }
}
