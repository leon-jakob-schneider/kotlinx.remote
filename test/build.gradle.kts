plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation(kotlin("stdlib"))
}

application {
    mainClassName = "test.AppKt"
}
