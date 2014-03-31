package xml;

import java.io.IOException;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import org.testng.annotations.Test;

public class XMLParserTest {

  public XMLParserTest() {
    // TODO Auto-generated constructor stub
  }

  @Test
  public void testXML() throws IOException {

    String fileContent = Resources.toString(Resources.getResource("psi_full_xml.xml"), Charsets.UTF_8);

    System.err.println(fileContent.length());

  }
}
