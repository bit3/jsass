import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import java.nio.file.Paths

plugins {
    id("signing")
    id("maven-publish")
}

//publishing {
//    if (project.hasProperty("ossrhUsername") && project.hasProperty("ossrhPassword")) {
//        repositories {
//            maven {
//                name = "MavenCentral"
//                url(version.endsWith("-SNAPSHOT") ? "https://oss.sonatype.org/content/repositories/snapshots/" : "https://oss.sonatype.org/service/local/staging/deploy/maven2/")
//                credentials {
//                    username ossrhUsername
//                    password ossrhPassword
//                }
//            }
//        }
//    }
//
//    var ciJobToken = System.getenv("CI_JOB_TOKEN")
//    var ciApiUrl = System.getenv("CI_API_V4_URL")
//    var ciProjectId = System.getenv("CI_PROJECT_ID")
//
//    if (ciJobToken && ciApiUrl && ciProjectId) {
//        repositories {
//            maven {
//                name = "Gitlab"
//                url("${ciApiUrl}/projects/${ciProjectId}/packages/maven")
//                credentials(HttpHeaderCredentials) {
//                    name = "Job-Token"
//                    value = ciJobToken
//                }
//                authentication {
//                    header(HttpHeaderAuthentication)
//                }
//            }
//        }
//    }
//
//    publications {
//        maven(MavenPublication) {
//            groupId = "io.bit3"
//            artifactId = project.name
//            version = project.version
//
//            from components.java
//
//            pom {
//                name = project.name
//                description = project.description
//                url = "https://gitlab.com/jsass/jsass"
//
//                licenses {
//                    license {
//                        name = "MIT"
//                        url = "https://raw.githubusercontent.com/bit3/jsass/master/LICENSE"
//                    }
//                }
//
//                organization {
//                    name = "bit3"
//                    url = "http://bit3.io"
//                }
//
//                developers {
//                    developer {
//                        id = "tril"
//                        name = "Tristan Lins"
//                        email = "tristan@lins.io"
//                        url = "https://tristan.lins.io"
//                    }
//                }
//
//                issueManagement {
//                    system = "GitLab.com"
//                    url = "https://gitlab.com/jsass/jsass/issues"
//                }
//
//                ciManagement {
//                    system = "GitLab.com"
//                    url = "https://gitlab.com/jsass/jsass/pipelines"
//                }
//
//                scm {
//                    url = "https://gitlab.com/jsass/jsass"
//                    connection = "scm:git:https://gitlab.com/jsass/jsass.git"
//                    developerConnection = "scm:git:git@gitlab.com:jsass/jsass.git"
//                }
//            }
//        }
//    }
//}
//
//// signing artifacts
//if (project.hasProperty("signing.gnupg.keyName") && project.hasProperty("signing.gnupg.passphrase")) {
//    signing {
//        useGpgCmd()
//        sign(publishing.publications.getByName("maven"))
//    }
//} else if (project.hasProperty("signing.keyId") && project.hasProperty("signing.password")) {
//    signing {
//        sign(publishing.publications.getByName("maven"))
//    }
//}
