package org.mybatisx.extension.test;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.mybatisx.extension.agent.mybatis.DynamicMyBatisConfiguration;
import org.mybatisx.extension.agent.mybatis.XmlStatementParser;

public class Test {

  public static void main(String[] args) {
    String xml = "<select id=\"queryAll\" parameterType=\"map\" resultType=\"org.example.agentdemo.entity.Student\">\n" +
                 "        SELECT *\n" +
                 "        FROM student\n" +
                 "        where 1 = 1sdfdsfsdfsdfsdf\n" +
                 "    </select>";


    XmlStatementParser parser = new XmlStatementParser();
    MappedStatement mappedStatement = parser.parse(new DynamicMyBatisConfiguration(new Configuration()), "D:/1.xml", "com.mapper.StudentMapper", xml);

    System.out.println(mappedStatement);
  }
}
