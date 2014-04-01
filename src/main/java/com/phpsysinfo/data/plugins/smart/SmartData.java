package com.phpsysinfo.data.plugins.smart;

import java.util.Collection;

import lombok.Data;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

@Data
public class SmartData {
  private final Multimap<String, DiskAttribute> disks = HashMultimap.create();

  public void addDiskAttribute(final String diskName, DiskAttribute attribute) {
    disks.put(diskName, attribute);
  }

  public Collection<DiskAttribute> getAttributes(final String diskName) {
    return disks.get(diskName);
  }
}
