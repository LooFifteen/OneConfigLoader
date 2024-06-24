dependencies {
    shade(project(":oneconfig-wrapper-common"))
}

publishing {
    publications {
        create<MavenPublication>("wrapper") {
            artifactId = project.name
            group = project.group
            version = project.version.toString()
            artifacts {
                artifact(tasks["remapJar"])
                artifact(tasks["sourcesJar"]) {
                    classifier = "sources"
                }
            }
        }
    }
}