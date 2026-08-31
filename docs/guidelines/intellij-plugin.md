# IntelliJ 插件开发

## 入口

- Plugin lifecycle defined in `plugin.xml` via `<extensions>` and `<actions>`.
- Main action classes implement `AnAction`; inspections extend `BaseLocalInspectionTool`.

## 规则

- 修改 `plugin.xml` 或其他 `META-INF/*.xml` 时，同时检查扩展声明、可选依赖和实现类是否匹配。
- PSI、DOM、检查器、意图、导航、编辑器和 MyBatis XML 行为的改动，应补充或更新 `mybatisx-plugin/src/test` 下的聚焦测试。
- UI 和资源文件属于运行时代码，保持图标、主题变体、模板、消息键和资源路径一致。

## IntelliJ Platform Plugin 开发指导文档

参考：https://plugins.jetbrains.com/docs/intellij/welcome.html
