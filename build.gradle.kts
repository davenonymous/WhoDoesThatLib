import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java-library")
    id("com.gradleup.shadow") version "9.0.0-beta9"
    id("maven-publish")
}

group = "com.davenonymous.whodoesthatlib"
version = "1.0.0"

repositories {
    mavenCentral()
    maven {
        url = uri("https://libraries.minecraft.net")
    }
}

java {
    withSourcesJar()
}


tasks.register<Jar>("apiJar") {
    group = "build"
    from(sourceSets.main.get().output)
    exclude("**/impl/**", "**/ModAnalyzer*.class", "**/JarScanner.class", "**/ResultAnalyzer.class")
    archiveClassifier.set("api")
}

val minecraftLibShadowJar by tasks.registering(ShadowJar::class) {
    group = com.github.jengelman.gradle.plugins.shadow.ShadowBasePlugin.GROUP_NAME
    description = "Create a jar with dependencies needed in Minecraft Instances"

    archiveClassifier = "mclib"
    from(sourceSets.main.map { it.output })
    configurations = provider { listOf(project.configurations["runtimeClasspath"]) }
    dependencies {
        exclude(dependency("com.mojang:datafixerupper:.*"))
        exclude(dependency("it.unimi.dsi:fastutil:.*"))
        exclude(dependency("org.slf4j:.*"))
        exclude(dependency("com.google.*:.*"))
        exclude(dependency("org.ow2.asm:asm:.*"))
    }

    manifest {
        // Optionally, set the main class for the JAR.
        attributes(mapOf(
            "Main-Class" to "com.davenonymous.whodoesthatlib.ModAnalyzerCLI",
            "Automatic-Module-Name" to "com.davenonymous.whodoesthatlib"
        ))
    }
}

tasks.assemble {
    dependsOn("apiJar")
    dependsOn(minecraftLibShadowJar)
    dependsOn("shadowJar")
}

dependencies {
    // These two are not needed when running within minecraft, but only for the CLI
    implementation("com.mojang:datafixerupper:8.0.16")
    implementation("org.ow2.asm:asm:9.7.1")

    // These are needed for both the CLI and the Minecraft mod
    implementation("org.tomlj:tomlj:1.1.1")
    implementation("info.picocli:picocli:4.7.6")
    implementation("org.yaml:snakeyaml:2.4")

    implementation("org.checkerframework:checker-qual:3.33.0")
    implementation("org.antlr:antlr4-runtime:4.13.1")

}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.davenonymous.whodoesthatlib.ModAnalyzerCLI"
    }
}

tasks.withType<ShadowJar> {
    relocate("org.tomlj", "com.davenonymous.whodoesthatlib.vendor.tomlj")
    relocate("picocli", "com.davenonymous.whodoesthatlib.vendor.picocli")
    relocate("org.yaml", "com.davenonymous.whodoesthatlib.vendor")
    relocate("org.checkerframework", "com.davenonymous.whodoesthatlib.vendor.checkerframework")
    relocate("org.antlr", "com.davenonymous.whodoesthatlib.vendor.antlr")
    exclude("META-INF/maven/**", "META-INF/versions/**")
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/davenonymous/whodoesthatlib")
            credentials {
                username = System.getenv("USERNAME")
                password = System.getenv("TOKEN")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            // Also publish the API jar
            artifact(tasks["apiJar"])
            // Also publish the shadow jar
            // artifact(tasks["shadowJar"])
            artifact(tasks["minecraftLibShadowJar"])
        }
    }
}
