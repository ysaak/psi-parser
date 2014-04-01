package com.phpsysinfo.data.plugins.updatenotifier;

import lombok.Data;

@Data
public class UpdateNotifierData {
  private int availableUpdates;
  private int securityUpdates;
}
