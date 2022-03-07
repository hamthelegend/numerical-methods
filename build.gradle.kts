import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    application
    `maven-publish`
}

val groupId = "com.hamthelegend"
val artifactId = "numerical-methods"
val versionId = "0.1"

group = groupId
version = versionId

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.mpobjects:bdparsii:1.0.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}

application {
    mainClass.set("MainKt")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = groupId
            artifactId = artifactId
            version = versionId

            from(components["java"])
        }
    }
}