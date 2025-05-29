plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "8.3.5"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "com.imjustdoom.villagerinabucket"
if (project.hasProperty("buildWithGitHash")) {
    fun getShortCommitHash(): Provider<String> = providers.exec {
        commandLine("git", "rev-parse", "--short", "HEAD")
    }.standardOutput.asText.map { it.trim().ifEmpty { "unknown" } }
    version = "${rootProject.version}-${getShortCommitHash().get()}"
}

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
    implementation("org.bstats:bstats-bukkit:3.1.0")
}
publishing {
    repositories {
        maven {
            name = "imjustdoom"
            url = uri("https://repo.imjustdoom.com/releases")
            credentials {
                username = System.getenv("MAVEN_NAME")
                password = System.getenv("MAVEN_SECRET")
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            version = project.version.toString()
            artifact(tasks.shadowJar.get().archiveFile)
        }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    shadowJar {
        dependencies {
            include(dependency("org.bstats:bstats-bukkit:3.1.0"))
            include(dependency("org.bstats:bstats-base:3.1.0"))
        }
        relocate("org.bstats", project.group.toString() + ".bstats")
        archiveFileName.set("${rootProject.name}-paper-${project.version}.jar")
    }

    processResources {
        val pluginName = rootProject.name
        val pluginVersion = project.version
        val pluginGroup = project.group

        filesMatching("**/plugin.yml") {
            expand(
                "name" to pluginName,
                "version" to pluginVersion,
                "group" to pluginGroup
            )
        }
    }

    runServer {
        minecraftVersion("1.21.4")
        downloadPlugins {
            modrinth("luckperms", "v5.4.145-bukkit")
        }
        dependsOn(shadowJar)
    }

    build {
        dependsOn(shadowJar)
    }
}