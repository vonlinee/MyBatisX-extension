package org.mybatisx.extension.agent.mybatis;

import org.apache.ibatis.parsing.PropertyParser;
import org.w3c.dom.CharacterData;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Map;
import java.util.Properties;

/**
 * @see org.apache.ibatis.parsing.XNode
 * @see Node
 */
public class XmlNode {
  private final Node node;
  private final String name;
  private final String body;
  private final Properties attributes;
  private final Properties variables;

  public XmlNode(Node node, Properties variables) {
    this.node = node;
    this.name = node.getNodeName();
    this.variables = variables;
    this.attributes = parseAttributes(node);
    this.body = parseBody(node);
  }

  /**
   * 将内容以<![CDATA[ ]>进行包裹
   * 被<![CDATA[]]>这个标记所包含的内容将表示为纯文本，比如<![CDATA[<]]>表示文本内容“<”。
   * <a href="https://www.runoob.com/xml/xml-cdata.html">CDATA</a>
   *
   * @param content 待包裹的内容
   * @return 包裹结果
   */
  public static String wrapWithCDATA(String content) {
    return "<![CDATA[" + content + "]]>";
  }

  @Override
  public String toString() {
    return buildToString(new StringBuilder(), 0).toString();
  }

  private StringBuilder buildToString(StringBuilder builder, int indentLevel) {
    indent(builder, indentLevel).append("<").append(name);
    for (Map.Entry<Object, Object> entry : attributes.entrySet()) {
      builder.append(" ");
      builder.append(entry.getKey());
      builder.append("=\"");
      builder.append(entry.getValue());
      builder.append("\"");
    }

    NodeList nodeList = node.getChildNodes();
    if (nodeList.getLength() == 0) {
      builder.append(" />\n");
    } else {
      builder.append(">\n");
      for (int i = 0, n = nodeList.getLength(); i < n; i++) {
        Node node = nodeList.item(i);
        short nodeType = node.getNodeType();
        if (nodeType == Node.ELEMENT_NODE) {
          // 子标签元素
          new XmlNode(node, variables).buildToString(builder, indentLevel + 1);
        } else {
          String bodyData = getBodyData(node);
          if (bodyData != null) {
            String text = bodyData.trim();
            if (!text.isEmpty()) {
              indent(builder, indentLevel + 1).append(text).append("\n");
            }
          }
        }
      }
      indent(builder, indentLevel).append("</").append(name).append(">\n");
    }
    return builder;
  }

  private StringBuilder indent(StringBuilder builder, int level) {
    int count = Math.max(0, level);
    for (int i = 0; i < count; i++) {
      builder.append("  ");
    }
    return builder;
  }

  /**
   * 获取节点的内容
   *
   * @param child Node
   * @return body
   */
  private String getBodyData(Node child) {
    String bodyData = null;
    if (child instanceof CharacterData) {

      CharacterData characterData = (CharacterData) child;
      // CDATA节点
      if (child.getNodeType() == Node.CDATA_SECTION_NODE) {
        String data = characterData.getData();
        if (data != null && data.contains("{")) {
          bodyData = PropertyParser.parse(data, variables);
        } else {
          bodyData = data;
          // 使用 CDATA标签进行包裹
          bodyData = wrapWithCDATA(bodyData);
        }
      }
      if (child.getNodeType() == Node.TEXT_NODE) { // 文本节点
        String data = characterData.getData();
        bodyData = PropertyParser.parse(data, variables);
      }
    }
    return bodyData;
  }

  private String parseBody(Node node) {
    String data = getBodyData(node);
    if (data == null) {
      NodeList children = node.getChildNodes();
      for (int i = 0; i < children.getLength(); i++) {
        Node child = children.item(i);
        data = getBodyData(child);
        if (data != null) {
          break;
        }
      }
    }
    return data;
  }

  private Properties parseAttributes(Node n) {
    Properties attributes = new Properties();
    NamedNodeMap attributeNodes = n.getAttributes();
    if (attributeNodes != null) {
      for (int i = 0; i < attributeNodes.getLength(); i++) {
        Node attribute = attributeNodes.item(i);
        String value = PropertyParser.parse(attribute.getNodeValue(), variables);
        attributes.put(attribute.getNodeName(), value);
      }
    }
    return attributes;
  }
}
