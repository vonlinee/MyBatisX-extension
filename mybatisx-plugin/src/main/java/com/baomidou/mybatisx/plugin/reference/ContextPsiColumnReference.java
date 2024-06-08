package com.baomidou.mybatisx.plugin.reference;

import com.baomidou.mybatisx.feat.jpa.component.mapping.EntityMappingHolder;
import com.baomidou.mybatisx.feat.jpa.component.mapping.EntityMappingResolverFactory;
import com.baomidou.mybatisx.util.StringUtils;
import com.intellij.database.model.DasColumn;
import com.intellij.database.model.DasNamespace;
import com.intellij.database.model.DasTable;
import com.intellij.database.model.ObjectKind;
import com.intellij.database.psi.DbDataSource;
import com.intellij.database.psi.DbElement;
import com.intellij.database.psi.DbPsiFacade;
import com.intellij.database.util.DasUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.containers.JBIterable;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * The type Context psi field reference.
 *
 * @author yanglin
 */
@Getter
@Setter
public class ContextPsiColumnReference extends PsiReferenceBase<XmlAttributeValue> {

    /**
     * The Resolver.
     */
    protected PsiColumnReferenceSetResolver resolver;
    /**
     * The Index.
     */
    protected int index;
    private PsiClass mapperClass;

    /**
     * Instantiates a new Context psi field reference.
     *
     * @param element     the element
     * @param range       the range
     * @param index       the index
     * @param mapperClass
     */
    public ContextPsiColumnReference(XmlAttributeValue element, TextRange range, int index, PsiClass mapperClass) {
        super(element, range, false);
        this.index = index;
        resolver = new PsiColumnReferenceSetResolver(element);
        this.mapperClass = mapperClass;
    }

    /**
     * 如果能找到正确的列, 线条转到正确的列
     * 无法找到数据库的列, 引用当前节点
     *
     * @return
     */
    @Nullable
    @Override
    public PsiElement resolve() {
        PsiElement element = null;
        Optional<DasTable> resolved = resolver.resolve(index);
        // 找到表则加入字段验证
        if (resolved.isPresent()) {
            DasTable dasTable = resolved.get();
            Optional<DbElement> columns = resolver.findColumns(dasTable);
            if (columns.isPresent()) {
                element = columns.get();
            }
        }
        return element;
    }

    /**
     * 获取用于提示的变量列表
     *
     * @return
     */
    @NotNull
    @Override
    public Object[] getVariants() {
        Project project = getElement().getProject();

        EntityMappingResolverFactory entityMappingResolverFactory
                = new EntityMappingResolverFactory(project);
        EntityMappingHolder entityMappingHolder = entityMappingResolverFactory.searchEntity(mapperClass);
        String tableName = entityMappingHolder.getTableName();
        if (StringUtils.isEmpty(tableName)) {
            return new Object[0];
        }
        DbPsiFacade dbPsiFacade = DbPsiFacade.getInstance(project);
        List<DbElement> dbElementList = getDbElements(tableName, dbPsiFacade);
        return !dbElementList.isEmpty() ? dbElementList.toArray() : PsiReference.EMPTY_ARRAY;
    }

    @NotNull
    private List<DbElement> getDbElements(String tableName, DbPsiFacade dbPsiFacade) {
        for (DbDataSource dataSource : dbPsiFacade.getDataSources()) {
            JBIterable<? extends DasNamespace> schemas = DasUtil.getSchemas(dataSource);
            for (DasNamespace schema : schemas) {
                DasTable dasTable = DasUtil.findChild(schema, DasTable.class, ObjectKind.TABLE, tableName);
                if (dasTable != null) {
                    List<DbElement> dbElementList = new LinkedList<>();
                    JBIterable<? extends DasColumn> columns = DasUtil.getColumns(dasTable);
                    for (DasColumn column : columns) {
                        DbElement element = dbPsiFacade.findElement(column);
                        dbElementList.add(element);
                    }
                    return dbElementList;
                }
            }
        }
        return Collections.emptyList();
    }
}
