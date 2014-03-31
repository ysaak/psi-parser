package com.phpsysinfo.data.plugins.quotas;

import lombok.Data;

@Data
public class Quota {
  private final String user;

  private final long byteUsed;
  private final long byteSoft;
  private final long byteHard;
  private final int bytePercentUsed;

  private final long fileUsed;
  private final long fileSoft;
  private final long fileHard;
  private final int filePercentUsed;
}
