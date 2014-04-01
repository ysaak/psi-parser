package com.phpsysinfo.data.memory;

import lombok.Data;

@Data
public class MemoryDetails {
  private final long applicationUsed;
  private final int applicationPercent;

  private final long bufferedUsed;
  private final int bufferedPercent;

  private final long cacheUsed;
  private final int cachePercent;
}
