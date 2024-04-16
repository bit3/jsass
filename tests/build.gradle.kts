plugins {
    id("jsass.java-conventions")
}

description = "Javet V8 Compiler for Jsass."

dependencies {
    implementation(project(":jsass"))
    implementation(project(":jsass.javet"))
    implementation(project(":jsass.javet.v8.compiler"))
    implementation(project(":jsass.javet.v8.webjar-module-resolver"))

    implementation(libs.javet)
    runtimeOnly(libs.webjars.sass)

    testRuntimeOnly(libs.logback.classic)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.junit.jupiter.engine)
    testImplementation(libs.hamcrest.library)
}
