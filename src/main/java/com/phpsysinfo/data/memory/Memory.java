package com.phpsysinfo.data.memory;

import lombok.Data;

@Data
public class Memory {
  private final long overallUsed;
  private final long overallTotal;
  private final int overallPercent;

  private MemoryDetails details;
  private SwapMemory swap;
}
