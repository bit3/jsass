plugins {
    id("jsass.java-conventions")
    id("jsass.publication-conventions")
}

description = "Jsass compiler implementation with Javet Node.js Mode."

dependencies {
    implementation(project(":jsass"))
    implementation(project(":jsass.javet"))
    implementation(libs.javet) // Linux and Windows (x86_64)
//    implementation(libs.javet.linux.arm64) // Linux (arm64)
//    implementation(libs.javet.macos) // Mac OS (x86_64 and arm64)
//    implementation(libs.javet.android.node) // Android Node (arm, arm64, x86 and x86_64)
//    implementation(libs.javet.android.v8) // Android V8 (arm, arm64, x86 and x86_64)
    implementation(libs.jetbrains.annotations)
}
