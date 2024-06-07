package com.baomidou.plugin.idea.mybatisx.setting;

import com.baomidou.plugin.idea.mybatisx.setting.vo.TranslationSettingVO;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.baomidou.plugin.idea.mybatisx.enums.SqlTypeAndJavaTypeEnum.BIGINT;
import static com.baomidou.plugin.idea.mybatisx.enums.SqlTypeAndJavaTypeEnum.DATE;
import static com.baomidou.plugin.idea.mybatisx.enums.SqlTypeAndJavaTypeEnum.DATETIME;
import static com.baomidou.plugin.idea.mybatisx.enums.SqlTypeAndJavaTypeEnum.DECIMAL;
import static com.baomidou.plugin.idea.mybatisx.enums.SqlTypeAndJavaTypeEnum.DOUBLE;
import static com.baomidou.plugin.idea.mybatisx.enums.SqlTypeAndJavaTypeEnum.INT;
import static com.baomidou.plugin.idea.mybatisx.enums.SqlTypeAndJavaTypeEnum.TIME;
import static com.baomidou.plugin.idea.mybatisx.enums.SqlTypeAndJavaTypeEnum.TIMESTAMP;
import static com.baomidou.plugin.idea.mybatisx.enums.SqlTypeAndJavaTypeEnum.TINYINT;
import static com.baomidou.plugin.idea.mybatisx.enums.SqlTypeAndJavaTypeEnum.VARCHAR;

@State(
    name = "JavaBean2DDL.Settings",
    storages = {
        @Storage(value = "$APP_CONFIG$/javabean2ddl.settings.xml")
    }
)
public final class JavaBean2DDLSetting implements PersistentStateComponent<JavaBean2DDLSetting.MySettingProperties> {

    public MySettingProperties myProperties = new MySettingProperties();

    public static JavaBean2DDLSetting getInstance() {
        return ServiceManager.getService(JavaBean2DDLSetting.class);
    }

    @Nullable
    @Override
    public JavaBean2DDLSetting.MySettingProperties getState() {
        return myProperties;
    }

    @Override
    public void loadState(@NotNull JavaBean2DDLSetting.MySettingProperties state) {
        myProperties = state;
    }

    @Setter
    @Getter
    public static class MySettingProperties {

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
