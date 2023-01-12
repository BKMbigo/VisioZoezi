plugins {
    kotlin("jvm")
    application
}

group = "com.github.bkmbigo.visiozoezi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(Training.kotlinDlTensorflow)
    implementation(Training.kotlinDlDataset)

    api(Desktop.deepLearning4JCore)
    api(Desktop.deepLearning4JModelImport)
}