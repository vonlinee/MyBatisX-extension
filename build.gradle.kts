subprojects {

    // 统一编码
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
