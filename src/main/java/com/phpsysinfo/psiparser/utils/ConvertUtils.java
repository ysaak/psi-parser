package com.phpsysinfo.psiparser.utils;

public abstract class ConvertUtils {

  public static final int safeGetInt(String intString) {
    return safeGetInt(intString, 0);
  }

  public static final int safeGetInt(String intString, int defaultValue) {
    try {
      return Integer.parseInt(intString);
    }
    catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  public static final long safeGetLong(String longString) {
    return safeGetLong(longString, 0L);
  }

  public static final long safeGetLong(String longString, long defaultValue) {
    try {
      return Long.parseLong(longString);
    }
    catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  public static final double safeGetDouble(String doubleString) {
    return safeGetDouble(doubleString, 0.0);
  }

  public static final double safeGetDouble(String doubleString, double defaultValue) {
    try {
      return Double.parseDouble(doubleString);
    }
    catch (NumberFormatException e) {
      return defaultValue;
    }
  }
}

