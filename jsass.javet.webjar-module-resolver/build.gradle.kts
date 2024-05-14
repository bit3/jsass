plugins {
    id("jsass.java-conventions")
    id("jsass.publication-conventions")
    id("org.sonarqube")
}

description = "Javet module resolver for webjars."

dependencies {
    implementation(project(":jsass"))
    implementation(libs.slf4j.api)
    implementation(libs.javet)
    implementation(libs.webjars.locator.core)
    implementation(libs.jetbrains.annotations)
}
