import java.nio.file.Files
import kotlin.io.path.Path
import org.siouan.frontendgradleplugin.infrastructure.gradle.InstallFrontendTask

plugins {
    id("jsass.java-conventions")
    id("net.researchgate.release")
    id("ca.cutterslade.analyze")
    id("org.siouan.frontend-jdk11")
}

description = "The jsass project bundle."

dependencies {
    testImplementation(project(":jsass"))
    testImplementation(project(":jsass.jackson"))
    testImplementation(project(":jsass.javet"))
    testImplementation(project(":jsass.javet.node.compiler"))
    testImplementation(project(":jsass.javet.node-modules-resolver"))
    testImplementation(project(":jsass.javet.v8.compiler"))
    testImplementation(project(":jsass.javet.webjar-module-resolver"))
    testImplementation(project(":jsass.webjar-importer"))
    testImplementation(libs.javet)
    testImplementation(libs.webjars.locator.core)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    permitTestUnusedDeclared(libs.junit.jupiter.params)
    testImplementation(libs.junit.jupiter.engine)
    permitTestUnusedDeclared(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.webjars.sass)
    testRuntimeOnly(libs.webjars.postcss)
    testRuntimeOnly(libs.webjars.autoprefixer)
    testRuntimeOnly(libs.webjars.bootstrap)
    testRuntimeOnly(libs.logback.classic)
}

frontend {
    nodeVersion.set("18.20.2")
    verboseModeEnabled.set(true)
}

tasks.named<InstallFrontendTask>("installFrontend") {
    val ciPlatformPresent = providers.environmentVariable("CI").isPresent()
    val lockFilePath = "${projectDir}/yarn.lock"
    val retainedMetadataFileNames: Set<String>
    if (ciPlatformPresent) {
        retainedMetadataFileNames = setOf(lockFilePath)
    } else {
        // The naive configuration below allows to skip the task if the last successful execution did not change neither
        // the package.json file, nor the lock file, nor the node_modules directory. Any other scenario where for
        // example the lock file is regenerated will lead to another execution before the task is "up-to-date" because
        // the lock file is both an input and an output of the task.
        retainedMetadataFileNames = mutableSetOf("${projectDir}/package.json")
        if (Files.exists(Path(lockFilePath))) {
            retainedMetadataFileNames.add(lockFilePath)
        }
        outputs.file(lockFilePath).withPropertyName("lockFile")
    }
    inputs.files(retainedMetadataFileNames).withPropertyName("metadataFiles")
    outputs.dir("${projectDir}/node_modules").withPropertyName("nodeModulesDirectory")
}

sonarqube {
    properties {
        property("sonar.projectKey", "jsass_jsass")
        property("sonar.organization", "jsass")
        property("sonar.junit.reportPaths", project.layout.buildDirectory.dir("build/test-results/test"))
    }
}
