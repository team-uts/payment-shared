plugins {
  // library variant in the published Gradle Module Metadata.
  `java-library`
  `maven-publish`
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.lombok)
  alias(libs.plugins.spotless)
}

// group/version live in gradle.properties so JitPack can override them with -P flags.
description = "Shared utilities for payment projects"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(libs.versions.java.get())
  }
  withSourcesJar()
  withJavadocJar()
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
  }
}

// NOTE: the org.springframework.boot plugin is deliberately NOT applied here.
// It would turn the jar into an executable fat bootJar, which is wrong for a library.
// We only import the Boot BOM so dependency versions match the consumer apps.
val springBootBom = dependencies.platform(libs.spring.boot.dependencies)

dependencies {
  compileOnly(springBootBom)
  annotationProcessor(springBootBom)

  compileOnly(libs.spring.boot.autoconfigure)

  // Spring is compileOnly on purpose: every consumer is already a Spring Boot app
  // and brings these at runtime. Keeping them off the published POM means this
  // library can never drag a conflicting Spring version into payment-app/point-app.
  compileOnly(libs.spring.security.web)
  compileOnly(libs.spring.context)
  compileOnly(libs.spring.web)
  compileOnly(libs.spring.tx)
  compileOnly(libs.jakarta.servlet.api)
  compileOnly(libs.slf4j.api)

  compileOnly(libs.lombok)
  annotationProcessor(libs.lombok)

  compileOnly(libs.google.gson)
  compileOnly(libs.snakeyaml)
  compileOnly(libs.jackson.annotation)

  // Tests need the real thing on the classpath.
  testImplementation(libs.spring.test)
  testImplementation(libs.junit.jupiter)
  testImplementation(libs.assertj.core)
  testImplementation(libs.mockito.core)
  testRuntimeOnly(libs.junit.platform.launcher)
  testImplementation(kotlin("test"))
}

configurations {
  testImplementation {
    extendsFrom(compileOnly.get())
  }

  testAnnotationProcessor {
    extendsFrom(annotationProcessor.get())
  }
}

tasks.test {
  useJUnitPlatform()
}

tasks.javadoc {
  // Publish a javadoc jar without failing/spamming on missing @param/@return.
  (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

tasks.withType<Jar>().configureEach {
  isPreserveFileTimestamps = false
  isReproducibleFileOrder = true
}

val pagesRepoDir =
  providers
    .gradleProperty("pagesRepoDir")
    .map { file(it) }
    .orElse(layout.buildDirectory.dir("maven-repo").map { it.asFile })

publishing {
  publications {
    create<MavenPublication>("library") {
      from(components["java"])
      groupId = "dev.teamuts.shared"
      artifactId = "payment-shared"
    }
  }

  repositories {
    maven {
      name = "pages"
      url = uri(pagesRepoDir.get())
    }
  }
}

spotless {
  java {
    target("src/**/*.java")
    targetExclude("**/generated/**")
    googleJavaFormat("2.0.0")
    removeUnusedImports()
  }

  kotlin {
    target("src/**/*.kt")
    targetExclude("**/generated/**", "**/build/**")

    ktfmt().googleStyle()
    trimTrailingWhitespace()
    endWithNewline()
  }

  kotlinGradle {
    target("**/*.gradle.kts")
    targetExclude("**/build/**")

    ktfmt().googleStyle()
    trimTrailingWhitespace()
    endWithNewline()
  }
}
