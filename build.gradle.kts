/*
 * Root build script. Holds configuration shared by every module so the
 * per-module scripts only declare what makes them different.
 */

plugins {
    java
}

subprojects {
    apply(plugin = "java-library")

    group = "io.streams"
    version = "0.1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    dependencies {
        "implementation"(rootProject.libs.slf4j.api)

        "testImplementation"(rootProject.libs.junit.jupiter)
        "testImplementation"(rootProject.libs.assertj.core)
        "testRuntimeOnly"(rootProject.libs.junit.platform.launcher)
        "testRuntimeOnly"(rootProject.libs.logback.classic)
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs.add("-Xlint:all")
    }
}
