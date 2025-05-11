package com.baomidou.mybatisx.util;

import com.baomidou.mybatisx.feat.bean.Field;
import com.baomidou.mybatisx.model.TranslationVO;
import com.baomidou.mybatisx.service.BaiduTranslationService;
import com.baomidou.mybatisx.service.TencentTranslationService;
import com.baomidou.mybatisx.service.Translation;
import com.baomidou.mybatisx.plugin.setting.OtherSetting;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baomidou.mybatisx.feat.ddl.TranslationAppEnum.BAIDU;
import static com.baomidou.mybatisx.feat.ddl.TranslationAppEnum.TENCENT;

/**
 * @author breezes_y@163.com
 * @date 2021/2/5 14:27
 * @description
 */
public class TranslationUtil {

  public static Translation translationInit(OtherSetting.State properties) {
    if (StringUtils.equals(BAIDU.getValue(), properties.getTranslationAppComboBox())) {
      return new BaiduTranslationService(properties.getAppIdText(), properties.getSecretText());
    }
    if (StringUtils.equals(TENCENT.getValue(), properties.getTranslationAppComboBox())) {
      return new TencentTranslationService(properties.getSecretId(), properties.getSecretKey());
    }
    return null;
  }

  public static Map<String, String> enToZh(List<Field> fieldList, String tableName) {
    tableName = tableName.replace("_", " ");
    OtherSetting.State properties = OtherSetting.getInstance().myProperties;
    String translationApp = properties.getTranslationAppComboBox();

    Map<String, String> dataMap = new HashMap<>();
    Translation translation = translationInit(properties);
    if (null == translation) {
      return dataMap;
    }

    String englishText = "";
    // 百度翻译
    if (StringUtils.equals(BAIDU.getValue(), translationApp)) {
      List<String> commendList = new ArrayList<>();
      commendList.add(tableName);
      for (Field field : fieldList) {
        commendList.add(field.getTableColumn().replace("_", " "));
      }
      englishText = String.join("\n", commendList);
    }
    // 腾讯翻译
    if (StringUtils.equals(TENCENT.getValue(), translationApp)) {
      StringBuilder text = new StringBuilder("[" + tableName + "]");
      for (Field field : fieldList) {
        text.append("[").append(field.getTableColumn().replace("_", " ")).append("]");
      }
      englishText = text.toString();
    }
    List<TranslationVO> translationList = translation.toChinese(englishText);
    if (CollectionUtils.isEmpty(translationList)) {
      return dataMap;
    }

    for (TranslationVO translationVO : translationList) {
      dataMap.put(translationVO.getSrc(), translationVO.getDst());
    }
    return dataMap;
  }
}
