package dev.lukaszmichalak.fillers;

public class FillerException extends RuntimeException {

  FillerException(Exception e) {
    super(e);
  }

  FillerException(String message) {
    super(message);
  }
}
