package com.baomidou.mybatisx.plugin.provider;

import com.baomidou.mybatisx.util.MyBatisUtils;
import com.baomidou.mybatisx.util.PsiUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlToken;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;

@Setter
@Getter
public class MapperStatementItem {

    /**
     * 当前项目实例
     */
    private Project project;

    /**
     * Mapper 文件路径
     */
    private String mapperXmlFileLocation;

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * XmlTag类型，通过XmlToken#getParent()即可拿到对应的XmlTag
     */
    private PsiElement element;

    /**
     * select|insert|update|delete
     */
    private String statementType;

    /**
     * 整个标签的文本
     */
    private String content;

    /**
     * 鼠标点击事件
     */
    private MouseEvent originEvent;

    /**
     * @param element Mapper 标签元素
     */
    public MapperStatementItem(MouseEvent event, @Nullable PsiElement element) {
        this.originEvent = event;
        this.element = element;
        if (element != null) {
            this.project = element.getProject();
            if (element instanceof XmlElement) {
                this.namespace = MyBatisUtils.getNamespace((XmlElement) element);
                if (element instanceof XmlToken) {
                    PsiElement parent = element.getParent();
                    if (parent instanceof XmlTag) {
                        this.content = parent.getText();
                    }
                }
            }
            this.mapperXmlFileLocation = PsiUtils.getPathOfContainingFile(element);
        }
    }
}
