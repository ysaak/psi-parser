package com.phpsysinfo.data;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

import com.phpsysinfo.data.hardware.Hardware;
import com.phpsysinfo.data.memory.Memory;
import com.phpsysinfo.data.plugins.Plugins;

@Data
public class HostData {
  private String psiVersion = "";
  private long updateTime = 0;

  private Vitals vitals;

  private final List<NetworkInterface> networkInterfaces = new ArrayList<NetworkInterface>();

  private Memory memory;

  private final Map<String, MountPoint> fileSystem = new HashMap<String, MountPoint>();

  private final Hardware hardware = new Hardware();

  // Plugins
  private final Plugins plugins = new Plugins();

  public void addFSMountPoint(MountPoint mountPoint) {
    fileSystem.put(mountPoint.getName(), mountPoint);
  }

  public void addNetworkInterface(NetworkInterface networkInterface) {
    networkInterfaces.add(networkInterface);
  }
}
