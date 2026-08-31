# 项目架构

根项目使用 Gradle，源码和目标字节码版本为 Java 17，包含三个主要模块。

1. mybatisx-plugin` 是 IntelliJ 插件主体，包含插件功能、UI、MyBatis 处理、资源和测试。
2. agent-api` 定义插件与运行时 Agent 之间共用的 API 和 DTO。
3. mybatisx-agent` 是运行时 Java Agent，依赖 `agent-api`。
4. sample` 存放 MyBatis 2/3 和多模块示例，仅作为行为参考。
5. libs 存放构建使用的本地 JAR，除非任务明确要求，否则不要修改。
插件扩展声明位于 `mybatisx-plugin/src/main/resources/META-INF/`，实现类必须与声明保持一致。

