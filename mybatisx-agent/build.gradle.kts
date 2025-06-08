import javax.xml.parsers.DocumentBuilderFactory

plugins {
  `java-library`
}

repositories {
  mavenCentral()
  mavenLocal()
}

val mybatisVersion = "3.6.0-SNAPSHOT"

dependencies {

  // https://mvnrepository.com/artifact/org.jetbrains/annotations
  compileOnly("org.jetbrains:annotations:24.1.0")

  // local pack for this plugin
  // implementation("org.mybatis:mybatis3-embed-plugin-support:${mybatisVersion}")

  testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:2.3.0")

  // https://mvnrepository.com/artifact/org.javassist/javassist
  implementation(project(":agent-api"))

  implementation(files("${rootDir}/libs/mybatis-3.6.0-SNAPSHOT.jar"))
}

// 设置兼容性版本

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.jar {
  dependsOn(":agent-api:jar")

  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
  archiveFileName = "mybatisx-agent.jar"

  manifest {
    attributes["Agent-Class"] = "org.mybatisx.extension.agent.Agent"
    attributes["Can-Redefine-Classes"] = true
    attributes["Can-Retransform-Classes"] = true
    attributes["Manifest-Version"] = 1.0
  }

  from(configurations.runtimeClasspath.get().map { file ->
    if (file.isDirectory) {
      file
    } else {
      project.zipTree(file)
    }
  })

  doLast {
    val jarFile = outputs.files.files.first()

    val targetFile = file("${rootDir}/mybatisx-plugin/src/main/resources/${jarFile.name}")
    if (targetFile.exists())
      targetFile.delete()

    project.copy {
      from(jarFile.absolutePath)
      into("${rootDir}/mybatisx-plugin/src/main/resources")
    }
  }
}

tasks.test {
  useJUnitPlatform()
}
