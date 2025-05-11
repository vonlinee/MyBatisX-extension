package com.baomidou.mybatisx.plugin.setting;

import com.baomidou.mybatisx.model.TranslationSettingVO;
import com.baomidou.mybatisx.util.IntellijSDK;
import com.baomidou.mybatisx.util.MyBatisXPlugin;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.baomidou.mybatisx.feat.ddl.SqlAndJavaTypeEnum.BIGINT;
import static com.baomidou.mybatisx.feat.ddl.SqlAndJavaTypeEnum.DATE;
import static com.baomidou.mybatisx.feat.ddl.SqlAndJavaTypeEnum.DATETIME;
import static com.baomidou.mybatisx.feat.ddl.SqlAndJavaTypeEnum.DECIMAL;
import static com.baomidou.mybatisx.feat.ddl.SqlAndJavaTypeEnum.DOUBLE;
import static com.baomidou.mybatisx.feat.ddl.SqlAndJavaTypeEnum.INT;
import static com.baomidou.mybatisx.feat.ddl.SqlAndJavaTypeEnum.TIME;
import static com.baomidou.mybatisx.feat.ddl.SqlAndJavaTypeEnum.TIMESTAMP;
import static com.baomidou.mybatisx.feat.ddl.SqlAndJavaTypeEnum.TINYINT;
import static com.baomidou.mybatisx.feat.ddl.SqlAndJavaTypeEnum.VARCHAR;

@Service
@State(name = "Other", storages = @Storage(value = MyBatisXPlugin.PERSISTENT_STATE_FILE))
public final class OtherSetting implements PersistentStateComponent<OtherSetting.State> {

  public State myProperties = new State();

  public static OtherSetting getInstance() {
    return IntellijSDK.getService(OtherSetting.class);
  }

  @Nullable
  @Override
  public OtherSetting.State getState() {
    return myProperties;
  }

  @Override
  public void loadState(@NotNull OtherSetting.State state) {
    myProperties = state;
  }

  public State getProperties() {
    return myProperties;
  }

  @Setter
  @Getter
  public static class State {

    public TranslationSettingVO translationSetting;

    /**
     * 是否开启自动翻译
     */
    private Boolean autoTranslationRadio = false;
    /**
     * 翻译组件
     */
    private String translationAppComboBox = "";
    /**
     * appid
     */
    private String appIdText = "";
    /**
     * secret
     */
    private String secretText = "";
    /**
     * 腾讯云翻译secretId
     */
    private String secretId = "";
    /**
     * 腾讯云翻译secretKey
     */
    private String secretKey = "";
    /**
     * 表名使用的注解
     */
    private String tableAnnotation = "javax.persistence.Table";
    /**
     * 表名使用的注解属性
     */
    private String tableAnnotationProperty = "name";
    /**
     * id使用的注解
     */
    private String idAnnotation = "javax.persistence.Id";
    /**
     * 注释
     */
    private String commentAnnotation = "comment";

    private String intType = INT.getSqlType();

    private String longType = BIGINT.getSqlType();

    private String stringType = VARCHAR.getSqlType();

    private String booleanType = TINYINT.getSqlType();

    private String dateType = DATETIME.getSqlType();

    private String doubleType = DOUBLE.getSqlType();

    private String floatType = DOUBLE.getSqlType();

    private String bigDecimalType = DECIMAL.getSqlType();

    private String localDateType = DATE.getSqlType();

    private String localTimeType = TIME.getSqlType();

    private String localDateTimeType = TIMESTAMP.getSqlType();

    private String intDefaultLength = INT.getDefaultLength();

    private String longDefaultLength = BIGINT.getDefaultLength();

    private String stringDefaultLength = VARCHAR.getDefaultLength();

    private String doubleDefaultLength = DOUBLE.getDefaultLength();

    private String floatDefaultLength = DOUBLE.getDefaultLength();

    private String booleanDefaultLength = TINYINT.getDefaultLength();

    private String dateDefaultLength = DATETIME.getDefaultLength();

    private String bigDecimalDefaultLength = DECIMAL.getDefaultLength();

    private String localDateDefaultLength = DATE.getDefaultLength();

    private String localTimeDefaultLength = TIME.getDefaultLength();

    private String localDateTimeDefaultLength = TIMESTAMP.getDefaultLength();

    private Boolean showNoInMapFieldRadio = false;
  }
}
