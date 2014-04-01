package com.phpsysinfo.data.plugins.quotas;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class QuotasData {
  private final Map<String, Quota> quotas = new HashMap<String, Quota>();

  public void addQuota(Quota quota) {
    quotas.put(quota.getUser(), quota);
  }
}
