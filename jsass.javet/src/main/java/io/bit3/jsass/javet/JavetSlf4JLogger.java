package io.bit3.jsass.javet;

import com.caoccao.javet.interfaces.IJavetLogger;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class JavetSlf4JLogger implements IJavetLogger {

  @NonNull
  private final Logger logger;

  public JavetSlf4JLogger() {
    this(LoggerFactory.getLogger(JavetSlf4JLogger.class));
  }

  public JavetSlf4JLogger(String name) {
    this(LoggerFactory.getLogger(name));
  }

  @Override
  public void debug(String message) {
    logger.debug(message);
  }

  @Override
  public void error(String message) {
    logger.error(message);
  }

  @Override
  public void error(String message, Throwable cause) {
    logger.error(message, cause);
  }

  @Override
  public void info(String message) {
    logger.info(message);
  }

  @Override
  public void warn(String message) {
    logger.warn(message);
  }

}
