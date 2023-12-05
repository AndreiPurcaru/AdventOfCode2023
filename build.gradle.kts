plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "me.andreipurcaru"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
    minHeapSize = "512m"
    maxHeapSize = "16384m"
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}