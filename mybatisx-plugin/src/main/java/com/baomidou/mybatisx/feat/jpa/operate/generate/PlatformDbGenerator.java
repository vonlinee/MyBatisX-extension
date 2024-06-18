package com.baomidou.mybatisx.feat.jpa.operate.generate;

import com.baomidou.mybatisx.feat.jpa.db.DasTableAdaptor;
import com.baomidou.mybatisx.feat.jpa.db.DbmsAdaptor;
import com.baomidou.mybatisx.util.StringUtils;
import com.intellij.database.model.DasTable;
import com.intellij.database.psi.DbDataSource;
import com.intellij.database.psi.DbPsiFacade;
import com.intellij.database.util.DasUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.sql.dialects.SqlLanguageDialect;
import com.intellij.sql.psi.SqlPsiFacade;
import com.intellij.util.containers.JBIterable;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * The type Platform db generator.
 */
public class PlatformDbGenerator extends PlatformSimpleGenerator {

    @Override
    protected DbmsAdaptor getDbmsAdaptor(@NotNull Project project, @NotNull PsiElement element) {
        DbmsAdaptor dbms = DbmsAdaptor.MYSQL;


        // 社区版不提供这个服务, 只能提供mysql方式的提示和生成
        try {
            SqlPsiFacade instance = SqlPsiFacade.getInstance(project);
            SqlLanguageDialect dialectMapping = instance.getDialectMapping(element.getContainingFile()
                .getVirtualFile());
            dbms = DbmsAdaptor.castOf(dialectMapping.getDbms());
        } catch (NoClassDefFoundError ignore) {
        }
        return dbms;
    }

    @Override
    protected String getTableName(PsiClass entityClass, Project project, String foundTableName, DasTableAdaptor dasTableAdaptor) {
        if (StringUtils.isNotBlank(foundTableName)) {
            return foundTableName;
        }
        DbPsiFacade dbPsiFacade = DbPsiFacade.getInstance(project);
        List<DbDataSource> dataSources = dbPsiFacade.getDataSources();
        // 如果有多个候选值, 就选择长度最长的
        PriorityQueue<String> priorityQueue = new PriorityQueue<>(Comparator.comparing(String::length, Comparator.reverseOrder()));
        if (!dataSources.isEmpty()) {
            for (DbDataSource dataSource : dataSources) {
                JBIterable<? extends DasTable> tables = DasUtil.getTables(dataSource);
                for (DasTable table : tables) {
                    String entityTableName = Objects.requireNonNull(entityClass.getName()).toLowerCase();
                    String tableName = table.getName();
                    String guessTableName = tableName.replaceAll("_", "").toUpperCase();
                    // 完全相等的情况下就不用候选了
                    if (guessTableName.equalsIgnoreCase(entityTableName)) {
                        // 第一版的猜测数据源，只做绝对相等的情况
                        dasTableAdaptor.setDasTable(table);
                        return tableName;
                    }
                    // 加入候选
                    if (guessTableName.contains(entityTableName.toUpperCase())) {
                        priorityQueue.add(guessTableName);
                    }
                }
            }
        }
        // 存在候选的情况下, 返回表名最长的
        if (!priorityQueue.isEmpty()) {
            return priorityQueue.peek();
        }
        return Objects.requireNonNull(entityClass.getName()).toUpperCase();
    }

}
