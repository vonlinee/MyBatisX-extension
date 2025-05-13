plugins {
  id("java")
}

repositories {
  mavenCentral()
}

dependencies {

  compileOnly("org.projectlombok:lombok:1.18.0")

  testImplementation(platform("org.junit:junit-bom:5.9.1"))
  testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
  useJUnitPlatform()
}
