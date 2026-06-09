plugins {
    `java-library`
}
repositories {
    mavenCentral()
}
version = "1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
dependencies {
    implementation(libs.gdx.core)
    implementation(project(":api"))
}