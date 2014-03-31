package com.phpsysinfo.psiparser.parser;

import com.phpsysinfo.data.MountPoint;
import com.phpsysinfo.data.PSIUps;
import com.phpsysinfo.data.hardware.Hardware.HardwareType;
import com.phpsysinfo.data.plugins.Plugin;
import com.phpsysinfo.psiparser.HostData;
import com.phpsysinfo.psiparser.utils.ConvertUtils;
import com.phpsysinfo.psiparser.utils.XmlParsingTools;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class PSIXmlHandler extends DefaultHandler {

  private HostData entry;

  private boolean inMemory = false;
  private boolean inSwapMemory = false;

  private boolean inHardware = false;
  private HardwareType hwType = null;

  private boolean inPluginImpi = false;
  private boolean inPluginImpiTemperature = false;

  private boolean inMbInfo = false;
  private boolean inMbInfoTemperature = false;
  private boolean inMbInfoFans = false;

  private boolean inPsStatus = false;

  private boolean inDisk = false;
  private String smartDiskName;

  private boolean inPackageUpdate = false;
  private boolean inSecurityUpdate = false;

  private StringBuilder buffer = new StringBuilder();

  @Override
  public void processingInstruction(String target, String data) throws SAXException {
    super.processingInstruction(target, data);
  }

  public PSIXmlHandler() {
    super();
  }

  @Override
  public void startDocument() throws SAXException {
    super.startDocument();
    entry = new HostData();
  }

  @Override
  public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {

    if (name.equalsIgnoreCase("Generation")) {
      entry.setPsiVersion(attributes.getValue("version"));
      entry.setUpdateTime(ConvertUtils.safeGetLong(attributes.getValue("timestamp")));
    }

    else if (name.equalsIgnoreCase("Vitals")) {
      entry.setVitals(XmlParsingTools.attributesToVitals(attributes));
    }

    else if (name.equalsIgnoreCase("Memory")) {
      entry.setMemory(XmlParsingTools.attributesToMemory(attributes));
      inMemory = true;
    }
    else if (inMemory && name.equalsIgnoreCase("Details")) {
      entry.getMemory().setDetails(XmlParsingTools.attributesToMemoryDetails(attributes));
    }
    else if (inMemory && name.equalsIgnoreCase("Swap")) {
      entry.getMemory().setSwap(XmlParsingTools.attributesToSwapMemory(attributes));
      inSwapMemory = true;
    }

    else if (name.equalsIgnoreCase("Mount")) {

      MountPoint mountPoint = XmlParsingTools.attributesToMountPoint(attributes);

      if (inSwapMemory) {
        entry.getMemory().getSwap().addSwapMountPoint(mountPoint);
      }
      else {
        entry.addFSMountPoint(mountPoint);
      }
    }

    else if (name.equalsIgnoreCase("Hardware")) {
      inHardware = true;
    }
    else if (inHardware && name.equals("PCI")) {
      hwType = HardwareType.PCI;
    }
    else if (inHardware && name.equals("USB")) {
      hwType = HardwareType.USB;
    }
    else if (inHardware && name.equals("IDE")) {
      hwType = HardwareType.IDE;
    }
    else if (inHardware && name.equals("SCSI")) {
      hwType = HardwareType.SCSI;
    }
    else if (inHardware && name.equalsIgnoreCase("Device")) {
      this.entry.getHardware().addDevice(hwType, XmlParsingTools.attributesToDevice(attributes));
    }
    else if (inHardware && name.equalsIgnoreCase("CpuCore")) {
      this.entry.getHardware().addCpuInfo(XmlParsingTools.attributesToCpuInfo(attributes));
    }

    else if (name.equalsIgnoreCase("NetDevice")) {
      entry.addNetworkInterface(XmlParsingTools.attributesToNetworkInterface(attributes));
    }

    // ------------------ PLUGINS ------------------

    // process status
    else if (name.equalsIgnoreCase("Plugin_PSStatus")) {
      inPsStatus = true;

      this.entry.getPlugins().setPluginAvailable(Plugin.PS_STATUS);
    }
    else if (inPsStatus && name.equalsIgnoreCase("Process")) {

      this.entry.getPlugins().getPsStatusData().setProcessStatus(
          attributes.getValue("Name"),
          ConvertUtils.safeGetInt(attributes.getValue("Status")) == 1
          );
    }

    // Update notifier
    // TODO: vérifier le format de sortie du plugin
    else if (name.equalsIgnoreCase("packages")) {
      inPackageUpdate = true;
      buffer.setLength(0);
    }

    else if (name.equalsIgnoreCase("security")) {
      inSecurityUpdate = true;
      buffer.setLength(0);
    }

    // Quotas
    else if (name.equalsIgnoreCase("Plugin_Quotas")) {
      this.entry.getPlugins().setPluginAvailable(Plugin.QUOTAS);
    }
    else if (name.equalsIgnoreCase("Quotas")) {
      this.entry.getPlugins().getQuotasData().addQuota(XmlParsingTools.attributesToQuota(attributes));
    }

    // ipmi
    else if (name.equalsIgnoreCase("Plugin_ipmi")) {
      inPluginImpi = true;
    }
    else if (inPluginImpi && name.equalsIgnoreCase("Temperature")) {
      inPluginImpiTemperature = true;
    }
    else if (inPluginImpiTemperature) {
      if (name.equalsIgnoreCase("Item")) {
        this.entry.addTemperature(attributes.getValue("Label"), attributes.getValue("Value"));
      }
    }

    // mb
    else if (name.equalsIgnoreCase("MBInfo")) {
      inMbInfo = true;
    }
    else if (inMbInfo && name.equalsIgnoreCase("Temperature")) {
      inMbInfoTemperature = true;
    }
    else if (inMbInfo && name.equalsIgnoreCase("Fans")) {
      inMbInfoFans = true;
    }
    else if (inMbInfoTemperature) {
      if (name.equalsIgnoreCase("Item")) {
        this.entry.addTemperature(attributes.getValue("Label"), attributes.getValue("Value"));
      }
    }
    else if (inMbInfoFans) {
      if (name.equalsIgnoreCase("Item")) {
        this.entry.addFans(attributes.getValue("Label"), attributes.getValue("Value"));
      }
    }


    // SMART
    else if (name.equalsIgnoreCase("Plugin_SMART")) {
      this.entry.getPlugins().setPluginAvailable(Plugin.SMART);
    }
    else if (name.equalsIgnoreCase("disk")) {
      inDisk = true;
      smartDiskName = attributes.getValue("name");
    }
    else if (inDisk && name.equalsIgnoreCase("attribute")) {
      this.entry.getPlugins().getSmartData().addDiskAttribute(smartDiskName, XmlParsingTools.attributesToDiskAttribute(attributes));
    }

    else if (name.equalsIgnoreCase("UPS")) {

      PSIUps ups = new PSIUps();

      ups.setName(attributes.getValue("Name"));
      ups.setModel(attributes.getValue("Model"));
      ups.setMode(attributes.getValue("Mode"));
      ups.setStartTime(attributes.getValue("StartTime"));
      ups.setStatus(attributes.getValue("Status"));
      ups.setTemperature(attributes.getValue("Temperature"));
      ups.setOutagesCount(attributes.getValue("OutagesCount"));
      ups.setLastOutage(attributes.getValue("LastOutage"));
      ups.setLastOutageFinish(attributes.getValue("LastOutageFinish"));
      ups.setLineVoltage(attributes.getValue("LineVoltage"));
      ups.setLoadPercent(attributes.getValue("LoadPercent"));
      ups.setBatteryVoltage(attributes.getValue("BatteryVoltage"));
      ups.setBatteryChargePercent(attributes.getValue("BatteryChargePercent"));
      ups.setTimeLeftMinutes(attributes.getValue("TimeLeftMinutes"));

      this.entry.setUps(ups);
    }

    else if (name.equalsIgnoreCase("Raid")) {
      this.entry.addRaid(attributes.getValue("Device_Name") + " (" + attributes.getValue("Level") + ")", attributes.getValue("Disks_Active"),
          attributes.getValue("Disks_Registered"));
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    super.characters(ch, start, length);

    if (inPackageUpdate || inSecurityUpdate) {
      buffer.append(ch);
    }
  }

  @Override
  public void endElement(String uri, String localName, String name) throws SAXException {

    if (name.equalsIgnoreCase("Memory")) {
      inMemory = false;
    }
    else if (inHardware && name.equalsIgnoreCase("Hardware")) {
      inHardware = false;
    }
    else if (inMemory && name.equalsIgnoreCase("Swap")) {
      inSwapMemory = false;
    }
    else if (localName.equalsIgnoreCase("Plugin_ipmi")) {
      inPluginImpi = false;
    }
    else if (localName.equalsIgnoreCase("MBInfo")) {
      inMbInfo = false;
    }
    else if (localName.equalsIgnoreCase("Temperature")) {
      inPluginImpiTemperature = false;
      inMbInfoTemperature = false;
    }
    else if (localName.equalsIgnoreCase("Fans")) {
      inMbInfoFans = false;
    }
    else if (localName.equalsIgnoreCase("Plugin_PSStatus")) {
      inPsStatus = false;
    }
    else if (localName.equalsIgnoreCase("disk")) {
      inDisk = false;
    }
    else if (localName.equalsIgnoreCase("packages")) {
      inPackageUpdate = false;
      this.entry.getPlugins().getUpdateNotifierData().setAvailableUpdates(
          ConvertUtils.safeGetInt(buffer.toString(), -1)
      );
    }
    else if (localName.equalsIgnoreCase("security")) {
      inSecurityUpdate = false;
      this.entry.getPlugins().getUpdateNotifierData().setSecurityUpdates(
          ConvertUtils.safeGetInt(buffer.toString(), -1)
      );
    }
  }

  public HostData getData() {
    return entry;
  }
}
