plugins {
    kotlin("jvm") version "1.9.21"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // https://mvnrepository.com/artifact/tools.aqua/z3-turnkey
    implementation("tools.aqua:z3-turnkey:4.12.2.1")

    // https://mvnrepository.com/artifact/org.jgrapht/jgrapht-core
    implementation("org.jgrapht:jgrapht-core:1.5.2")

}

tasks.test {
    useJUnitPlatform()
}
