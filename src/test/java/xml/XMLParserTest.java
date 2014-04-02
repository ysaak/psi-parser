package xml;

import java.io.IOException;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.phpsysinfo.data.HostData;
import com.phpsysinfo.parser.PSIError;
import com.phpsysinfo.parser.PSIParser;
import com.phpsysinfo.parser.PSIParserException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class XMLParserTest {

  public XMLParserTest() {
    // TODO Auto-generated constructor stub
  }

  @Test
  public void testXML() throws IOException, PSIParserException {

    String fileContent = Resources.toString(Resources.getResource("psi_full_xml.xml"), Charsets.UTF_8);


    final PSIParser parser = PSIParser.getXMLParser();
    HostData data;
    try {
      data = parser.parseServerData(fileContent);
      Assert.assertEquals(data.getPsiVersion(), "3.1.11");
    }
    catch (PSIError e) {
      Assert.fail(e.getMessage());
    }
  }
}
