package dev.shibasis.dependeasy

import dev.shibasis.dependeasy.tasks.logFrameworkSize
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension
import java.io.File

open class DependeasyExtension {
    // Living on the edge
    val annotations = listOf(
        "kotlin.js.ExperimentalJsExport",
        "kotlinx.cinterop.ExperimentalForeignApi",
        "kotlinx.cinterop.BetaInteropApi",
        "kotlin.ExperimentalStdlibApi",
        "kotlinx.coroutines.DelicateCoroutinesApi",
        "androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi"
    )

    companion object {
        @JvmStatic
        fun create(project: Project) = project.extensions.create("dependeasy", DependeasyExtension::class.java)

        @JvmStatic
        fun get(project: Project) = project.extensions.getByName("dependeasy") as DependeasyExtension
    }
}

internal inline fun <reified T : Any> Any.getExtension(name: String): T? =
    (this as ExtensionAware).extensions.getByName(name) as T?

fun Project.applyMultiplatformPlugins(dependeasyExtension: DependeasyExtension) {
    plugins.apply("kotlin-multiplatform")
    val multiplatform = extensions.getByName("kotlin") as KotlinMultiplatformExtension
    multiplatform.sourceSets.all {
        dependeasyExtension.annotations.forEach { languageSettings.optIn(it) }
    }

    val enableDarwin = project.properties["enableDarwin"]?.toString()?.toBoolean() ?: true
    if (enableDarwin) {
        plugins.apply("org.jetbrains.kotlin.native.cocoapods")
        val cocoapods = multiplatform.getExtension<CocoapodsExtension>("cocoapods")
    }
}

class Dependeasy: Plugin<Project> {
    override fun apply(project: Project): Unit = project.run {
        plugins.apply("kotlinx-serialization")
//        plugins.apply("com.google.firebase.crashlytics")
        project.tasks.register("buildReleaseBinaries") { logFrameworkSize() }

        if (project.plugins.hasPlugin("com.android.application")) {
            return
        } else {
            val extension = DependeasyExtension.create(this)
            applyMultiplatformPlugins(extension)
        }

    }
}

