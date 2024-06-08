package com.baomidou.mybatisx.feat.jpa;

import com.baomidou.mybatisx.dom.model.Mapper;
import com.baomidou.mybatisx.feat.jpa.common.MapperClassGenerateFactory;
import com.baomidou.mybatisx.feat.jpa.common.iftest.ConditionFieldWrapper;
import com.baomidou.mybatisx.feat.jpa.common.iftest.ConditionIfTestWrapper;
import com.baomidou.mybatisx.feat.jpa.operate.generate.Generator;
import com.baomidou.mybatisx.feat.jpa.operate.generate.PlatformDbGenerator;
import com.baomidou.mybatisx.feat.jpa.operate.generate.PlatformGenerator;
import com.baomidou.mybatisx.feat.jpa.operate.generate.PlatformSimpleGenerator;
import com.baomidou.mybatisx.feat.jpa.component.TxField;
import com.baomidou.mybatisx.feat.jpa.component.TypeDescriptor;
import com.baomidou.mybatisx.feat.jpa.component.mapping.EntityMappingHolder;
import com.baomidou.mybatisx.feat.jpa.component.mapping.EntityMappingResolverFactory;
import com.baomidou.mybatisx.plugin.ui.JpaAdvanceDialog;
import com.baomidou.mybatisx.util.MapperUtils;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiTypeElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 在mapper类中通过名字生成方法和xml内容
 *
 * @author ls9527
 */
public class GenerateSmartJpaAdvanceAction extends PsiElementBaseIntentionAction implements IntentionAction {

    private static final Logger logger = LoggerFactory.getLogger(GenerateSmartJpaAdvanceAction.class);

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
        try {

            PsiTypeElement statementElement = PsiTreeUtil.getParentOfType(element, PsiTypeElement.class);
            if (statementElement == null) {
                statementElement = PsiTreeUtil.getPrevSiblingOfType(element, PsiTypeElement.class);
            }
            PsiClass mapperClass = PsiTreeUtil.getParentOfType(statementElement, PsiClass.class);
            if (mapperClass == null) {
                logger.info("未找到mapper类");
                return;
            }
            EntityMappingResolverFactory entityMappingResolverFactory = new EntityMappingResolverFactory(project);
            EntityMappingHolder entityMappingHolder = entityMappingResolverFactory.searchEntity(mapperClass);
            PsiClass entityClass = entityMappingHolder.getEntityClass();
            if (entityClass == null) {
                logger.info("未找到实体类");
                return;
            }

            boolean hasDatabaseComponent = findDatabaseComponent();
            String text = statementElement.getText();

            PlatformSimpleGenerator platformSimpleGenerator = new PlatformSimpleGenerator();
            if (hasDatabaseComponent) {
                platformSimpleGenerator = new PlatformDbGenerator();
            }
            PlatformGenerator platformGenerator = platformSimpleGenerator.getPlatformGenerator(project, element, entityMappingHolder, text);
            // 不仅仅是参数的字符串拼接， 还需要导入的对象
            TypeDescriptor parameterDescriptor = platformGenerator.getParameter();

            // 插入到编辑器
            TypeDescriptor returnDescriptor = platformGenerator.getReturn();
            if (returnDescriptor == null) {
                logger.info("不支持的语法");
                return;
            }
            boolean isSelect = !returnDescriptor.getImportList().isEmpty();

            Optional<ConditionFieldWrapper> conditionFieldWrapperOptional = getConditionFieldWrapper(project,
                platformGenerator.getDefaultDateWord(),
                platformGenerator.getAllFields(),
                platformGenerator.getResultFields(),
                platformGenerator.getConditionFields(),
                platformGenerator.getEntityClass(),
                isSelect);
            if (!conditionFieldWrapperOptional.isPresent()) {
                logger.info("没找到合适的条件包装器, mapperClass: {}", mapperClass.getName());
                return;
            }

            ConditionFieldWrapper conditionFieldWrapper = conditionFieldWrapperOptional.get();

            generate(project, editor, statementElement, mapperClass, platformGenerator, parameterDescriptor, returnDescriptor, conditionFieldWrapper);
        } catch (ProcessCanceledException e) {
            logger.info("cancel info", e);
        }
    }

    private void generate(@NotNull Project project,
                          Editor editor,
                          PsiTypeElement statementElement,
                          PsiClass mapperClass,
                          PlatformGenerator platformGenerator,
                          TypeDescriptor parameterDescriptor,
                          TypeDescriptor returnDescriptor,
                          ConditionFieldWrapper conditionFieldWrapper
    ) {
        // 找到 mapper.xml 的 Mapper 对象
        Optional<Mapper> firstMapper = MapperUtils.findFirstMapper(project, mapperClass);
        if (firstMapper.isPresent()) {
            Mapper mapper = firstMapper.get();
            conditionFieldWrapper.setMapper(mapper);
        }


        MapperClassGenerateFactory mapperClassGenerateFactory =
            new MapperClassGenerateFactory(project,
                editor,
                statementElement,
                mapperClass,
                parameterDescriptor,
                conditionFieldWrapper,
                returnDescriptor);

        String newMethodString = mapperClassGenerateFactory.generateMethodStr(conditionFieldWrapper.getResultType());
        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
        final PsiMethod psiMethod = factory.createMethodFromText(newMethodString, mapperClass);

        List<TxField> resultTxFields = conditionFieldWrapper.getResultTxFields();
        final Generator generator = conditionFieldWrapper.getGenerator(mapperClassGenerateFactory);
        if (generator.checkCanGenerate(mapperClass)) {
            platformGenerator.generateMapperXml(
                psiMethod,
                conditionFieldWrapper,
                resultTxFields, generator);

        }
    }


    private boolean findDatabaseComponent() {
        try {
            Class.forName("com.intellij.database.psi.DbTable");
        } catch (NoClassDefFoundError | ClassNotFoundException ex) {
            return false;
        }
        return true;
    }

    /**
     * 创建 条件字段包装器， 用于if,where 这样的标签
     *
     * @param project         the project
     * @param defaultDateWord
     * @param allFields
     * @param resultFields
     * @param conditionFields
     * @param entityClass
     * @param isSelect
     * @return the condition field wrapper
     */
    protected Optional<ConditionFieldWrapper> getConditionFieldWrapper(@NotNull Project project,
                                                                       String defaultDateWord,
                                                                       List<TxField> allFields,
                                                                       List<String> resultFields,
                                                                       List<String> conditionFields,
                                                                       PsiClass entityClass,
                                                                       boolean isSelect) {
        // 弹出模态窗口
        JpaAdvanceDialog jpaAdvanceDialog = new JpaAdvanceDialog(project);
        jpaAdvanceDialog.initFields(conditionFields,
            resultFields,
            allFields,
            entityClass);
        jpaAdvanceDialog.show();

        // 模态窗口选择 OK, 生成相关代码
        if (jpaAdvanceDialog.getExitCode() != Messages.YES) {
            return Optional.empty();
        }
        Set<String> selectedFields = jpaAdvanceDialog.getSelectedFields();
        List<String> selectResultFields = null;
        List<String> resultFieldsFromDialog = jpaAdvanceDialog.getResultFields();
        if (!resultFieldsFromDialog.isEmpty()) {
            selectResultFields = resultFieldsFromDialog;
        }
        if (selectResultFields == null) {
            selectResultFields = resultFields;
        }
        ConditionIfTestWrapper conditionIfTestWrapper = new ConditionIfTestWrapper(project, selectedFields, selectResultFields, allFields, defaultDateWord);

        conditionIfTestWrapper.setAllFields(jpaAdvanceDialog.getAllFieldsStr());

        conditionIfTestWrapper.setResultMap(jpaAdvanceDialog.getResultMap());
        conditionIfTestWrapper.setResultType(jpaAdvanceDialog.isResultType());
        if (isSelect) {
            conditionIfTestWrapper.setResultTypeClass(jpaAdvanceDialog.getResultTypeClass());
        } else {
            // 对于update,insert,delete, count查询是没有返回类型的
            conditionIfTestWrapper.setResultTypeClass(null);
        }
        conditionIfTestWrapper.setGeneratorType(jpaAdvanceDialog.getGeneratorType());
        conditionIfTestWrapper.setDefaultDateList(jpaAdvanceDialog.getDefaultDate());
        conditionIfTestWrapper.setNewLine(jpaAdvanceDialog.getNewLine());
        return Optional.of(conditionIfTestWrapper);
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        String name = element.getContainingFile().getFileType().getName();
        if (!JavaFileType.INSTANCE.getName().equals(name)) {
            return false;
        }
        PsiMethod parentMethodOfType = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
        if (parentMethodOfType != null) {
            return false;
        }
        PsiClass parentClassOfType = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        if (parentClassOfType == null || (!parentClassOfType.isInterface())) {
            return false;
        }

        // 查找最近的有效节点, 找到Class类
        PsiTypeElement statementElement = PsiTreeUtil.getParentOfType(element, PsiTypeElement.class);
        if (statementElement == null) {
            statementElement = PsiTreeUtil.getPrevSiblingOfType(element, PsiTypeElement.class);
        }
        if (statementElement != null) {
            // 当前节点的父类不是mapper类就返回
            PsiClass mapperClass = PsiTreeUtil.getParentOfType(statementElement, PsiClass.class);
            return mapperClass != null;
        }
        return true;
    }


    @NotNull
    @Override
    public String getText() {
        return "[mybatisX] generate mybatis sql for advance";
    }

    @NotNull
    @Override
    public String getFamilyName() {
        return "Statement complete";
    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }
}
