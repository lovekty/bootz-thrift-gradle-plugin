import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    kotlin("jvm") version "1.3.72"
    id("com.gradle.plugin-publish") version "0.12.0"
}

group = "cn.bootz.thrift"
version = "0.0.1"

dependencies {
    implementation(gradleApi())
    implementation(kotlin("stdlib-jdk8"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "thrift-gradle-plugin"
            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
    }
}