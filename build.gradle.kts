plugins {
    kotlin("jvm") version "2.4.10"
}

group = "app.masajid"
version = "1.0.0"

repositories {
    // morphe-patcher is published here by `morphe-patcher: publishToMavenLocal`.
    mavenLocal()
    mavenCentral()
    google()
    maven {
        url = uri("https://jitpack.io")
        content {
            includeGroup("com.github.MorpheApp.smali")
            includeGroup("com.github.REAndroid")
        }
    }
}

dependencies {
    // Provided at runtime by Morphe Desktop / Manager. `implementation` (not compileOnly)
    // so its transitive deps (smali/dexlib2) resolve for compilation; the jar task only
    // packages this project's own classes, so nothing is bundled into the .mpp.
    implementation("app.morphe:morphe-patcher:1.14.0")
    // The patcher POM exposes smali (dexlib2) as runtime scope, so add it explicitly
    // for compilation — same coordinates the patcher itself uses.
    compileOnly("com.github.MorpheApp.smali:smali:d856bad65f")
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.jar {
    archiveFileName.set("masajid-patches-${project.version}.mpp")
    manifest {
        attributes(
            "Name" to "Masajid Patches",
            "Description" to "Patches for sa.gov.moia.beneficiaryapp (Masajid)",
            "Version" to project.version.toString(),
            "Timestamp" to System.currentTimeMillis().toString(),
            "Source" to "https://github.com/kaza7/masajid-patches",
            "Author" to "kaza7",
            "Contact" to "https://github.com/kaza7",
            "Website" to "https://github.com/kaza7/masajid-patches",
            "License" to "GNU General Public License v3.0",
            // Lowest patcher the bundle needs; Desktop 1.16.0 ships a newer one.
            "Patcher-Version" to "1.10.0",
        )
    }
}

// `./gradlew buildBundle` -> build/libs/masajid-patches-1.0.0.mpp
tasks.register("buildBundle") {
    dependsOn(tasks.jar)
}
