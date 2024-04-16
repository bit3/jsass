plugins {
    id("jsass.java-conventions")
    id("jsass.publication-conventions")
}

description = "The ultimate SASS compiler for Java / the JVM."

dependencies {
    implementation(libs.slf4j.api)
    implementation(libs.webjars.locator)
    implementation(libs.webjars.locator.core)
    implementation(libs.javet) // Linux and Windows (x86_64)
//    implementation(libs.javet.linux.arm64) // Linux (arm64)
//    implementation(libs.javet.macos) // Mac OS (x86_64 and arm64)
//    implementation(libs.javet.android.node) // Android Node (arm, arm64, x86 and x86_64)
//    implementation(libs.javet.android.v8) // Android V8 (arm, arm64, x86 and x86_64)
    implementation(libs.jetbrains.annotations)
    runtimeOnly(libs.webjars.sass)
    testRuntimeOnly(libs.slf4j.simple)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.junit.jupiter.engine)
    testImplementation(libs.hamcrest.library)
}
