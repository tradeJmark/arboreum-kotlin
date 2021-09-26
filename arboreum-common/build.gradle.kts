plugins {
    kotlin("multiplatform")
    id("maven-publish")
    kotlin("plugin.serialization") version "1.5.31"
}

group = "ca.tradejmark.arboreum"
version = "0.0.2"

repositories {
    mavenCentral()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/tradeJmark/arboreum-kotlin")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0-RC")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        jvm()
        val jvmTest by getting {
            dependencies{
                val jUnitVersion = "5.8.1"
                implementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")
            }
        }
        js().browser()
    }
}

tasks.withType(Test::class).getByName("jvmTest") {
    useJUnitPlatform()
}