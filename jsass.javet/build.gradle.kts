plugins {
    id("jsass.java-conventions")
    id("jsass.publication-conventions")
    id("org.sonarqube")
}

description = "Jsass compiler implementation using Javet."

dependencies {
    implementation(project(":jsass"))
    implementation(libs.slf4j.api)
    implementation(libs.javet) // Linux and Windows (x86_64)
//    implementation(libs.javet.linux.arm64) // Linux (arm64)
//    implementation(libs.javet.macos) // Mac OS (x86_64 and arm64)
//    implementation(libs.javet.android.node) // Android Node (arm, arm64, x86 and x86_64)
//    implementation(libs.javet.android.v8) // Android V8 (arm, arm64, x86 and x86_64)
    implementation(libs.jetbrains.annotations)
}
