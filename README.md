# payment-shared

Shared Java library for [`payment-app`](https://github.com/team-uts/payment-app) and [`point-app`](https://github.com/team-uts/point-app). It provides common domain types,
error handling, HTTP response models, and Spring integration under `dev.teamuts.shared`.
It is published as a regular JAR with source and Javadoc JARs.

## Requirements

- Java 21.
- The included Gradle wrapper (9.6.0).
- A consuming Spring Boot application that supplies the dependencies used by the shared APIs.

The build uses the Spring Boot `4.1.0` BOM. Keep this aligned with the consumer apps;
versions are configured in `gradle/libs.versions.toml`.

## Using the library (Consumer applications)

- [payment-app](https://github.com/team-uts/payment-app)
- [point-app](https://github.com/team-uts/point-app)

Add the Maven repository to the consumer's `settings.gradle.kts`:

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://team-uts.github.io/payment-shared/maven-repo")
            content { includeGroup("dev.teamuts.shared") }
        }
        mavenCentral()
    }
}
```

Keep the content filter so Gradle only queries this repository for the group it hosts.
Then add the dependency to the module that uses it:

```kotlin
// build.gradle.kts — version currently used by payment-app and point-app
implementation("dev.teamuts.shared:payment-shared:{RELEASE_VERSION}")
```

Published artifact versions have no `v` prefix; release tags use `v` (for example,
artifact version `0.4.2` and tag `v0.4.2`). Consumers do not need repository credentials.

### Dependency policy

Production dependencies are declared `compileOnly`, so the published library does not
bring transitive runtime dependencies into consuming applications. The Spring Boot BOM
is used for the build and is not exported to consumers.

Consumers must supply the dependencies needed by the APIs they use: Spring Boot
autoconfiguration, Spring Context, Web, Transactions and Security Web, Jakarta Servlet,
SLF4J, Gson, SnakeYAML, and Jackson annotations. Lombok is used during compilation of
this library. Adding `payment-shared` alone does not install these frameworks or configure
application security and persistence.

## Spring Boot autoconfiguration

The library registers two autoconfigurations through
`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`:

- `SharedAutoConfiguration` provides an `AppProfile` bean when one is missing.
- `SharedMessageAutoConfiguration` provides a `messageSource` when neither a bean with
  that name nor another `MessageSource` exists. It uses the configured `spring.messages`
  basenames followed by `shared-messages`, along with the configured encoding, locale
  fallback, cache duration, common messages, and code-as-default-message setting.

Shared message bundles are included as `shared-messages.properties` and
`shared-messages_en.properties`. Applications that provide their own `MessageSource`
should include `shared-messages` themselves if they need those messages.

## Local development

Run commands from the `payment-shared` directory:

```bash
# Compile, run tests, check formatting, and assemble artifacts
./gradlew clean build

# Run tests only
./gradlew test

# Check or apply formatting
./gradlew spotlessCheck
./gradlew spotlessApply

# Publish to ~/.m2/repository for local consumer testing
./gradlew publishToMavenLocal -Pversion=0.4.3-local

# Generate the Maven repository in build/maven-repo
./gradlew publishLibraryPublicationToPagesRepository -Pversion=0.4.3-local
```

The versions above are examples. Pass `-Pversion=...` explicitly when publishing;
`gradle.properties` does not define a release version. To use a local publication,
add `mavenLocal()` to the consumer's dependency repositories and select the same version.
Use `-PpagesRepoDir=/path/to/maven-repo` to change the repository output directory.

## CI and releases

Artifacts are hosted in a Maven repository on GitHub Pages, backed by the `gh-pages`
branch. The release workflow expects that branch to exist and GitHub Pages to serve
its repository contents, including `maven-repo/`.

### Continuous integration

`.github/workflows/ci.yml` runs on pushes to `main` with Temurin Java 21. It runs
`clean test`, `clean build`, and a local Maven publication with version `ci-snapshot`.
The CI publication verifies packaging; it does not push artifacts to GitHub Pages.

### Publishing a release

1. Push the intended changes and open **Actions → Release → Run workflow**.
2. Select the branch to release (normally `main`) and enter a version in
   `MAJOR.MINOR.PATCH` form, such as `0.4.3`, without a `v` prefix.
3. The workflow builds the library, checks out `gh-pages`, refuses to overwrite an
   existing artifact version, and publishes into `maven-repo/` on that branch.
4. It pushes the Maven repository, creates the `v<version>` tag and a GitHub Release
   with generated notes, then waits up to five minutes for Pages to serve the POM.
5. Check the workflow and Pages deployment, then update the dependency version in
   `payment-app` and `point-app`.

The workflow uses the built-in `GITHUB_TOKEN` with `contents: write`; no personal
access token is configured. A Pages availability timeout produces a warning, so check
the Pages deployment if the new version is not yet available.
