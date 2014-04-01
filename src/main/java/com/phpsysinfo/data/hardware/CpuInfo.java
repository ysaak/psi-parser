package com.phpsysinfo.data.hardware;

import lombok.Data;

@Data
public class CpuInfo {
  private final String model;
  private final double speed;
  private final long cache;
  private final String virt;
  private final double bogomips;
  private final double load;
}
