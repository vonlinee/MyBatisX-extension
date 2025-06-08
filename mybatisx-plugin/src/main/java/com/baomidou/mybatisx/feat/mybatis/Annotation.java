package com.baomidou.mybatisx.feat.mybatis;

import com.baomidou.mybatisx.util.CollectionUtils;
import com.baomidou.mybatisx.util.PsiUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * <p>
 * Mybatis 相关注解定义
 * </p>
 *
 * @author yanglin
 * @since 2018 -07-30
 */
public class Annotation implements Cloneable {

  /**
   * The constant PARAM.
   */
  public static final Annotation PARAM = new Annotation("@Param", "org.apache.ibatis.annotations.Param");

  /**
   * The constant SELECT.
   */
  public static final Annotation SELECT = new Annotation("@Select", "org.apache.ibatis.annotations.Select");

  /**
   * The constant UPDATE.
   */
  public static final Annotation UPDATE = new Annotation("@Update", "org.apache.ibatis.annotations.Update");

  /**
   * The constant INSERT.
   */
  public static final Annotation INSERT = new Annotation("@Insert", "org.apache.ibatis.annotations.Insert");

  /**
   * The constant DELETE.
   */
  public static final Annotation DELETE = new Annotation("@Delete", "org.apache.ibatis.annotations.Delete");

  /**
   * The constant ALIAS.
   */
  public static final Annotation ALIAS = new Annotation("@Alias", "org.apache.ibatis.type.Alias");

  /**
   * The constant AUTOWIRED.
   */
  public static final Annotation AUTOWIRED = new Annotation("@Autowired", "org.springframework.beans.factory.annotation.Autowired");

  /**
   * The constant RESOURCE.
   */
  public static final Annotation RESOURCE = new Annotation("@Resource", "javax.annotation.Resource");

  /**
   * The constant STATEMENT_SYMMETRIES.
   */
  public static final Set<Annotation> STATEMENT_SYMMETRIES = Set.of(SELECT, UPDATE, INSERT, DELETE);

  private final String label;

  private final String qualifiedName;

  private Map<String, AnnotationValue> attributePairs;

  /**
   * Instantiates a new Annotation.
   *
   * @param label         the label
   * @param qualifiedName the qualified name
   */
  public Annotation(@NotNull String label, @NotNull String qualifiedName) {
    this.label = label;
    this.qualifiedName = qualifiedName;
    attributePairs = new HashMap<>();
  }

  private Annotation addAttribute(String key, AnnotationValue value) {
    this.attributePairs.put(key, value);
    return this;
  }

  /**
   * With attribute annotation.
   *
   * @param key   the key
   * @param value the value
   * @return the annotation
   */
  public Annotation withAttribute(@NotNull String key, @NotNull AnnotationValue value) throws CloneNotSupportedException {
    Annotation copy = this.clone();
    copy.attributePairs = new HashMap<>(this.attributePairs);
    return copy.addAttribute(key, value);
  }

  /**
   * With value annotation.
   *
   * @param value the value
   * @return the annotation
   */
  public Annotation withValue(@NotNull AnnotationValue value) throws CloneNotSupportedException {
    return withAttribute("value", value);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(label);
    if (!CollectionUtils.isEmpty(attributePairs.entrySet())) {
      builder.append(setupAttributeText());
    }
    return builder.toString();
  }

  private String setupAttributeText() {
    Optional<String> singleValue = getSingleValue();
    return singleValue.orElseGet(this::getComplexValue);
  }

  private String getComplexValue() {
    StringBuilder builder = new StringBuilder("(");
    for (String key : attributePairs.keySet()) {
      builder.append(key);
      builder.append(" = ");
      builder.append(attributePairs.get(key).toString());
      builder.append(", ");
    }
    builder.deleteCharAt(builder.length() - 1);
    builder.deleteCharAt(builder.length() - 1);
    builder.append(")");
    return builder.toString();
  }

  /**
   * To psi class optional.
   *
   * @param project the project
   * @return the optional
   */
  public Optional<PsiClass> toPsiClass(@NotNull Project project) {
    return PsiUtils.findPsiClassGlobally(project, getQualifiedName());
  }

  private Optional<String> getSingleValue() {
    try {
      String value = CollectionUtils.getOnlyElement(attributePairs.keySet());
      String builder = "(" + attributePairs.get(value).toString() + ")";
      return Optional.of(builder);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  /**
   * Gets label.
   *
   * @return the label
   */
  @NotNull
  public String getLabel() {
    return label;
  }

  /**
   * Gets qualified name.
   *
   * @return the qualified name
   */
  @NotNull
  public String getQualifiedName() {
    return qualifiedName;
  }

  @Override
  protected Annotation clone() throws CloneNotSupportedException {
    try {
      return (Annotation) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new IllegalStateException();
    }
  }

  /**
   * The interface Annotation value.
   */
  public interface AnnotationValue {
  }

  /**
   * The type String value.
   */
  public static class StringValue implements AnnotationValue {

    private final String value;

    /**
     * Instantiates a new String value.
     *
     * @param value the value
     */
    public StringValue(@NotNull String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return "\"" + value + "\"";
    }
  }
}
