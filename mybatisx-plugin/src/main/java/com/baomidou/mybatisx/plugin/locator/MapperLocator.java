package com.baomidou.mybatisx.plugin.locator;

import com.baomidou.mybatisx.util.JavaUtils;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.Nullable;

/**
 * The type Mapper locator.
 *
 * @author yanglin
 */
public class MapperLocator {

  /**
   * The constant dfltLocateStrategy.
   */
  public static LocateStrategy dfltLocateStrategy = new PackageLocateStrategy();

  /**
   * Process boolean.
   *
   * @param method the method
   * @return the boolean
   */
  public boolean process(@Nullable PsiMethod method) {
    return null != method && process(method.getContainingClass());
  }

  /**
   * Process boolean.
   *
   * @param clazz the clazz
   * @return the boolean
   */
  public boolean process(@Nullable PsiClass clazz) {
    return null != clazz && JavaUtils.isElementWithinInterface(clazz) && dfltLocateStrategy.apply(clazz);
  }

}
