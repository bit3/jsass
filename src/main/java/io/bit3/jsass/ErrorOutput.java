package io.bit3.jsass;

/**
 * The Output of a failed compilation
 */
public interface ErrorOutput {

  int getErrorStatus();

  String getErrorJson();

  String getErrorText();

  String getErrorMessage();

  String getErrorFile();

  String getErrorSrc();
}
