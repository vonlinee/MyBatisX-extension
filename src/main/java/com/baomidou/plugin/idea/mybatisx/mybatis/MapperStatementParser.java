package com.baomidou.plugin.idea.mybatisx.mybatis;

import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;

public class MapperStatementParser {

    public XNode getNode(String mapperStatementXml) {
        return getNode(mapperStatementXml, null);
    }

    public XNode getNode(String mapperStatementXml, SqlStatementType statementType) {
        XPathParser xPathParser = new XPathParser(mapperStatementXml, false, null, new IgnoreDTDEntityResolver());
        XNode xNode;
        if (statementType == null) {
            // 直接取根节点
            xNode = xPathParser.evalNode("/*");
        } else {
            // 取指定标签名称的节点
            xNode = xPathParser.evalNode(statementType.name().toLowerCase());
        }
        return xNode;
    }
}
