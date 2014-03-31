package com.phpsysinfo.data.hardware;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

@Data
public class Hardware {
  public static enum HardwareType {
    PCI, USB, IDE, SCSI
  }

  private final Multimap<HardwareType, Device> devices = HashMultimap.create();
  private final List<CpuInfo> cpus = new ArrayList<CpuInfo>();

  public void addCpuInfo(CpuInfo info) {
    cpus.add(info);
  }

  public void addDevice(HardwareType type, Device dev) {
    devices.put(type, dev);
  }
}
