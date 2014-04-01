package com.phpsysinfo.parser;


import java.io.IOException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.phpsysinfo.data.HostData;
import com.phpsysinfo.parser.utils.HttpUtils;


public abstract class PSIParser {

  public abstract HostData parseServerData(String serverXML) throws PSIParserException;
  protected abstract URL getServerUri(String urlString) throws MalformedURLException;

  public static PSIParser getXMLParser() {
    return new PSIXMLParser();
  }

  public HostData getServerData(String urlString, String username, String password) throws PSIParserException {

    final URL serverURL;
    try {
      serverURL = getServerUri(urlString);
    }
    catch (MalformedURLException e1) {
      throw new PSIParserException("get_url");
    }

    HttpUtils.installSSLTrust();

    if (username != null && password != null) {
      Authenticator.setDefault(HttpUtils.getBasicAuthenticator(username, password));
    }

    String serverData = null;
    try {
      serverData = Resources.toString(serverURL, Charsets.UTF_8);
      return parseServerData(serverData);
    }
    catch (IOException e) {
      throw new PSIParserException("get_data");
    }
    finally {
      Authenticator.setDefault(null);
    }
  }
}
