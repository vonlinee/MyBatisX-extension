import java.nio.charset.StandardCharsets

plugins {
  java
}

subprojects {

  tasks.withType<JavaCompile> {
    options.encoding = StandardCharsets.UTF_8.name()

    options.compilerArgs = listOf(
      "--add-exports",
      "jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED",
    )
  }

}

java {
  sourceCompatibility = JavaVersion.VERSION_17 // 源码兼容版本
  targetCompatibility = JavaVersion.VERSION_17 // 目标字节码版本（通常与源码版本一致）
}
