plugins {
    application
}

group = "ru.gamesphere"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("ru.gamesphere.Main")
}

dependencies {
    implementation(project(":jooq-generated"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}