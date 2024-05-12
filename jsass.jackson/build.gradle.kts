plugins {
    id("jsass.java-conventions")
    id("jsass.publication-conventions")
}

description = "Jsass JSON implementation with Jackson."

dependencies {
    implementation(project(":jsass"))
    implementation(platform(libs.jackson.bom))
    implementation(libs.jackson.core)
    implementation(libs.jackson.databind)
}
