plugins {
    id("jsass.java-conventions")
    id("jsass.publication-conventions")
}

description = "Javet V8 Compiler for Jsass."

java {
    registerFeature("jackson") {
        usingSourceSet(sourceSets["main"])
    }
}

dependencies {
    implementation(libs.javet)
    implementation(libs.webjars.locator.core)
    implementation(libs.jetbrains.annotations)

    "jacksonImplementation"(libs.jackson.databind)
}
