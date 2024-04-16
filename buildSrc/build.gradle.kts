plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.github.ben-manes:gradle-versions-plugin:0.51.0")
    implementation("net.researchgate:gradle-release:3.0.2")
    implementation("org.ajoberstar.grgit:grgit-gradle:5.2.1")
    implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:5.0.0.4638")
    implementation("io.freefair.gradle:lombok-plugin:8.6")
}
