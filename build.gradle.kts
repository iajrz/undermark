import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.30"
    id("org.jetbrains.dokka") version "1.4.20"
}

group = "com.iajrz"
version = "0.9.1"

ext {
    this["PUBLISH_GROUP_ID"] = group
    this["PUBLISH_VERSION"] = version
    this["PUBLISH_ARTIFACT_ID"] = rootProject.name
}
apply { from("publish-mavencentral.gradle") }

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}