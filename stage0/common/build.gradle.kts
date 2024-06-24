plugins {
    `java-library`
}

dependencies {
    compileOnlyApi("com.google.code.gson:gson:2.2.4")

    // real MC uses older versions of log4j but that doesn't really matter in this case
    compileOnlyApi("org.apache.logging.log4j:log4j-api:2.19.0")
    compileOnlyApi("org.apache.logging.log4j:log4j-core:2.19.0")
}