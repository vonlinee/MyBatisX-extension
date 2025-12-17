plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}
