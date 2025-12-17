package com.baomidou.mybatisx.util;

import com.baomidou.mybatisx.feat.mybatis.generator.dto.TemplateSettingDTO;
import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 通过xml读取元数据配置
 */
public class XmlUtils {

  public static Map<String, TemplateSettingDTO> loadTemplatesByFile(InputStream inputStream) throws IOException {
    try {
      //1.或去SAXParserFactory实例
      SAXParserFactory factory = SAXParserFactory.newInstance();
      //2.获取SAXParser实例
      SAXParser saxParser = factory.newSAXParser();
      //创建Handel对象
      SAXDemoHandel dh = new SAXDemoHandel();
      saxParser.parse(inputStream, dh);
      return dh.map;
    } catch (ParserConfigurationException | SAXException | IOException e) {
      throw new IOException("读取配置文件出错", e);
    }
  }

  static class SAXDemoHandel extends DefaultHandler {
    Stack<Object> objects = new Stack<>();
    Map<String, TemplateSettingDTO> map = new HashMap<>();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      super.startElement(uri, localName, qName, attributes);
      if (qName.equals("template")) {
        objects.push(new TemplateSettingDTO());
      } else if (qName.equals("property")) {
        TemplateSettingDTO peek = (TemplateSettingDTO) objects.peek();
        String name = attributes.getValue("name");
        String value = attributes.getValue("value");
        switch (name) {
          case "configName":
            peek.setConfigName(value);
            break;
          case "configFile":
            peek.setConfigFile(value);
            break;
          case "fileName":
            peek.setFileName(value);
            break;
          case "suffix":
            peek.setSuffix(value);
            break;
          case "packageName":
            peek.setPackageName(value);
            break;
          case "encoding":
            peek.setEncoding(value);
            break;
          case "basePath":
            peek.setBasePath(value);
            break;
        }
      }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
      super.endElement(uri, localName, qName);
      if (qName.equals("template")) {
        TemplateSettingDTO templateSettingDTO = (TemplateSettingDTO) objects.pop();
        map.put(templateSettingDTO.getConfigFile(), templateSettingDTO);
      }
    }
  }

  /**
   * <     &lt;
   * >     &gt;
   * &    *amp;
   * '      &aops;
   * "     &quot;
   *
   * @param xml xml content
   * @return escaped xml content
   */
  public static String escape(String xml) {
    return StringEscapeUtils.escapeXml(xml);
  }

  public static String escapeXmlBodyText(String xmlStr) {
    try {
      // 创建DOM解析器
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      // 从字符串解析XML
      Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
      // 处理整个文档
      processNode(doc.getDocumentElement());
      // 将DOM转换回字符串
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(writer));
      return writer.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static void processNode(Node node) {
    // 处理文本节点
    if (node.getNodeType() == Node.TEXT_NODE) {
      String text = node.getNodeValue();
      if (text != null && !text.trim().isEmpty()) {
        node.setNodeValue(escapeSpecialChars(text));
      }
    }
    // 递归处理子节点
    NodeList childNodes = node.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++) {
      processNode(childNodes.item(i));
    }
  }

  private static String escapeSpecialChars(String text) {
    if (text == null) {
      return null;
    }
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);
      switch (c) {
        case '&':
          // 检查是否是已存在的实体
          if (text.startsWith("&amp;", i) ||
              text.startsWith("&lt;", i) ||
              text.startsWith("&gt;", i) ||
              text.startsWith("&quot;", i) ||
              text.startsWith("&apos;", i)) {
            // 如果是已存在的实体，直接添加
            int end = text.indexOf(';', i);
            if (end != -1) {
              result.append(text, i, end + 1);
              i = end;
              continue;
            }
          }
          result.append("&amp;");
          break;
        case '<':
          result.append("&lt;");
          break;
        case '>':
          result.append("&gt;");
          break;
        case '"':
          result.append("&quot;");
          break;
        case '\'':
          result.append("&apos;");
          break;
        default:
          result.append(c);
      }
    }
    return result.toString();
  }
}
