dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("versionsPlugin", "com.github.ben-manes:gradle-versions-plugin:0.51.0")
            library("analyze", "ca.cutterslade.gradle:gradle-dependency-analyze:1.9.2")
            library("release", "net.researchgate:gradle-release:3.0.2")
            library("grgit", "org.ajoberstar.grgit:grgit-gradle:5.2.1")
            library("sonarqube", "org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:5.0.0.4638")
            library("lombok", "io.freefair.gradle:lombok-plugin:8.6")
            library("frontend", "org.siouan:frontend-jdk11:8.0.0")
        }
    }
}
