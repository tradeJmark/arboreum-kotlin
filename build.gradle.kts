plugins {
    kotlin("jvm") version "1.5.31"
    id("maven-publish")
    id("java-library")
}

group = "ca.tradejmark.arboreum"
version = "0.0.2"

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("arboreumKotlin") {
            from(components["kotlin"])
        }
    }

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

dependencies {
    implementation(kotlin("stdlib"))
    api(project(":arboreum-common"))

    val kMongoVersion = "4.3.0"
    implementation("org.litote.kmongo:kmongo:$kMongoVersion")
    implementation("org.litote.kmongo:kmongo-coroutine:$kMongoVersion")

    val jUnitVersion = "5.8.1"
    testImplementation(platform("org.junit:junit-bom:$jUnitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter:$jUnitVersion")
    testImplementation("org.litote.kmongo:kmongo-flapdoodle:$kMongoVersion")
}