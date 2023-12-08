plugins {
    id("java")
}

group = "committee.nova"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.ow2.asm:asm:9.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.named<Jar>("jar") {
    manifest {
        attributes["Premain-Class"] = "committee.nova.runitanyway.RunItAnyway"
        attributes["Can-Redefine-Classes"] = "true"
        attributes["Can-Retransform-Classes"] = "true"
    }
    from(sourceSets.main.get().output)
}