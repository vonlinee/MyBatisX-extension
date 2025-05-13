import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.nio.charset.StandardCharsets

buildscript {
  repositories {
    mavenLocal()
    maven { url = uri("https://maven.aliyun.com/repository/public/") }
    mavenCentral()
    maven { url = uri("https://plugins.gradle.org/m2/") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/releases/") }
    maven { url = uri("https://dl.bintray.com/jetbrains/intellij-plugin-service") }
    maven { url = uri("https://dl.bintray.com/jetbrains/intellij-third-party-dependencies/") }
  }
  dependencies {
    classpath("org.jetbrains.intellij.plugins:gradle-intellij-plugin:1.17.3")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20")
  }
}

plugins {
  java
  id("org.jetbrains.kotlin.jvm") version "1.9.20"
  id("org.jetbrains.intellij") version "1.14.0"
}

repositories {
  maven { url = uri("https://maven.aliyun.com/repository/public/") }
  mavenCentral()
}

// 设置兼容性版本
java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
  implementation("com.softwareloop:mybatis-generator-lombok-plugin:1.0")
  implementation("uk.com.robust-it:cloning:1.9.2")
  implementation("org.mybatis.generator:mybatis-generator-core:1.4.0")
  implementation("org.mybatis:mybatis:3.5.16")
  implementation("org.freemarker:freemarker:2.3.30")
  implementation("com.itranswarp:compiler:1.0")
  testImplementation("junit:junit:4.13.1")
  testImplementation("commons-io:commons-io:2.8.0")
  compileOnly("org.projectlombok:lombok:1.18.0")

  testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

  implementation("org.apache.commons:commons-lang3:3.11")
  implementation("com.google.guava:guava:33.2.1-jre")
  implementation("org.springframework:spring-web:5.2.12.RELEASE")
  implementation("org.apache.httpcomponents:httpclient:4.5.7")
  implementation("com.tencentcloudapi:tencentcloud-sdk-java:3.1.210")

  // 解决 lombok 不生效问题
  // 还需在Build,Execution,Deployment -> Compiler -> Annotation Processor开启注解处理器
  annotationProcessor("org.projectlombok:lombok:1.18.32")

  implementation(project(":agent-api"))
  implementation(project(":mybatisx-agent"))
  implementation(files("${rootDir}/tools.jar"))
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
  version = "2021.3.3"
  type = "IU"

  // sandboxDir = "${rootDir}/idea-sandbox-${version}"
  updateSinceUntilBuild = false

  // Bundled plugin dependencies
  plugins = listOf(
    "com.intellij.spring.boot",
    "com.intellij.java",
    "org.intellij.intelliLang",
    "com.intellij.spring",
    "com.intellij.database",
    "org.jetbrains.kotlin"
  )
}

tasks.patchPluginXml {
  sinceBuild = "203"
  // 包含未来所有版本分支
  untilBuild = ""
  changeNotes = """
    <b>初始版本</b>
    """
}

tasks.withType<KotlinCompile> {
  /**
   * Kotlin compiler options
   */
  kotlinOptions {
    // This option specifies the target version of the generated JVM bytecode
    jvmTarget = "11"
  }
}

tasks.withType<JavaCompile> {
  options.encoding = StandardCharsets.UTF_8.name()
}
