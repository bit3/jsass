plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.versionsPlugin)
    implementation(libs.analyze)
    implementation(libs.release)
    implementation(libs.grgit)
    implementation(libs.sonarqube)
    implementation(libs.lombok)
    implementation(libs.frontend)
}
