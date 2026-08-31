# 构建与测试

所有操作均使用 Gradle Wrapper(在 Unix 上为 `./gradlew`, 在 Windows 上为 `gradlew.bat`)执行.

```bash
# Clean build (full) - clean and rebuild everything
./gradlew clean build

# Compile and run tests (without building)
./gradlew test

# Run plugin in a sandbox IDE
./gradlew runIde

# Build plugin ZIP for distribution
./gradlew buildPlugin

# Verify plugin compatibility
./gradlew verifyPlugin

# Run a single test class
./gradlew test --tests "com.example.MyTest"

# Run a specific test method
./gradlew test --tests "com.example.MyTest.myMethod"

# List all available tasks
./gradlew tasks

# Update IntelliJ platform version - first edit 'platformVersion' in gradle.properties, then rebuild
./gradlew build
```
