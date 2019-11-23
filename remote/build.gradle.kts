plugins {
    kotlin("jvm")
    `java-library`
}

dependencies {
    compileOnly(kotlin("stdlib"))
    api("io.ktor:ktor-network:1.2.4")
}
