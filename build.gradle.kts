plugins {
    id("net.fabricmc.fabric-loom")
    id("com.modrinth.minotaur")
    `maven-publish`
}

repositories {
}

dependencies {
    minecraft("com.mojang:minecraft:${providers.gradleProperty("minecraft_version").get()}")
    implementation("net.fabricmc:fabric-loader:${providers.gradleProperty("loader_version").get()}")
}

loom {
    accessWidenerPath = file("src/main/resources/cull-everything.classtweaker")
}

tasks.processResources {
    val version = version
    inputs.property("version", version)

    filesMatching("fabric.mod.json") {
        expand("version" to version)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
    val projectName = project.name
    inputs.property("projectName", projectName)

    from("LICENSE") {
        rename { "${it}_$projectName" }
    }
}

modrinth {
    projectId.set("cull-everything")
    versionNumber.set(project.version.toString())
    versionName.set(project.version.toString())
    versionType.set(providers.gradleProperty("modrinth_version_type").get())
    uploadFile.set(tasks.jar)
    changelog = rootProject.file("CHANGELOG.md").readText()
    syncBodyFrom = rootProject.file("README.md").readText()

    val extraCompatibleVersions = providers.gradleProperty("modrinth_extra_minecraft_versions").get()
        .split(',');
    extraCompatibleVersions.forEach { gameVersions.add(it.trim()) }
}

tasks.modrinth {
    dependsOn(tasks.modrinthSyncBody)
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories {
    }
}
