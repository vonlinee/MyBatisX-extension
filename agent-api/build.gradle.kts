import java.nio.charset.StandardCharsets

plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
  options.encoding = StandardCharsets.UTF_8.name()

  sourceCompatibility = "17"
  targetCompatibility = "17"
}
