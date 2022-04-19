import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.6.20"
  kotlin("plugin.serialization") version "1.6.20"
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
  implementation("com.github.ajalt.clikt:clikt:3.4.0")
  implementation("com.zaxxer:nuprocess:2.0.2")
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "17"
}

tasks.withType<Wrapper> {
  gradleVersion = "7.4"
  distributionType = Wrapper.DistributionType.ALL
}
