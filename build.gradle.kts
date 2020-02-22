
allprojects {
    repositories {
        mavenCentral()
        jcenter()
        maven { url = uri("https://oss.jfrog.org/artifactory/oss-snapshot-local/") }

        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
        maven {
            url = uri("https://dl.bintray.com/arrow-kt/arrow-kt/")
            content {
                includeGroup("io.arrow-kt")
            }
        }
        maven { url = uri("https://jitpack.io") }
    }
}