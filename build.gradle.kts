plugins {
    `java-library`
    `maven-publish`
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "com.imjustdoom.villagerinabucket"
version = "1.1.0"

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}
publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

//tasks.processResources {
//    filesMatching("**/plugin.yml") {
//        expand(
//            "name" to rootProject.name,
//            "version" to project.version,
//            "group" to project.group.toString()
//        )
//    }
//}

tasks {
    runServer {
        minecraftVersion("1.21.4")
    }
}