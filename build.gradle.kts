plugins {
    // library variant in the published Gradle Module Metadata.
    `java-library`
    `maven-publish`
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

// NOTE: the org.springframework.boot plugin is deliberately NOT applied here.
// It would turn the jar into an executable fat bootJar, which is wrong for a library.
// We only import the Boot BOM so dependency versions match the consumer apps.
val springBootBom = dependencies.platform(libs.spring.boot.dependencies)

dependencies {
    compileOnly(springBootBom)
    annotationProcessor(springBootBom)
    testImplementation(springBootBom)
    testAnnotationProcessor(springBootBom)

    // Spring is compileOnly on purpose: every consumer is already a Spring Boot app
    // and brings these at runtime. Keeping them off the published POM means this
    // library can never drag a conflicting Spring version into payment-app/point-app.
    compileOnly(libs.spring.context)
    compileOnly(libs.spring.web)
    compileOnly(libs.spring.tx)
    compileOnly(libs.jakarta.servlet.api)
    compileOnly(libs.slf4j.api)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // Tests need the real thing on the classpath.
    testImplementation(libs.spring.context)
    testImplementation(libs.spring.web)
    testImplementation(libs.spring.tx)
    testImplementation(libs.spring.test)
    testImplementation(libs.jakarta.servlet.api)
    testImplementation(libs.slf4j.api)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testImplementation(libs.mockito.core)
    testRuntimeOnly(libs.junit.platform.launcher)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
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

publishing {
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            artifactId = "payment-shared"
            groupId = "dev.teamuts.shared"
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
}
