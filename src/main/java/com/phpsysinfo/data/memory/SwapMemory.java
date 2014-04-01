package com.phpsysinfo.data.memory;


import java.util.ArrayList;
import java.util.List;

import com.phpsysinfo.data.MountPoint;


import lombok.Data;

@Data
public class SwapMemory {
  private final long swapOverallUsed;
  private final long swapOverallTotal;
  private final int swapOverallPercent;

  private final List<MountPoint> swapMointPoints = new ArrayList<MountPoint>();

  public void addSwapMountPoint(MountPoint mountPoint) {
    swapMointPoints.add(mountPoint);
  }
}
