import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.lang.Boolean
import org.gradle.api.tasks.Copy
import org.gradle.jvm.tasks.Jar
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.compile.JavaCompile

val mod_version: String by project
val maven_group: String by project
val archives_base_name: String by project
val minecraft_version: String by project
val yarn_mappings: String by project
val loader_version: String by project
val fabric_api_version: String by project
val fabric_kotlin_version: String by project

plugins {
    // Versions for these plugins are provided via settings.gradle.kts pluginManagement
    id("fabric-loom")
    `maven-publish`
    kotlin("jvm")
}

version = mod_version
group = maven_group

base {
    archivesName.set(archives_base_name)
}

repositories {
    // Add repositories if you need to fetch other artifacts
}

// Configure Fabric API data generation equivalent to the Groovy block:
// fabricApi {
//     configureDataGeneration { client = true }
// }
// We call the extension reflectively so this file doesn't require the Fabric API extension class
// to be present on the Kotlin DSL classpath at evaluation time.
extensions.findByName("fabricApi")?.let { ext ->
    try {
        val method = ext::class.java.getMethod("configureDataGeneration", org.gradle.api.Action::class.java)
        val action = object : org.gradle.api.Action<Any> {
            override fun execute(dataGenExt: Any) {
                try {
                    // Try to call setClient(Boolean) / setClient(boolean)
                    val methods = dataGenExt::class.java.methods
                    val setClient = methods.firstOrNull { m -> m.name == "setClient" && m.parameterTypes.size == 1 }
                    if (setClient != null) {
                        setClient.invoke(dataGenExt, Boolean.TRUE)
                    } else {
                        // Fallback: try to set a property named "client" via a setter-style method
                        val clientSetter = methods.firstOrNull { m -> m.name.equals("client", ignoreCase = true) && m.parameterTypes.size == 1 }
                        clientSetter?.invoke(dataGenExt, Boolean.TRUE)
                    }
                } catch (_: Exception) {
                    // ignore failures - data gen config is optional
                }
            }
        }
        method.invoke(ext, action)
    } catch (_: Exception) {
        // ignore if the extension or method is not available
    }
}

dependencies {
    // To change the versions see the gradle.properties file
    add("minecraft", "com.mojang:minecraft:${minecraft_version}")
    add("mappings", loom.officialMojangMappings())
    add("modImplementation", "net.fabricmc:fabric-loader:${loader_version}")

    // Fabric API. This is technically optional, but you probably want it anyway.
    add("modImplementation", "net.fabricmc.fabric-api:fabric-api:${fabric_api_version}")
    add("modImplementation", "net.fabricmc:fabric-language-kotlin:${fabric_kotlin_version}")
}

// If you used fabricApi.configureDataGeneration in Groovy, you can re-add a typed configuration here
// but it's optional. Leaving it out keeps this file simple and focused on Kotlin DSL conversion.

tasks.named<Copy>("processResources") {
    inputs.property("version", project.version)
    filesMatching("fabric.mod.json") {
        expand(mapOf("version" to project.version))
    }
}

// Java compile options
tasks.withType<JavaCompile> {
    options.release.set(21)
}

// Kotlin compile options (migrated to compilerOptions DSL)
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget("21"))
    }
}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.named<Jar>("jar") {
    inputs.property("archivesName", archives_base_name)

    from("LICENSE") {
        rename { "${it}_$archives_base_name" }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = archives_base_name
            from(components["java"])
        }
    }

    repositories {
        // Add repositories to publish to here.
    }
}
