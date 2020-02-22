plugins {
    kotlin("jvm")
    kotlin("kapt")
    `java-library`
    id("com.github.johnrengelman.shadow")
}

val ARROW_VERSION="0.10.4-SNAPSHOT"

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(kotlin("compiler-embeddable"))
    compileOnly("io.arrow-kt", "compiler-plugin", "1.3.61-SNAPSHOT")

    testImplementation("io.arrow-kt", "testing-plugin", "1.3.61-SNAPSHOT")
    testImplementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
    testImplementation ("io.github.classgraph:classgraph:4.8.47")

    testImplementation ("io.arrow-kt:arrow-core-data:$ARROW_VERSION")
    kapt("io.arrow-kt:arrow-annotations-processor:0.7.1")
    implementation("io.arrow-kt:arrow-annotations:0.7.1") {
        exclude( group= "org.jetbrains.kotlin", module= "kotlin-stdlib")
    }
    testImplementation("io.arrow-kt:arrow-core-data:$ARROW_VERSION") {
        exclude (group= "org.jetbrains.kotlin", module= "kotlin-stdlib")
    }
    testImplementation("io.arrow-kt:arrow-optics:$ARROW_VERSION") {
        exclude (group= "org.jetbrains.kotlin", module= "kotlin-stdlib")
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

// Create a new JAR with: Arrow Meta + new plugin
tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    configurations = listOf(project.configurations.compileOnly.get())
    dependencies {
        exclude("org.jetbrains.kotlin:kotlin-stdlib")
        exclude("org.jetbrains.kotlin:kotlin-compiler-embeddable")
    }
}

tasks.test{
    useJUnitPlatform()
}