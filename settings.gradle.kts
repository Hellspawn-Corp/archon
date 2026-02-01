// Load properties from gradle.properties so we can read plugin and version settings here
val props = java.util.Properties().apply {
    val propsFile = settings.rootDir.resolve("gradle.properties")
    if (propsFile.exists()) propsFile.inputStream().use { load(it) }
}

fun prop(name: String): String = props.getProperty(name)
    ?: error("Property '$name' not found in gradle.properties")

// The plugins block inside pluginManagement is evaluated specially by Gradle's settings script
// and often must use literal version strings. Here we read those literal strings from
// gradle.properties so they stay in a single source of truth.

pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        mavenCentral()
        gradlePluginPortal()
    }

    // helper to read gradle.properties keys from the settings script by loading property files
    val _gradleProps = java.util.Properties().apply {
        listOf(
            File(rootDir, "gradle.properties"),
            File(rootDir, "gradle/gradle.properties")
        ).forEach { f ->
            if (f.exists()) f.reader().use { load(it) }
        }
    }

    fun prop(key: String): String =
        _gradleProps.getProperty(key) ?: error("Property '$key' not found in gradle.properties")

    plugins {
        // Read versions from gradle.properties
        id("fabric-loom") version prop("loom_version")
        id("org.jetbrains.kotlin.jvm") version prop("kotlin_version")
    }
}
