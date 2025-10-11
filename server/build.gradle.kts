plugins {
	kotlin("jvm") version "2.1.0"
	kotlin("plugin.spring") version "2.1.0"
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "2.1.0"
	id("org.openapi.generator") version "7.10.0"
}

group = "co.flyingtoaster"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
    implementation("org.springframework.ai:spring-ai-starter-mcp-server:1.0.1")
    implementation("org.springframework.ai:spring-ai-model:1.0.1")
    implementation("io.modelcontextprotocol.sdk:mcp:0.10.0")

    implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("io.swagger.core.v3:swagger-annotations:2.2.15")

    implementation(project(":lib-mind"))

	implementation("org.springframework.boot:spring-boot-starter-data-redis:3.5.5")

    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("io.mockk:mockk:1.13.13")
	testImplementation("org.assertj:assertj-core:3.27.3")
	testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
	testImplementation("com.squareup.okhttp3:okhttp:4.12.0")
	testImplementation("com.squareup.retrofit2:retrofit:3.0.0")
	testImplementation("com.squareup.retrofit2:converter-jackson:3.0.0")
	testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.1")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

sourceSets {
	main {
		kotlin {
			srcDir("${layout.buildDirectory}/generated/src/main/kotlin")
		}
	}
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
	mainClass.set("co.flyingtoaster.mind.mcp.MindMcpServerApplicationKt")
}

