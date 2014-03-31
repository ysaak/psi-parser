package com.phpsysinfo.data.plugins.psstatus;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class PSStatusData {
  private final Map<String, Boolean> processStatuses = new HashMap<String, Boolean>();

  public void setProcessStatus(final String process, boolean status) {
    processStatuses.put(process, Boolean.valueOf(status));
  }
}
