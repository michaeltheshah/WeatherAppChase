plugins {
    val gradleVersion = "8.1.0"
    val kotlinVersion = "1.9.10"

    id("com.android.application") version gradleVersion apply false
    id("com.android.library") version gradleVersion apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
    id("org.jetbrains.kotlin.jvm") version kotlinVersion apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    kotlin("plugin.serialization") version kotlinVersion
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
