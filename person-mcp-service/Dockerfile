# Multi-stage build for person-mcp-service
# Stage 1: Extract Spring Boot layers
FROM eclipse-temurin:21-jre AS builder
WORKDIR /app
COPY target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Stage 2: Minimal runtime image
FROM eclipse-temurin:21-jre
WORKDIR /app

LABEL maintainer="example@company.com"
LABEL description="Person MCP Service — Clean Architecture"
LABEL version="1.0.0"

# Copy Spring Boot layers for optimal caching
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./

# Non-root user for security
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

EXPOSE 8060

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "org.springframework.boot.loader.launch.JarLauncher"]
