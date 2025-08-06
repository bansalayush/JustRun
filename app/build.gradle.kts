import java.io.FileInputStream
import java.io.IOException
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
}

android {
    namespace = "com.scorpio.distancecalculator"
    compileSdk = 36

    defaultConfig {
        val (name, code) = getVersionNameAndCode()
        applicationId = "com.scorpio.distancecalculator"
        minSdk = 24
        targetSdk = 36
        versionCode = code
        versionName = name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.service)
    implementation(project(":logger"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Kotlin coroutines core library
    implementation(libs.kotlinx.coroutines.core) // Check for the latest version

    // Kotlin coroutines support for Android
    implementation(libs.kotlinx.coroutines.android) // Check for the latest version

    // Lifecycle-aware coroutine scopes (optional but recommended for Android)
    // For ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // Check for the latest version
    // For LiveData builder
    implementation(libs.androidx.lifecycle.livedata.ktx) // Check for the latest version

    implementation(libs.play.services.location)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // Optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)

    implementation(libs.work.runtime.ktx)

    // Jetpack DataStore Preferences
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.timber)
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom("$projectDir/config/detekt.yml")
    baseline = file("$projectDir/config/baseline.xml")
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        html.required.set(true)
        xml.required.set(true)
        txt.required.set(true)
        sarif.required.set(true)
    }
}

ktlint {
    version.set("1.1.1")
    android.set(true)
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
    filter {
        exclude("**/generated/**")
        exclude("**/build/**")
    }
}

fun getVersionNameAndCode(): Pair<String, Int> {
    val versionPropertiesFile = file("version.properties")
    if (versionPropertiesFile.canRead()) {
        val versionProperties = Properties()
        versionProperties.load(FileInputStream(versionPropertiesFile))
        val versionMajor = versionProperties.getProperty("major").toInt()
        val versionMinor = versionProperties.getProperty("minor").toInt()
        val versionBuild = versionProperties.getProperty("build").toInt()

        val versionName = "$versionMajor.$versionMinor.$versionBuild"
        val versionCode = versionMajor * 100000000 + versionMinor * 1000 + versionBuild
        return Pair<String, Int>(versionName, versionCode)
    }
    throw IOException("CAN'T READ VERSION.PROPERTIES")
}

fun updateBuildVersion(): Pair<String, Int> {
    val versionPropertiesFile = file("version.properties")
    if (versionPropertiesFile.canRead()) {
        val versionProperties = Properties()
        versionProperties.load(FileInputStream(versionPropertiesFile))
        val versionMajor = versionProperties.getProperty("major").toInt()
        val versionMinor = versionProperties.getProperty("minor").toInt()
        val versionBuild = versionProperties.getProperty("build").toInt()
        var newVersionBuild: Int
        var newVersionMinor = versionMinor
        if (versionBuild == 999) {
            newVersionBuild = 1
            newVersionMinor = versionMinor + 1
            versionProperties.setProperty("build", newVersionBuild.toString())
            versionProperties.setProperty("minor", newVersionMinor.toString())
        } else {
            newVersionBuild = versionBuild + 1
            versionProperties.setProperty("build", newVersionBuild.toString())
        }
        versionProperties.store(versionPropertiesFile.writer(), null)

        val versionName = "$versionMajor.$versionMinor.$newVersionBuild"
        val versionCode = versionMajor * 100000000 + newVersionMinor * 1000 + newVersionBuild
        return Pair<String, Int>(versionName, versionCode)
    }
    throw IOException("can't read version.properties")
}

tasks.register("bumpVersion") {
    group = "versioning"
    description = "Increment Build version"
    doLast {
        updateBuildVersion()
    }
}
