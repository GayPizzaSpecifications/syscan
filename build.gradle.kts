import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  application

  kotlin("jvm") version "1.7.21"
  kotlin("plugin.serialization") version "1.7.21"

  id("com.github.johnrengelman.shadow") version "7.1.2"
  id("org.graalvm.buildtools.native") version "0.9.18"
}

repositories {
  mavenCentral()
}

sourceSets.create("constructs").apply {
  compileClasspath += sourceSets.main.get().output
  runtimeClasspath += sourceSets.main.get().output
}

java {
  val javaVersion = JavaVersion.toVersion(17)
  sourceCompatibility = javaVersion
  targetCompatibility = javaVersion
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-bom")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
  implementation("com.github.ajalt.clikt:clikt:3.5.0")
  implementation("com.zaxxer:nuprocess:2.0.6")
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "17"
}

tasks.withType<Wrapper> {
  gradleVersion = "7.6"
}

application {
  mainClass.set("lgbt.mystic.syscan.tool.MainKt")
}
