package com.baomidou.mybatisx.dom.converter;

import com.baomidou.mybatisx.dom.model.IdDomElement;
import com.baomidou.mybatisx.dom.model.Mapper;
import com.intellij.util.xml.ConvertContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * The type Sql converter.
 *
 * @author yanglin
 */
public class SqlConverter extends IdBasedTagConverter {

  @NotNull
  @Override
  public Collection<? extends IdDomElement> getComparisons(@Nullable Mapper mapper, ConvertContext context) {
    if (mapper == null) {
      return Collections.emptyList();
    }
    return mapper.getSqlFragments();
  }

}
