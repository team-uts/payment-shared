# payment-shared

Shared Spring utilities (error model, transaction annotations, utils) for `payment-app` and `point-app`.

## Dependency policy

Spring and Jakarta dependencies are declared `compileOnly` here, so the published POM has
**no transitive dependencies**. Consumers are Spring Boot apps and already supply
`spring-context`, `spring-web`, `spring-tx` and `jakarta.servlet-api` at runtime — this
guarantees the library can never pull a conflicting Spring version into an app.

The Spring Boot BOM version in `gradle/libs.versions.toml` should be kept in sync with the
consumer apps (currently `4.1.0`).

## Publishing via JitPack (alternative to GitHub Packages)

JitPack builds from a git tag — there is no publish step and no credentials to manage.

**The repo must be public.** JitPack's free tier only builds public repositories; private
repos require a paid subscription. For a payment library that is the decision to make
before anything else.

### From Github Actions (preferred)
- `.github/workflows/ci.yml`
  - builds and checks the validity of code on every push to `main`.
- `.github/workflows/release.yml`
  - runs on a published GitHub Release.
  - It uses the built-in `GITHUB_TOKEN`, so no personal token is needed.
  - A release tagged `v0.1.0` publishes version `0.1.0`.

### From a laptop

```bash
./gradlew publishToMavenLocal # ~/.m2/repository
```

### Setup

Already in place: `jitpack.yml` pins OpenJDK 21 (JitPack defaults to 8, which cannot build
this project):
```yaml
jdk:
  - openjdk21
```

### Releasing

1. Push code to `main` branch in `github.com/team-uts/payment-shared`.
2. `Actions` → `Release` → `Run workflow` → Set Release version (e.g., `0.1.0`, without the letter `v`).
3. Open `https://jitpack.io/#team-uts/payment-shared` and check the build status.

### Consuming

```kotlin
// settings.gradle.kts
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

// build.gradle.kts
implementation("com.github.team-uts:payment-shared:v0.1.0")
```

The version is the tag verbatim, so `v0.1.0` (not `0.1.0`). Unlike GitHub Packages, no
credentials are needed on the consumer side.
