
allprojects {
    repositories {
        jcenter()
        maven { url = uri("https://oss.jfrog.org/artifactory/oss-snapshot-local/") }
    }
}