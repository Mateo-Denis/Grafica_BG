plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.xerial:sqlite-jdbc:3.46.0.0")
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("org.slf4j:slf4j-simple:1.7.36")
    testImplementation(kotlin("test"))
    implementation("org.reflections:reflections:0.10.2")
    compileOnly ("org.projectlombok:lombok:1.18.34")
    annotationProcessor ("org.projectlombok:lombok:1.18.34")
    testCompileOnly ("org.projectlombok:lombok:1.18.34")
    testAnnotationProcessor ("org.projectlombok:lombok:1.18.34")
    implementation("com.itextpdf:kernel:8.0.2")
    implementation("com.itextpdf:layout:8.0.5")
    implementation ("com.jgoodies:jgoodies-forms:2.13.0")
    implementation ("org.swinglabs.swingx:swingx-all:1.6.5-1")
    implementation ("org.javatuples:javatuples:1.2")
    implementation ("com.google.guava:guava:32.1.2-jre")
    implementation ("com.formdev:flatlaf:3.5.1")
}

tasks.test {
    useJUnitPlatform()
}