package com.baomidou.mybatisx.plugin.structure;

import com.intellij.ide.structureView.xml.XmlStructureViewBuilderProvider;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MyBatisMapperXmlStructureViewFactoryTest {

  @Test
  public void shouldRegisterAsXmlStructureViewBuilderProvider() {
    assertTrue(new MyBatisMapperXmlStructureViewFactory() instanceof XmlStructureViewBuilderProvider);
  }
}
