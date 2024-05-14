plugins {
    id("jsass.java-conventions")
    id("jsass.publication-conventions")
}

description = "Javet module resolver for node_modules."

dependencies {
    implementation(project(":jsass"))
    implementation(libs.slf4j.api)
    implementation(libs.javet)
    implementation(libs.jetbrains.annotations)
}
