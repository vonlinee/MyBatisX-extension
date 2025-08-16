import java.nio.charset.StandardCharsets

subprojects {

  tasks.withType<JavaCompile> {
    options.encoding = StandardCharsets.UTF_8.name()

    options.compilerArgs = listOf(
      "--add-exports",
      "jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED",
    )
  }
}
