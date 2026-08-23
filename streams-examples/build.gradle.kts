plugins {
    application
}

dependencies {
    implementation(project(":streams-api"))
    implementation(project(":streams-runtime"))
    implementation(project(":streams-core"))
    runtimeOnly(libs.logback.classic)
}

application {
    mainClass = "io.streams.examples.EchoJob"
}
