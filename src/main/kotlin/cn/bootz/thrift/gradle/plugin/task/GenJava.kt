package cn.bootz.thrift.gradle.plugin.task

import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.work.InputChanges
import java.io.File
import javax.inject.Inject

/**
 * @author tony.zhuby
 * @date 2020/12/1
 */
class GenJava @Inject constructor() : ThriftGenTask() {

    init {
        if (project.plugins.hasPlugin(JavaPlugin::class.java)) {
            reg()
        } else {
            project.plugins.whenPluginAdded {
                if (it is JavaPlugin) {
                    reg()
                }
            }
        }
    }


    @TaskAction
    override fun execute(changes: InputChanges) {
        super.execute(changes)
    }


    override val metadata = Metadata.GEN_JAVA

    fun genFolderName(): String {
        if (options.any { it == "beans" }) {
            return "gen-javabean"
        }
        return "gen-java"
    }

    fun reg() {
        val task = project.tasks.withType(JavaCompile::class.java).getByName(JavaPlugin.COMPILE_JAVA_TASK_NAME)
        task.dependsOn(this)
        project.extensions.getByType(SourceSetContainer::class.java)
            .findByName(SourceSet.MAIN_SOURCE_SET_NAME)?.java?.srcDir(
                if (createGenFolder) {
                    File(outDir, genFolderName())
                } else {
                    outDir
                }
            )

    }
}