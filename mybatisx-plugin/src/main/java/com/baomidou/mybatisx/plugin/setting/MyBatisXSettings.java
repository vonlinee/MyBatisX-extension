package com.baomidou.mybatisx.plugin.setting;

import com.baomidou.mybatisx.plugin.setting.config.AbstractStatementGenerator;
import com.baomidou.mybatisx.util.MyBatisXPlugin;
import com.google.common.base.Joiner;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Mybatis setting.
 * 这里是全局配置, 所以配置文件在目录($APP_CONFIG$)下
 * name：持久化文件组件名称可随意指定，通常用插件名称即可
 * storages ：定义配置参数的持久化位置
 * 其中$APP_CONFIG$变量为Idea安装后默认的用户路径，
 *
 * @author yanglin
 */
@Service
@State(
        name = "MybatisXSettings",
        storages = @Storage(value = MyBatisXPlugin.PERSISTENT_STATE_FILE))
public final class MyBatisXSettings implements PersistentStateComponent<MyBatisXSettings.State> {

    private final State state = new State();

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static MyBatisXSettings getInstance() {
        return ServiceManager.getService(MyBatisXSettings.class);
    }

    @NotNull
    @Override
    public MyBatisXSettings.State getState() {
        return this.state;
    }

    @Override
    public void loadState(@NotNull MyBatisXSettings.State state) {
        XmlSerializerUtil.copyBean(state, this.state);
    }

    public String getMapperIcon() {
        return state.mapperIcon;
    }

    public void setMapperIcon(String mapperIcon) {
        this.state.mapperIcon = mapperIcon;
    }

    public String getInsertGenerator() {
        return state.insertGenerator;
    }

    public void setInsertGenerator(String insertGenerator) {
        this.state.insertGenerator = insertGenerator;
    }

    public String getUpdateGenerator() {
        return state.updateGenerator;
    }

    public void setUpdateGenerator(String updateGenerator) {
        this.state.updateGenerator = updateGenerator;
    }

    public String getDeleteGenerator() {
        return state.deleteGenerator;
    }

    public void setDeleteGenerator(String deleteGenerator) {
        this.state.deleteGenerator = deleteGenerator;
    }

    public String getSelectGenerator() {
        return state.selectGenerator;
    }

    public void setSelectGenerator(String selectGenerator) {
        this.state.selectGenerator = selectGenerator;
    }

    /**
     * 配置值, 字段需要是public
     * <a href="https://plugins.jetbrains.com/docs/intellij/persisting-state-of-components.html">...</a>
     */
    public static class State implements Serializable {

        public String mapperIcon;
        public String insertGenerator;
        public String updateGenerator;
        public String deleteGenerator;
        public String selectGenerator;

        /**
         * 数据类型映射
         */
        public Map<String, Map<String, String>> dataTypeMapping = new HashMap<>();

        public State() {
            // 配置的默认值
            Joiner joiner = Joiner.on(";");
            insertGenerator = joiner.join(AbstractStatementGenerator.INSERT_GENERATOR.getPatterns());
            updateGenerator = joiner.join(AbstractStatementGenerator.UPDATE_GENERATOR.getPatterns());
            deleteGenerator = joiner.join(AbstractStatementGenerator.DELETE_GENERATOR.getPatterns());
            selectGenerator = joiner.join(AbstractStatementGenerator.SELECT_GENERATOR.getPatterns());
        }
    }
}
