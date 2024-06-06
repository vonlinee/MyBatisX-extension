plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    api("com.alibaba:fastjson:2.0.47")
}

tasks.test {
    useJUnitPlatform()
}
