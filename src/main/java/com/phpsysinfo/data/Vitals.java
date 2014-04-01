package com.phpsysinfo.data;

import lombok.Data;

@Data
public class Vitals {
  private final String hostname;
  private final String ipAddress;
  private final String kernel;
  private final String distro;
  private final String distroIcon;
  private final double uptime;
  private final int users;
  private final double[] loadAvg;
  private final double cpuLoad;
}
