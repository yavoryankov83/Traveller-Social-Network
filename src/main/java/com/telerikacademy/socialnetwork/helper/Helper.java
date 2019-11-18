package com.telerikacademy.socialnetwork.helper;

import com.telerikacademy.socialnetwork.exceptions.UnauthorizedException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;

public final class Helper {

  private Helper() {
  }

  public static Pageable createPageRequest(Integer pageNumber, Integer sizePerPage) {
    return PageRequest.of(pageNumber, sizePerPage);
  }

  public static byte[] convertFileContentToBlob(String filePath) throws IOException {
    File file = new File(filePath);

    byte[] fileContent = new byte[(int) file.length()];
    FileInputStream inputStream = null;
    try {

      inputStream = new FileInputStream(file);

      inputStream.read(fileContent);
    } catch (IOException e) {
      throw new IOException("Unable to convert file to byte array." + e.getMessage());
    } finally {
      if (inputStream != null) {
        inputStream.close();
      }
    }
    return fileContent;
  }

  public static void checkPrincipal(Principal principal) {
    if (principal == null) {
      throw new UnauthorizedException(Constants.USER_NOT_LOGGED);
    }
  }
}
