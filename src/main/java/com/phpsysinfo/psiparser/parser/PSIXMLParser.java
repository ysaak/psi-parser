package com.phpsysinfo.psiparser.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.phpsysinfo.psiparser.HostData;
import com.phpsysinfo.psiparser.PSIParserException;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class PSIXMLParser extends PSIParser {

  @Override
  public HostData parseServerData(String serverXML) throws PSIParserException {
    // Build parser
    SAXParser parser = null;

    try {
      parser = SAXParserFactory.newInstance().newSAXParser();
    }
    catch (Exception e) {
        //Log.d("PSIAndroid", "XML_PARSER_CREATE");
        //errorCode = PSIErrorCode.XML_PARSER_CREATE;
        return null;
    }

    DefaultHandler handler = new PSIXmlHandler();

    try {
      // String to InputStream conversion
      // @see http://www.mkyong.com/java/how-to-convert-string-to-inputstream-in-java/

      InputStream is = new ByteArrayInputStream(serverXML.getBytes());

      parser.parse(is, handler);
    }
    catch (SAXException e) {
      throw new PSIParserException("Parsing", e);
    }
    catch (IOException e) {
      throw new PSIParserException("Parsing", e);
    }

    return ((PSIXmlHandler) handler).getData();
  }

  @Override
  protected URL getServerUri(String urlString) throws MalformedURLException {
    String urlStringXML = urlString;
    if (!urlStringXML.endsWith("/"))
      urlStringXML += "/";
    urlStringXML += "xml.php";

    return new URL(urlStringXML);
  }

}
