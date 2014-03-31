package com.phpsysinfo.psiparser;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

import com.phpsysinfo.data.MountPoint;
import com.phpsysinfo.data.NetworkInterface;
import com.phpsysinfo.data.PSIRaid;
import com.phpsysinfo.data.PSIUps;
import com.phpsysinfo.data.Vitals;
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

  private HashMap<String, String> temperature = new HashMap<String, String>();
  private HashMap<String, String> fans = new HashMap<String, String>();


  private HashMap<String, String> smart = new HashMap<String, String>();

  private List<PSIRaid> raid = new ArrayList<PSIRaid>();

  private PSIUps ups = null;

  public void addFSMountPoint(MountPoint mountPoint) {
    fileSystem.put(mountPoint.getName(), mountPoint);
  }

  public void addTemperature(String label, String value) {
    temperature.put(label, value);
  }

  public HashMap<String, String> getTemperature() {
    return temperature;
  }

  public void addFans(String label, String value) {
    fans.put(label, value);
  }

  public HashMap<String, String> getFans() {
    return fans;
  }


  public void addNetworkInterface(NetworkInterface networkInterface) {
    networkInterfaces.add(networkInterface);
  }

  public void addSmart(String attr, String value) {
    smart.put(attr, value);
  }

  public HashMap<String, String> getSmart() {
    return smart;
  }

  public PSIUps getUps() {
    return ups;
  }

  public void setUps(PSIUps ups) {
    this.ups = ups;
  }

  public List<PSIRaid> getRaid() {
    return raid;
  }

  public void addRaid(String name, String active, String registered) {
    int _active = 0;
    int _registered = 0;

    if (active != null && !active.equals("")) {
      _active = Integer.parseInt(active);
    }

    if (registered != null && !registered.equals("")) {
      _registered = Integer.parseInt(registered);
    }

    raid.add(new PSIRaid(name, _active, _registered));
  }
}
