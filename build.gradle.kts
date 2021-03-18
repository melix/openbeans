import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.get
import org.gradle.api.publish.maven.MavenPom

plugins {
    `maven-publish`
    `java-library`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.0.0"
}

group = "me.champeau.openbeans"
version = "1.0.1"
description = "Openbeans for Android"

java {
    withSourcesJar()
    withJavadocJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "6"
    targetCompatibility = "6"
}

tasks.withType<Javadoc> {
    options {
        this as CoreJavadocOptions
        addStringOption("Xdoclint:none", "-quiet")
    }
}

sourceSets {
    main {
        java {
            srcDir("src")
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            username.set(providers.systemProperty("central.username").forUseAtConfigurationTime())
            password.set(providers.systemProperty("central.password").forUseAtConfigurationTime())
        }
    }
}

publishing {
    repositories {
        maven {
            name = "projectLocal"
            url = uri("${rootProject.buildDir}/repo")
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set(providers.provider { "${project.group}:${project.name}" })
                description.set(providers.provider { project.description })
                url.set("https://github.com/melix/openbeans")
                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        name.set("Cédric Champeau")
                        email.set("cedric.champeau@gmail.com")
                        organization.set("Personal")
                        organizationUrl.set("https://github.com/melix")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/melix/openbeans.git")
                    developerConnection.set("scm:git:ssh://github.com/melix/openbeans.git")
                    url.set("https://github.com/melix/openbeans/tree/master")
                }
            }
        }
    }
}

signing {
    setRequired {
        gradle.taskGraph.allTasks.any {
            it.name.startsWith("publish")
        }
    }
    publishing.publications.configureEach {
        sign(this)
    }
    useGpgCmd()
}

fun isSnapshot() = (version as String).endsWith("-SNAPSHOT")