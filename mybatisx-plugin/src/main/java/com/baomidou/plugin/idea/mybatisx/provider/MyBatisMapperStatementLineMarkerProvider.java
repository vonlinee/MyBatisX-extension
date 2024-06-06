package com.baomidou.plugin.idea.mybatisx.provider;

import com.baomidou.plugin.idea.mybatisx.dom.model.*;
import com.baomidou.plugin.idea.mybatisx.util.Icons;
import com.baomidou.plugin.idea.mybatisx.util.JavaUtils;
import com.baomidou.plugin.idea.mybatisx.util.MapperUtils;
import com.baomidou.plugin.idea.mybatisx.util.StringUtils;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlToken;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Set;
import java.util.*;

/**
 * Xml Mapper 文件中增删改查标记
 */
public class MyBatisMapperStatementLineMarkerProvider extends LineMarkerProviderDescriptor {

    private static final String MAPPER_CLASS = Mapper.class.getSimpleName().toLowerCase();
    private static final Set<String> TARGET_TYPES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            Select.class.getSimpleName().toLowerCase(),
            Insert.class.getSimpleName().toLowerCase(),
            Update.class.getSimpleName().toLowerCase(),
            Delete.class.getSimpleName().toLowerCase()
    )));

    /**
     * @param psiElement XmlToken
     * @return LineMarkerInfo
     */
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement psiElement) {
        if (!isTheElement(psiElement)) {
            return null;
        }
        Optional<? extends PsiElement[]> processResult = getTargets(psiElement);
        if (!processResult.isPresent()) {
            return null;
        }
        return processResult.map(psiElements -> new MapperLineMarkerInfo(psiElement, getIcon(), psiElements)).orElse(null);
    }

    @Override
    public String getName() {
        return getClass().getName();
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return Icons.STATEMENT_LINE_MARKER_ICON;
    }

    public Optional<? extends PsiElement[]> getTargets(@NotNull PsiElement from) {
        DomElement domElement = DomUtil.getDomElement(from);
        if (null == domElement) {
            return Optional.empty();
        } else if (domElement instanceof IdDomElement) { // 方法
            return JavaUtils.findMethods(from.getProject(),
                    MapperUtils.getNamespace(domElement),
                    MapperUtils.getId((IdDomElement) domElement));
        } else {
            XmlTag xmlTag = domElement.getXmlTag();
            if (xmlTag == null) {
                return Optional.empty();
            }
            String namespace = xmlTag.getAttributeValue("namespace");
            if (StringUtils.isEmpty(namespace)) {
                return Optional.empty();
            }
            return JavaUtils.findClasses(from.getProject(), namespace);
        }
    }

    public boolean isTheElement(@NotNull PsiElement element) {
        return element instanceof XmlToken
               && isTargetType((XmlToken) element)
               && MapperUtils.isElementWithinMybatisFile(element);
    }

    private boolean isTargetType(@NotNull XmlToken token) {
        boolean targetType = false;
        if (MAPPER_CLASS.equals(token.getText())) {
            // 判断当前元素是开始节点
            PsiElement nextSibling = token.getNextSibling();
            if (nextSibling instanceof PsiWhiteSpace) {
                targetType = true;
            }
        }
        if (!targetType) {
            if (TARGET_TYPES.contains(token.getText())) {
                PsiElement parent = token.getParent();
                // 判断当前节点是标签
                if (parent instanceof XmlTag) {
                    // 判断当前元素是开始节点
                    PsiElement nextSibling = token.getNextSibling();
                    if (nextSibling instanceof PsiWhiteSpace) {
                        targetType = true;
                    }
                }
            }
        }
        return targetType;
    }
}
