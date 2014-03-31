package com.phpsysinfo.data.plugins.smart;

import lombok.Data;

@Data
public class DiskAttribute {
  private final int id;
  private final String name;
  private final long value;
}
