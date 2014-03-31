package com.phpsysinfo.data.plugins.ipmi;

import lombok.Data;

@Data
public class IpmiInfo {
  private final String label;
  private final double value;
  private final boolean state;
}
