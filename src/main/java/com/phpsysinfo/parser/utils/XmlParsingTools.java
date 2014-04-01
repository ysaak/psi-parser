package com.phpsysinfo.parser.utils;


import com.google.common.base.Splitter;
import com.phpsysinfo.data.MountPoint;
import com.phpsysinfo.data.NetworkInterface;
import com.phpsysinfo.data.Vitals;
import com.phpsysinfo.data.hardware.CpuInfo;
import com.phpsysinfo.data.hardware.Device;
import com.phpsysinfo.data.memory.Memory;
import com.phpsysinfo.data.memory.MemoryDetails;
import com.phpsysinfo.data.memory.SwapMemory;
import com.phpsysinfo.data.plugins.quotas.Quota;
import com.phpsysinfo.data.plugins.smart.DiskAttribute;

import org.xml.sax.Attributes;

public class XmlParsingTools {

  /**
   * Convert XML attributes to the PSIVitals objets
   * @param attributes XML attributes
   * @return Vitals object
   */
  public static final Vitals attributesToVitals(Attributes attributes) {

    final String loadAgvStr = attributes.getValue("LoadAvg");

    double[] loadAvg = new double[] { 0.0, 0.0, 0.0 };

    int i = 0;
    for (String loadStr : Splitter.on(' ').split(loadAgvStr)) {

      loadAvg[i] = ConvertUtils.safeGetDouble(loadStr);

      i++;
      if (i == 3) {
        break;
      }
    }

    return new Vitals(
        attributes.getValue("Hostname"),
        attributes.getValue("IPAddr"),
        attributes.getValue("Kernel"),
        attributes.getValue("Distro"),
        attributes.getValue("Distroicon"),
        ConvertUtils.safeGetDouble(attributes.getValue("Uptime")),
        ConvertUtils.safeGetInt(attributes.getValue("Users")),
        loadAvg,
        ConvertUtils.safeGetDouble(attributes.getValue("CPULoad"))
    );
  }

  /**
   * Convert XML attributes to the PSINetworkInterface object
   * @param attributes XML attributes
   * @return Network interface object
   */
  public static final NetworkInterface attributesToNetworkInterface(Attributes attributes) {
    return new NetworkInterface(
        attributes.getValue("Name"),
        ConvertUtils.safeGetLong(attributes.getValue("RxBytes")),
        ConvertUtils.safeGetLong(attributes.getValue("TxBytes")),
        ConvertUtils.safeGetInt(attributes.getValue("Err")),
        ConvertUtils.safeGetInt(attributes.getValue("Drops")),
        attributes.getValue("Info")
    );
  }

  public static final Memory attributesToMemory(Attributes attributes) {
    return new Memory(
        ConvertUtils.safeGetLong(attributes.getValue("Used")),
        ConvertUtils.safeGetLong(attributes.getValue("Total")),
        ConvertUtils.safeGetInt(attributes.getValue("Percent"))
    );
  }

  public static final MemoryDetails attributesToMemoryDetails(Attributes attributes) {
    return new MemoryDetails(
        ConvertUtils.safeGetLong(attributes.getValue("App")),
        ConvertUtils.safeGetInt(attributes.getValue("AppPercent")),
        ConvertUtils.safeGetLong(attributes.getValue("Buffers")),
        ConvertUtils.safeGetInt(attributes.getValue("BuffersPercent")),
        ConvertUtils.safeGetLong(attributes.getValue("Cached")),
        ConvertUtils.safeGetInt(attributes.getValue("CachedPercent"))
    );
  }

  public static final SwapMemory attributesToSwapMemory(Attributes attributes) {
    return new SwapMemory(
        ConvertUtils.safeGetLong(attributes.getValue("Used")),
        ConvertUtils.safeGetLong(attributes.getValue("Total")),
        ConvertUtils.safeGetInt(attributes.getValue("Percent"))
    );
  }


  public static final MountPoint attributesToMountPoint(Attributes attributes) {
    MountPoint mp = new MountPoint(
        attributes.getValue("Name"),
        ConvertUtils.safeGetLong(attributes.getValue("Used")),
        ConvertUtils.safeGetLong(attributes.getValue("Total")),
        ConvertUtils.safeGetInt(attributes.getValue("Percent")),
        attributes.getValue("FSType"),
        attributes.getValue("MountPoint"),
        ConvertUtils.safeGetInt(attributes.getValue("Inodes"))
    );

    final String options = attributes.getValue("MountOptions");
    if (options != null) {

      for (String option : Splitter.on(',').trimResults().split(options)) {
        mp.addOption(option);
      }
    }

    return mp;
  }

  public static final Device attributesToDevice(Attributes attributes) {
    return new Device(
        attributes.getValue("Name"),
        ConvertUtils.safeGetInt(attributes.getValue("Count"))
    );
  }

  public static final CpuInfo attributesToCpuInfo(Attributes attributes) {
    return new CpuInfo(
        attributes.getValue("Model"),
        ConvertUtils.safeGetDouble(attributes.getValue("CpuSpeed")),
        ConvertUtils.safeGetLong(attributes.getValue("Cache")),
        attributes.getValue("Virt"),
        ConvertUtils.safeGetDouble(attributes.getValue("Bogomips")),
        ConvertUtils.safeGetDouble(attributes.getValue("Load"))
    );
  }

  public static final Quota attributesToQuota(Attributes attributes) {
    return new Quota(
        attributes.getValue("User"),

        ConvertUtils.safeGetLong(attributes.getValue("ByteUsed")),
        ConvertUtils.safeGetLong(attributes.getValue("ByteSoft")),
        ConvertUtils.safeGetLong(attributes.getValue("ByteHard")),
        ConvertUtils.safeGetInt(attributes.getValue("BytePercentUsed")),

        ConvertUtils.safeGetLong(attributes.getValue("FileUsed")),
        ConvertUtils.safeGetLong(attributes.getValue("FileSoft")),
        ConvertUtils.safeGetLong(attributes.getValue("FileHard")),
        ConvertUtils.safeGetInt(attributes.getValue("FilePercentUsed"))
    );
  }

  public static final DiskAttribute attributesToDiskAttribute(Attributes attributes) {
    return new DiskAttribute(
        ConvertUtils.safeGetInt("id"),
        attributes.getValue("attribute_name"),
        ConvertUtils.safeGetLong("raw_value")
    );
  }
}
