plugins {
    kotlin("jvm") version "1.5.31"
}

group = "ca.tradejmark.arboreum"
version = "0.0.1"

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(kotlin("stdlib"))
    api(project(":arboreum-common"))

    val kMongoVersion = "4.3.0"
    implementation("org.litote.kmongo:kmongo:$kMongoVersion")
    implementation("org.litote.kmongo:kmongo-coroutine:$kMongoVersion")

    testImplementation(platform("org.junit:junit-bom:5.8.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.litote.kmongo:kmongo-flapdoodle:$kMongoVersion")
}