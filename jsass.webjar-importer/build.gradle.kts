plugins {
    id("jsass.java-conventions")
    id("jsass.publication-conventions")
    id("org.sonarqube")
}

description = "Jsass importer for Webjars."

dependencies {
    implementation(project(":jsass"))
    implementation(libs.slf4j.api)
    implementation(libs.webjars.locator.core)
    implementation(libs.jetbrains.annotations)
}
