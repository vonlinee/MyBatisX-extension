package com.baomidou.mybatisx.plugin;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.function.Supplier;

public class LocalizedMessages {
  @NonNls
  private static final String PATH_TO_BUNDLE = "messages.messages";
  private static final DynamicBundle INSTANCE =
    new DynamicBundle(PATH_TO_BUNDLE);

  private LocalizedMessages() {
  }

  public static @NotNull @Nls String getMessage(
    @NotNull @PropertyKey(resourceBundle = PATH_TO_BUNDLE) String key,
    Object @NotNull ... params
  ) {
    return INSTANCE.getMessage(key, params);
  }

  public static Supplier<@Nls String> getLazyMessage(
    @NotNull @PropertyKey(resourceBundle = PATH_TO_BUNDLE) String key,
    Object @NotNull ... params
  ) {
    return INSTANCE.getLazyMessage(key, params);
  }
}
