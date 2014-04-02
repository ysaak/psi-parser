package com.phpsysinfo.parser;

public class PSIError extends Exception {

  private final String function;

  public PSIError(String function, String message) {
    super(message);
    this.function = function;
  }

  public String getFunction() {
    return function;
  }
}
