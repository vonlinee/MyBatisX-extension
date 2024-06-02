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
    kotlin("jvm") version "1.9.20"
    id("org.jetbrains.intellij") version "1.14.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    jvmToolchain(8)  // 生成的字节码的目标版本
}

// https://github.com/JetBrains/gradle-intellij-plugin/
// http://www.jetbrains.org/intellij/sdk/docs/tutorials/build_system/prerequisites.html
intellij {
    version = "2020.2"
    type = "IU" // 企业版 type="IC"  // 社区版

    // Bundled plugin dependencies
    plugins.set(
        listOf(
            "java",
            "Kotlin",
            "IntelliLang",
            "Spring",
            "SpringBoot",
            "DatabaseTools"
        )
    )

    pluginName = "MybatisX"
    sandboxDir = "${rootProject.rootDir}/idea-sandbox"
    updateSinceUntilBuild = false
    downloadSources = true
}

// 各种版本去这里找
// https://www.jetbrains.com/intellij-repository/releases

group = "com.baomidou.plugin.idea.mybatisx"
version = "1.5.2"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("com.softwareloop:mybatis-generator-lombok-plugin:1.0")
    implementation("uk.com.robust-it:cloning:1.9.2")
    implementation("org.mybatis.generator:mybatis-generator-core:1.4.0")
    implementation("org.mybatis:mybatis:3.5.16")
    implementation("org.freemarker:freemarker:2.3.30")
    implementation("com.itranswarp:compiler:1.0")
    testImplementation("junit:junit:4.13.1")
    testImplementation("commons-io:commons-io:2.8.0")
    compileOnly("org.projectlombok:lombok:1.18.0")

    implementation(files("libs/tools.jar"))

    // 解决 lombok 不生效问题
    // 还需在Build,Execution,Deployment -> Compiler -> Annotation Processor开启注解处理器
    annotationProcessor("org.projectlombok:lombok:1.18.32")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.test {
    // 跳过单元测试任务
    enabled = false
}
