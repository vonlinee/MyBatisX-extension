package com.baomidou.mybatisx.plugin.provider;

import com.baomidou.mybatisx.dom.model.Mapper;
import com.baomidou.mybatisx.util.MapperIcon;
import com.baomidou.mybatisx.plugin.setting.MyBatisXSettings;
import com.baomidou.mybatisx.util.Icons;
import com.baomidou.mybatisx.util.MapperUtils;
import com.intellij.ide.IconProvider;
import com.intellij.lang.Language;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;
import java.util.Optional;

/**
 * mapper.xml 和 mapperClass 的文件图标修改为骚气的小鸟
 */
public class XmlAndMapperIconProvider extends IconProvider {
    private final MyBatisXSettings instance = MyBatisXSettings.getInstance();

    @Override
    public @Nullable Icon getIcon(@NotNull PsiElement element, int flags) {
        if (instance.getMapperIcon() != null && Objects.equals(MapperIcon.DEFAULT.name(), instance.getMapperIcon())) {
            return null;
        }
        Language language = element.getLanguage();
        if (language.is(JavaLanguage.INSTANCE)) {
            if (element instanceof PsiClass) {
                PsiClass mayMapperClass = (PsiClass) element;
                Optional<Mapper> firstMapper = MapperUtils.findFirstMapper(element.getProject(), mayMapperClass);
                if (firstMapper.isPresent()) {
                    return Icons.MAPPER_CLASS_ICON;
                }
            }
        }
        if (MapperUtils.isElementWithinMybatisFile(element)) {
            return Icons.MAPPER_XML_ICON;
        }
        return null;
    }
}
