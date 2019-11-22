plugins {
    kotlin("jvm")
    application
    id("com.github.johnrengelman.shadow")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly("io.arrow-kt:arrow-annotations:0.10.3")
    compileOnly(project(":remote"))

    // To run :create-plugin:shadowJar when building this project
    api(project(":compiler-plugin", "shadow"))
}

application {
    mainClassName = "test.AppKt"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xplugin=${project.rootDir}/compiler-plugin/build/libs/compiler-plugin-all.jar")
    }
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    configurations = listOf(project.configurations.compileOnly.get())
}