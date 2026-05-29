# ── Stage 1: Build ──────────────────────────────────────────────────────────
# Debian-based (glibc) so the node-gradle plugin can download the Node/pnpm
# binaries needed to build the React frontend. The Gradle wrapper pulls its
# own Gradle 9.0, so the base image's Gradle version is irrelevant.
FROM gradle:8.7-jdk17 AS builder
WORKDIR /app

# Gradle build configuration and wrapper first (better layer caching).
COPY build.gradle settings.gradle gradlew gradlew.bat ./
COPY gradle/ gradle/
RUN chmod +x gradlew

# Backend sources and the React frontend (built into the boot jar as static
# resources via pnpmBuild → processResources).
COPY src/ src/
COPY frontend/ frontend/

# bootJar produces only the executable fat jar (avoids the *-plain.jar that a
# full `build` would also create). Tests run in the CI pipeline, not here.
RUN ./gradlew clean bootJar --no-daemon

# ── Stage 2: Run ────────────────────────────────────────────────────────────
# Slim JRE-only image; the frontend is already baked into the jar, so no Node
# is needed at runtime.
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Run as an unprivileged user.
RUN addgroup -S app && adduser -S app -G app
USER app

COPY --from=builder /app/build/libs/ShopSimulator-*.jar app.jar

EXPOSE 8080

# exec replaces the shell so the JVM is PID 1 and receives SIGTERM directly,
# enabling Spring Boot's graceful shutdown. ${PORT} lets a host (e.g. Render)
# inject the listen port; defaults to 8080 locally.
ENTRYPOINT ["sh", "-c", "exec java ${JAVA_OPTS} -jar app.jar --server.port=${PORT:-8080}"]
