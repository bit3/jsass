plugins {
    id("jsass.java-conventions")
    id("jsass.publication-conventions")
    id("org.sonarqube")
}

description = "The ultimate SASS compiler for Java / the JVM."

dependencies {
    implementation(libs.jetbrains.annotations)
}
