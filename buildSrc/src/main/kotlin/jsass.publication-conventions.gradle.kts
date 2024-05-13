plugins {
    id("signing")
    id("maven-publish")
}

publishing {
    if (project.hasProperty("ossrhUsername") && project.hasProperty("ossrhPassword")) {
        repositories {
            maven {
                name = "MavenCentral"
                uri(when {
                    version.toString().endsWith("-SNAPSHOT") -> "https://oss.sonatype.org/content/repositories/snapshots/"
                    else -> "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                })
                credentials {
                    username = project.property("ossrhUsername")?.toString()
                    password = project.property("ossrhPassword")?.toString()
                }
            }
        }
    }

    val ciJobToken = System.getenv("CI_JOB_TOKEN")
    val ciApiUrl = System.getenv("CI_API_V4_URL")
    val ciProjectId = System.getenv("CI_PROJECT_ID")

    if (!ciJobToken.isNullOrBlank() && !ciApiUrl.isNullOrBlank() && !ciProjectId.isNullOrBlank()) {
        repositories {
            maven {
                name = "Gitlab"
                uri("${ciApiUrl}/projects/${ciProjectId}/packages/maven")
                credentials(HttpHeaderCredentials::class) {
                    name = "Job-Token"
                    value = ciJobToken
                }
                authentication {
                    this.
                    create<HttpHeaderAuthentication>("header")
                }
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            groupId = "io.bit3"
            artifactId = project.name
            version = project.version.toString()

            from (components["java"])

            pom {
                name = project.name
                description = project.description
                url = "https://gitlab.com/jsass/jsass"

                licenses {
                    license {
                        name = "MIT"
                        url = "https://raw.githubusercontent.com/bit3/jsass/master/LICENSE"
                    }
                }

                organization {
                    name = "bit3"
                    url = "http://bit3.io"
                }

                developers {
                    developer {
                        id = "tril"
                        name = "Tristan Lins"
                        email = "tristan@lins.io"
                        url = "https://tristan.lins.io"
                    }
                }

                issueManagement {
                    system = "GitLab.com"
                    url = "https://gitlab.com/jsass/jsass/issues"
                }

                ciManagement {
                    system = "GitLab.com"
                    url = "https://gitlab.com/jsass/jsass/pipelines"
                }

                scm {
                    url = "https://gitlab.com/jsass/jsass"
                    connection = "scm:git:https://gitlab.com/jsass/jsass.git"
                    developerConnection = "scm:git:git@gitlab.com:jsass/jsass.git"
                }
            }
        }
    }
}

// signing artifacts
if (project.hasProperty("signing.gnupg.keyName") && project.hasProperty("signing.gnupg.passphrase")) {
    signing {
        useGpgCmd()
        sign(publishing.publications.getByName("maven"))
    }
} else if (project.hasProperty("signing.keyId") && project.hasProperty("signing.password")) {
    signing {
        sign(publishing.publications.getByName("maven"))
    }
}
