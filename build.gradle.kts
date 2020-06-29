import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.3.1.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

val ktor_version by extra("1.3.2")
val kotlinx_coroutines_version by extra("1.3.4")

repositories {
	mavenCentral()
	jcenter()
	maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
}


dependencies {

	implementation("org.eclipse.jetty.websocket:websocket-client:9.4.30.v20200611")

	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$kotlinx_coroutines_version")

	implementation("io.ktor:ktor-client-websockets:$ktor_version")
	implementation("io.ktor:ktor-client-cio:$ktor_version")
	implementation("io.ktor:ktor-client-js:$ktor_version")
	implementation("io.ktor:ktor-client-okhttp:$ktor_version")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}
