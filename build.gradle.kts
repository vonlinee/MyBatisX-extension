subprojects {

  // 统一编码
  tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"

    options.compilerArgs = listOf(
      "--add-exports",
      "jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED",
    )
  }
}
