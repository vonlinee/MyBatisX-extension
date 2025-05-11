package com.baomidou.mybatisx.feat.mybatis;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 忽略DTD验证
 * DTD验证需要联网
 */
public final class IgnoreDTDEntityResolver implements EntityResolver {

  @Override
  public InputSource resolveEntity(String arg0, String arg1) throws SAXException, IOException {
    return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
  }
}
