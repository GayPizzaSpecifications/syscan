import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  application

  kotlin("jvm") version "1.6.21"
  kotlin("plugin.serialization") version "1.6.21"

  id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
  mavenCentral()
}

java {
  val javaVersion = JavaVersion.toVersion(17)
  sourceCompatibility = javaVersion
  targetCompatibility = javaVersion
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-bom")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
  implementation("com.github.ajalt.clikt:clikt:3.4.2")
  implementation("com.zaxxer:nuprocess:2.0.2")
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "17"
}

tasks.withType<Wrapper> {
  gradleVersion = "7.4.2"
  distributionType = Wrapper.DistributionType.ALL
}

application {
  mainClass.set("io.kexec.syscan.MainKt")
}
