package cn.bootz.thrift.gradle.plugin

import cn.bootz.thrift.gradle.plugin.task.Metadata
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author tony.zhuby
 * @date 2020/12/1
 */
class ThriftPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        Metadata.values().forEach {
            project.tasks.create(it.taskName, it.clazz)
        }
    }
}