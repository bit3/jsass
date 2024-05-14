import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import java.nio.file.Paths

plugins {
    id("java")
    id("jacoco")
    id("com.github.ben-manes.versions")
    id("org.ajoberstar.grgit")
    id("org.sonarqube")
    id("io.freefair.lombok")
    id("ca.cutterslade.analyze")
}

group = "io.bit3"

java {
    withJavadocJar()
    withSourcesJar()

    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

tasks.compileJava {
    options.compilerArgs.add("-parameters")
}

tasks.jar {
    manifest {
        attributes(
                "Implementation-Title" to project.name,
                "Implementation-Version" to version
        )
    }
}

repositories {
    mavenCentral()
}

dependencyLocking {
    lockAllConfigurations()
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}

tasks.test {
    useJUnitPlatform()

    systemProperty("org.slf4j.simpleLogger.log.io.bit3.jsass", "trace")

    if (project.hasProperty("test.tmpdir")) {
        val path = Paths.get(project.findProperty("test.tmpdir").toString())
        systemProperty("java.io.tmpdir", path.toAbsolutePath().normalize().toString())
    }
}

tasks.register<Test>("testOnJava17") {
    javaLauncher = javaToolchains.launcherFor {
        languageVersion = JavaLanguageVersion.of(17)
    }

    useJUnitPlatform()

    systemProperty("org.slf4j.simpleLogger.log.io.bit3.jsass", "trace")

    if (project.hasProperty("test.tmpdir")) {
        val path = Paths.get(project.findProperty("test.tmpdir").toString())
        systemProperty("java.io.tmpdir", path.toAbsolutePath().normalize().toString())
    }
}

tasks.register<Test>("testOnJava21") {
    javaLauncher = javaToolchains.launcherFor {
        languageVersion = JavaLanguageVersion.of(21)
    }

    useJUnitPlatform()

    systemProperty("org.slf4j.simpleLogger.log.io.bit3.jsass", "trace")

    if (project.hasProperty("test.tmpdir")) {
        val path = Paths.get(project.findProperty("test.tmpdir").toString())
        systemProperty("java.io.tmpdir", path.toAbsolutePath().normalize().toString())
    }
}

tasks.check {
    dependsOn("testOnJava17", "testOnJava21")
}

sonarqube {
    properties {
        property("sonar.projectKey", "jsass_jsass")
        property("sonar.organization", "jsass")
    }
}

tasks.javadoc {
    options {
        // addBooleanOption("html5", true)
    }
}
