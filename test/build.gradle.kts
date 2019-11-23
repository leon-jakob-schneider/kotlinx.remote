plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("io.arrow-kt:arrow-annotations:0.10.3")
    implementation(project(":remote"))

    // To run :create-plugin:shadowJar when building this project
    api(project(":compiler-plugin", "shadow"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xplugin=${project.rootDir}/compiler-plugin/build/libs/compiler-plugin-all.jar")
    }
}

tasks{
    val runClient by creating(JavaExec::class){
        main = "test.ClientKt"
        group = "application"
        classpath = files(findByName("shadowJar"))
    }

    val runServer by creating(JavaExec::class){
        main = "test.ServerKt"
        group = "application"
        classpath = files(findByName("shadowJar"))
    }
}