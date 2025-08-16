package com.baomidou.mybatisx.plugin.actions;

import com.baomidou.mybatisx.feat.ddl.Convertor;
import com.baomidou.mybatisx.feat.ddl.TableField;
import com.baomidou.mybatisx.feat.ddl.TypeTranslator;
import com.baomidou.mybatisx.plugin.ui.DDLResultDialog;
import com.intellij.lang.jvm.annotation.JvmAnnotationAttribute;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

public abstract class CreatorSupport extends AnAction {

  public abstract String createDDL(AnActionEvent event, PsiClass psiClass);

  /**
   * 执行分析生成操作
   *
   * @param e
   */
  @Override
  public void actionPerformed(AnActionEvent e) {
    // TODO: insert action logic here
    // 获取当前编辑的文件, 通过PsiFile可获得PsiClass, PsiField等对象
    PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
    if (psiFile == null) {
      return;
    }
    PsiJavaFile javaFile = (PsiJavaFile) psiFile;
    //获得当前编辑的类，如果定义了多个类就直接返回多个DDL语句 当然几乎不可能
    PsiClass[] psiClasses = javaFile.getClasses();
    StringBuilder result = new StringBuilder();
    for (PsiClass pClass : psiClasses) {
      // 分析并获得DDL语句
      String ddl = createDDL(e, pClass);
      result.append(ddl);
    }
    //打开Dialog 显示DDL语句
    openDialog(result.toString());
  }

  /**
   * 打开结果对话框
   *
   * @param result
   */
  protected void openDialog(String result) {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int w = (int) (screenSize.width * 0.3);
    int h = (int) (screenSize.height * 0.3);
    DDLResultDialog dialog = new DDLResultDialog(result);
    dialog.setSize(w, h);
    dialog.pack();
    dialog.setLocation((int) (screenSize.width * 0.5) - (int) (w * 0.5), (int) (screenSize.height * 0.5) - (int) (h * 0.5));
    dialog.setVisible(true);
  }

  /**
   * 分析代码中的字段内容获得TableField对象
   *
   * @param psiField
   * @return
   */
  protected TableField getTableField(PsiField psiField) {
    //如果是常量或者是静态变量直接忽略
    if (psiField.hasModifierProperty(PsiModifier.FINAL) || psiField.hasModifierProperty(PsiModifier.STATIC)) {
      return null;
    }
    //获得字段的类型名称
    String typeStr = psiField.getType().getCanonicalText();
    //这个类主要是封装 SQL字段的length、name、type、desc等等
    TableField tableField = new TableField();
    //获得字段名并转换成下划线形式
    tableField.setName(Convertor.camel2Underline(psiField.getName()));

    //获得是否有文档注释如果有直接用到SQL字段声明的 COMMENT里面去
    if (psiField.getDocComment() != null) {
      //调用clearDesc方法主要是自己定义了一个文档整理的方法去除空格换行 星号等等
      tableField.setDesc(clearDesc(psiField.getDocComment().getText()));
    }
    //默认认为不是主键
    tableField.setPrimaryKey(false);
    tableField.setGeneratedValue(false);

    //通过自己定义的一个Convertor.getFieldGetterName 将字段名 转换获得其Getter方法
    String getterName = Convertor.getFieldGetterName(psiField.getName());

    //获得Getter方法名称后 通过名称获得方法对象数组  最后有个Boolean参数 我看API的源代码 原来是是否在父类里面找，不能写写注释吗真的是
    PsiMethod[] getterMethods = Objects.requireNonNull(psiField.getContainingClass()).findMethodsByName(getterName, true);

    //如果有Getter方法 先去Getter方法 里面去获得annotation 信息并注入到tableField对象里面去
    if (getterMethods.length > 0) {
      for (PsiMethod targetMethod : getterMethods) {
        targetMethod.getParameterList();
        if (targetMethod.getParameterList().isEmpty()) {
          //这个是分析JPA annotation的 下面代码里有 详细解释
          tableField = tableFieldPropertyFillIn(tableField, psiField, targetMethod.getAnnotations());
          break;
        }
      }
      //如果返回空 证明这个字段需要忽略直接返回空忽略
      if (tableField == null) {
        return null;
      }
    }

    //然后通过属性去获得JPA annotation信息 这个将会覆盖方法的JPA annotation信息 所以 如果出现两个一样的JPA annotation内容 会以field的为主
    tableField = tableFieldPropertyFillIn(tableField, psiField, psiField.getAnnotations());

    //该忽略就忽略
    if (tableField == null) {
      return null;
    }

    //这里需要注意的是 如果是ManyToOne的话 需要去对应的映射当中获得ID字段 然后分析获得其SQL字段类型 所以我们会在这里做一次转换转换成SQL 字段类型后下次就不需要转了
    if (!tableField.isHasTypeTranslate()) {

      //这里是自己写的一个类型转换器，将JAVA类型 转换成 SQL的字段类型，如果是无法解释将会返回一个unknown 并不会异常 让业务可以继续执行下去
      tableField.setType(TypeTranslator.sqlTypeTranslate(typeStr));
      //标识已经转换成SQL 类型了
      tableField.setHasTypeTranslate(true);
    }
    //返回字段对象
    return tableField;
  }


  //主要获得Getter方法或者是字段的JPA annotation信息
  private TableField tableFieldPropertyFillIn(TableField tableField, PsiField psiField, PsiAnnotation[] annotations) {

    //一开始先忽略字段
    for (PsiAnnotation annotation : annotations) {

      //忽略Transient的
      if (Objects.equals(annotation.getQualifiedName(), "javax.persistence.Transient")) {
        return null;
      }
      //忽略OneToMany 一般都是在ManyToOne定义字段
      if (Objects.equals(annotation.getQualifiedName(), "javax.persistence.OneToMany")
          || Objects.equals(annotation.getQualifiedName(), "javax.persistence.ManyToMany")) {
        return null;
      } else if (Objects.equals(annotation.getQualifiedName(), "javax.persistence.OneToOne")) {
        //如果是OneToOne的话，分析是不是mappedBy对方对象的（这个描述很变扭）
        for (JvmAnnotationAttribute attr : annotation.getAttributes()) {
          if (attr.getAttributeName().equals("mappedBy")) {
            return null;
          }
        }
      }
      //获得JoinColumn annotation 然后提取是否为空 提取字段名称等等
      if (Objects.equals(annotation.getQualifiedName(), "javax.persistence.JoinColumn")) {
        for (JvmAnnotationAttribute attr : annotation.getAttributes()) {
          if (attr.getAttributeName().equals("name")) {
            tableField.setName(getAttrTxtValue(attr));
          } else if (attr.getAttributeName().equals("nullable")) {
            tableField.setNullable(!getAttrTxtValue(attr).equals("false"));
          }
        }
        //提取MapperField 主要是映射方的ID字段信息 然后并将映射对象的类型和类型长度设置到这个外检字段里面去
        TableField mapperField = getTargetIdField(psiField);
        tableField.setType(mapperField.getType());
        tableField.setHasTypeTranslate(mapperField.isHasTypeTranslate());
        tableField.setLength(mapperField.getLength());
        return tableField;
      } else if (Objects.equals(annotation.getQualifiedName(), "javax.persistence.Column")) {
        //分析普通字段 获得字段名
        for (JvmAnnotationAttribute attr : annotation.getAttributes()) {
          if (attr.getAttributeName().equals("name")) {
            tableField.setName(getAttrTxtValue(attr));
            //获得字段长度
          } else if (attr.getAttributeName().equals("length")) {
            tableField.setLength(Integer.valueOf(getAttrTxtValue(attr)));
            //是否为空
          } else if (attr.getAttributeName().equals("nullable")) {
            tableField.setNullable(!getAttrTxtValue(attr).equals("false"));
          }
        }
      }
      //获得是否为主键
      if (annotation.getQualifiedName().equals("javax.persistence.Id")) {
        tableField.setPrimaryKey(true);
      }
      //获得是否自动生成主键
      if (annotation.getQualifiedName().equals("javax.persistence.GeneratedValue")) {
        tableField.setGeneratedValue(true);
      }
    }
    return tableField;
  }


  /**
   * ManyToOne或者OneToOne 就去获得映射的类ID字段信息
   *
   * @param psiField
   * @return
   */
  protected TableField getTargetIdField(PsiField psiField) {
    //获得映射类
    PsiClass psiClass = JavaPsiFacade.getInstance(psiField.getProject())
      .findClass(psiField.getType().getCanonicalText(), GlobalSearchScope.projectScope(psiField.getProject()));
    if (psiClass == null) {
      return null;
    }
    //寻找ID字段的路上
    for (PsiField targetField : psiClass.getFields()) {
      for (PsiAnnotation annotation : targetField.getAnnotations()) {
        if (Objects.equals(annotation.getQualifiedName(), "javax.persistence.Id")) {
          return this.getTableField(targetField);
        }
      }
    }

    //字段找不到就找getter方法
    PsiMethod[] psiMethodArray = psiClass.getMethods();
    for (PsiMethod method : psiMethodArray) {
      if (Convertor.isFieldGetter(method.getName())) {
        continue;
      }
      for (PsiAnnotation annotation : method.getAnnotations()) {
        if (Objects.equals(annotation.getQualifiedName(), "javax.persistence.Id")) {
          String fieldName = Convertor.getGetterFieldName(method.getName());
          PsiField targetField = psiClass.findFieldByName(fieldName, true);
          if (targetField != null) {
            return this.getTableField(targetField);
          }
        }
      }
    }
    //都找不到就没有办法了
    return null;
  }


  /**
   * 文档注释整理
   *
   * @param docStr
   * @return
   */
  protected String clearDesc(String docStr) {
    if (docStr != null && !docStr.isEmpty()) {
      docStr = docStr.replace("/**", "");
      docStr = docStr.replace("*/", "");
      docStr = docStr.replace("*", "");
      docStr = docStr.replace("\n", "");
      docStr = docStr.trim();
    }
    return docStr;
  }


  /**
   * 获得annotation参数
   *
   * @param attr
   * @return
   */
  protected String getAttrTxtValue(JvmAnnotationAttribute attr) {
    String txt = attr.toString();
    if (txt.contains("\"")) {
      txt = txt.substring(1, txt.length() - 1);
    }
    if (!txt.isEmpty()) {
      txt = txt.trim();
    }
    return txt;
  }


  /**
   * 获得表名称
   *
   * @param psiClass
   * @return
   */
  protected String getTableName(PsiClass psiClass) {
    String tableName = psiClass.getName();
    if (tableName == null) {
      return "Unknown";
    }
    String firstLetter = tableName.substring(0, 1).toLowerCase();
    tableName = firstLetter + tableName.substring(1);
    tableName = Convertor.camel2Underline(tableName);
    for (PsiAnnotation psiAnnotation : psiClass.getAnnotations()) {
      if (Objects.equals(psiAnnotation.getQualifiedName(), "javax.persistence.Table")) {
        for (JvmAnnotationAttribute attribute : psiAnnotation.getAttributes()) {
          if (attribute.getAttributeName().equals("name")) {
            tableName = getAttrTxtValue(attribute);
          }
        }
      }
    }
    return tableName;
  }

  @Override
  public void update(@NotNull AnActionEvent e) {
    super.update(e);
    // 获取当前编辑的文件, 通过PsiFile可获得PsiClass, PsiField等对象
    PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);

    // 获取当前的project对象
    Project project = e.getProject();

    // 获取数据上下文
    DataContext dataContext = e.getDataContext();

    Editor editor = e.getData(PlatformDataKeys.EDITOR);

    //如果没有打开编辑框就不显示
    if (editor == null) {
      e.getPresentation().setEnabled(false);
      return;
    }

    //如果当前打开的文件不是JAVA文件也不用显示
    if (!(psiFile instanceof PsiJavaFile)) {
      e.getPresentation().setEnabled(false);
      return;
    }

    //是否存在@JPA Entity标签 如果没有不显示按钮
    PsiJavaFile javaFile = (PsiJavaFile) psiFile;
    PsiClass[] psiClasses = javaFile.getClasses();
    boolean hasEntityAnnotation = false;
    for (PsiClass pClass : psiClasses) {
      for (PsiAnnotation psiAnnotation : pClass.getAnnotations()) {
        if (Objects.equals(psiAnnotation.getQualifiedName(), "javax.persistence.Entity")) {
          hasEntityAnnotation = true;
        }
      }
    }
    if (!hasEntityAnnotation) {
      e.getPresentation().setEnabled(false);
    }
  }
}
