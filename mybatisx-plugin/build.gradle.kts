import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.nio.charset.StandardCharsets

buildscript {
  repositories {
    mavenLocal()
    maven { url = uri("https://www.jetbrains.com/intellij-repository/releases") }
    maven { url = uri("https://maven.aliyun.com/repository/public/") }
    mavenCentral()
    maven { url = uri("https://plugins.gradle.org/m2/") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/releases/") }
    maven { url = uri("https://dl.bintray.com/jetbrains/intellij-plugin-service") }
    maven { url = uri("https://dl.bintray.com/jetbrains/intellij-third-party-dependencies/") }
  }
  dependencies {
    classpath("org.jetbrains.intellij.plugins:gradle-intellij-plugin:1.17.3")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.0")
  }
}

plugins {
  java
  id("org.jetbrains.kotlin.jvm") version "2.2.0"
  id("org.jetbrains.intellij.platform") version "2.9.0"
  id("org.jetbrains.intellij.platform.module") version "2.9.0"
}

repositories {
  mavenLocal()
  maven { url = uri("https://maven.aliyun.com/repository/public/") }
  mavenCentral()
  intellijPlatform {
    defaultRepositories()
  }
}

intellijPlatform {

  dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  }
}

dependencies {
  compileOnly("org.projectlombok:lombok:1.18.32")

  // 解决 lombok 不生效问题
  // 还需在Build,Execution,Deployment -> Compiler -> Annotation Processor开启注解处理器
  annotationProcessor("org.projectlombok:lombok:1.18.32")

  implementation("org.mybatis.generator:mybatis-generator-core:1.4.0")
  implementation("org.freemarker:freemarker:2.3.30")
  implementation(files("${rootDir}/libs/tools.jar"))
  // this is published locally
  implementation(files("${rootDir}/libs/mybatis-3.6.0-SNAPSHOT.jar"))
  implementation("com.tencentcloudapi:tencentcloud-sdk-java:3.1.210")
  // https://mvnrepository.com/artifact/net.minidev/json-smart
  implementation("net.minidev:json-smart:2.6.0")
  implementation(project(":agent-api"))
  implementation(project(":mybatisx-agent"))

  testImplementation("junit:junit:4.13.1")
  testImplementation("commons-io:commons-io:2.14.0")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")

  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

  intellijPlatform {
    create(IntelliJPlatformType.IntellijIdeaUltimate, "2025.2.1")

    bundledPlugins(
      "com.intellij.java",
      "com.intellij.spring.boot",
      "com.intellij.spring",
      "com.intellij.database",
      "com.intellij.modules.json",
      "org.intellij.intelliLang",
      "org.jetbrains.kotlin"
    )

    testFramework(TestFrameworkType.Platform)
  }
}

tasks.patchPluginXml {
  sinceBuild = "251"
  // 包含未来所有版本分支
  untilBuild = "252.*"
  changeNotes = """
    <b>初始版本</b>
    """
}

tasks.withType<KotlinCompile> {
  /**
   * Kotlin compiler options
   */
  // This option specifies the target version of the generated JVM bytecode
  compilerOptions.jvmTarget = JvmTarget.JVM_17
}

tasks.withType<JavaCompile> {
  options.encoding = StandardCharsets.UTF_8.name()
}
