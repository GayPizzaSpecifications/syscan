import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  application

  kotlin("jvm") version "1.6.10"
  kotlin("plugin.serialization") version "1.6.10"
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  testImplementation("org.jetbrains.kotlin:kotlin-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
  implementation("com.github.ajalt.clikt:clikt:3.4.0")
}

application {
  mainClass.set("io.kexec.syscan.MainKt")
}

java {
  val javaVersion = JavaVersion.toVersion(17)
  sourceCompatibility = javaVersion
  targetCompatibility = javaVersion
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "17"
}

tasks.withType<Wrapper> {
  gradleVersion = "7.3.3"
  distributionType = Wrapper.DistributionType.ALL
}
