plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("slf4j", "2.0.12")
            library("slf4j-api", "org.slf4j", "slf4j-api").versionRef("slf4j")
            library("slf4j-simple", "org.slf4j", "slf4j-simple").versionRef("slf4j")
            library("slf4j-jdk14", "org.slf4j", "slf4j-jdk14").versionRef("slf4j")
            library("logback-classic", "ch.qos.logback:logback-classic:1.5.4")

            library("webjars.locator", "org.webjars:webjars-locator:0.52")
            library("webjars.locator.core", "org.webjars:webjars-locator-core:0.58")
            library("webjars.url-polyfill", "org.webjars.npm:url-polyfill:1.1.12")
            library("webjars.sass", "org.webjars.npm:sass:1.71.1")
            library("webjars.postcss", "org.webjars.npm:postcss:8.4.38")
            library("webjars.autoprefixer", "org.webjars.npm:autoprefixer:10.4.19")
            library("webjars.bootstrap", "org.webjars.npm:bootstrap:5.3.3")

            version("javet", "3.1.0")
            library("javet", "com.caoccao.javet", "javet").versionRef("javet")
            library("javet-linux-arm64", "com.caoccao.javet", "javet-linux-arm64").versionRef("javet")
            library("javet-macos", "com.caoccao.javet", "javet-macos").versionRef("javet")
            library("javet-android-node", "com.caoccao.javet", "javet-android-node").versionRef("javet")
            library("javet-android-v8", "com.caoccao.javet", "javet-android-v8").versionRef("javet")
            library("javenode", "com.caoccao.javet:javenode:0.6.0")

            library("jackson-bom", "com.fasterxml.jackson:jackson-bom:2.17.0")
            library("jackson-core", "com.fasterxml.jackson.core", "jackson-core").withoutVersion()
            library("jackson-databind", "com.fasterxml.jackson.core", "jackson-databind").withoutVersion()

            version("junit", "5.10.1")
            library("junit-jupiter-api", "org.junit.jupiter", "junit-jupiter-api").versionRef("junit")
            library("junit-jupiter-params", "org.junit.jupiter", "junit-jupiter-params").versionRef("junit")
            library("junit-jupiter-engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")

            library("guava", "com.google.guava:guava:33.2.0-jre")
            library("hamcrest-library", "org.hamcrest:hamcrest-library:1.3")
            library("jetbrains.annotations", "org.jetbrains:annotations:24.1.0")
            library("reflections", "org.reflections:reflections:0.10.2")
        }
    }
}

rootProject.name = "jsass-bundle"
include("jsass")
include("jsass.jackson")
include("jsass.javet")
include("jsass.javet.node.compiler")
include("jsass.javet.node-modules-resolver")
include("jsass.javet.v8.compiler")
include("jsass.javet.webjar-module-resolver")
include("jsass.webjar-importer")
