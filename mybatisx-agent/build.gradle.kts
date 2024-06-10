plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {

    compileOnly("org.mybatis.spring.boot:mybatis-spring-boot-starter:2.3.0")

    // https://mvnrepository.com/artifact/org.mybatis/mybatis
    // mybatis 内部提供了 javassist 库
    compileOnly("org.mybatis:mybatis:3.5.16")
    // https://mvnrepository.com/artifact/org.jetbrains/annotations
    compileOnly("org.jetbrains:annotations:24.1.0")

    testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:2.3.0")

    // https://mvnrepository.com/artifact/org.javassist/javassist
    implementation(project(":agent-api"))
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
