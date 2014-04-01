package com.phpsysinfo.data.plugins;

import java.util.EnumSet;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import com.phpsysinfo.data.plugins.psstatus.PSStatusData;
import com.phpsysinfo.data.plugins.quotas.QuotasData;
import com.phpsysinfo.data.plugins.smart.SmartData;
import com.phpsysinfo.data.plugins.updatenotifier.UpdateNotifierData;

@Data
@Setter(AccessLevel.NONE)
public class Plugins {
  private final EnumSet<Plugin> availablePlugins = EnumSet.noneOf(Plugin.class);

  private PSStatusData psStatusData;

  private UpdateNotifierData updateNotifierData;

  private QuotasData quotasData;

  private SmartData smartData;

  public void setPluginAvailable(Plugin plugin) {
    availablePlugins.add(plugin);

    switch (plugin) {
      case PS_STATUS: psStatusData = new PSStatusData(); break;
      case UPDATE_NOTIFIER: updateNotifierData = new UpdateNotifierData(); break;
      case QUOTAS: quotasData = new QuotasData(); break;
      case SMART: smartData = new SmartData(); break;
    }
  }
}
