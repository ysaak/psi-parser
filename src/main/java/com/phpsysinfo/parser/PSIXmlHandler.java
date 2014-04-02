package com.phpsysinfo.parser;

import com.phpsysinfo.data.HostData;
import com.phpsysinfo.data.MountPoint;
import com.phpsysinfo.data.hardware.Hardware.HardwareType;
import com.phpsysinfo.data.plugins.Plugin;
import com.phpsysinfo.parser.utils.ConvertUtils;
import com.phpsysinfo.parser.utils.XmlParsingTools;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class PSIXmlHandler extends DefaultHandler {

  private HostData entry;
  private PSIError error = null;

  private boolean readData = false;

  private boolean inError = false;
  private String errorFunction;

  private boolean inMemory = false;
  private boolean inSwapMemory = false;

  private boolean inHardware = false;
  private HardwareType hwType = null;

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
    entry = new HostData();
  }

  @Override
  public void startDocument() throws SAXException {
    super.startDocument();
  }

  @Override
  public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {

    if (name.equalsIgnoreCase("Generation")) {
      entry.setPsiVersion(attributes.getValue("version"));
      entry.setUpdateTime(ConvertUtils.safeGetLong(attributes.getValue("timestamp")));
    }

    else if (name.equalsIgnoreCase("Error")) {
      inError = true;
    }
    else if (inError) {

      if (name.equalsIgnoreCase("Function")) {
        readData = true;
      }
      else if (name.equalsIgnoreCase("Message")) {
        readData = true;
      }
    }
    else {

      if (name.equalsIgnoreCase("Vitals")) {
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

      // Process status
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
      else if (name.equalsIgnoreCase("UpdateNotifier")) {
        this.entry.getPlugins().setPluginAvailable(Plugin.UPDATE_NOTIFIER);
      }
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
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    super.characters(ch, start, length);

    if (inPackageUpdate || inSecurityUpdate || readData) {
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
    else if (inError && name.equalsIgnoreCase("Function")) {
      readData = false;
      errorFunction = buffer.toString();
      buffer.setLength(0);
    }
    else if (inError && name.equalsIgnoreCase("Message")) {
      readData = false;

      error = new PSIError(errorFunction, buffer.toString());
      buffer.setLength(0);
    }
  }

  public HostData getData() throws PSIError {
    if (error != null) {
      throw error;
    }
    return entry;
  }
}
