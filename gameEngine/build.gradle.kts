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
    implementation(project(":api"))

    implementation(libs.gdx.core)
    implementation(libs.gdx.freetype)
    implementation(libs.gdx.freetype.natives) { artifact { classifier = "natives-desktop" } }
}