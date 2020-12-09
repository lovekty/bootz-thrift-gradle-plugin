package cn.bootz.thrift.gradle.plugin.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.work.InputChanges
import java.io.File

/**
 * @author tony.zhuby
 * @date 2020/12/9
 */
abstract class ThriftGenTask : DefaultTask() {

    init {
        group = "thrift"
    }

    @InputDirectory
    var idlDir = project.file("${project.projectDir}/src/main/thrift")

    @OutputDirectory
    var outDir = project.file("${project.buildDir}/generated-sources/thrift")

    @Input
    var thriftExecutable = "thrift"

    @Input
    var createGenFolder = false

    @Input
    var includes = setOf<File>()

    @Input
    var nowarn = false

    @Input
    var strict = false

    @Input
    var verbose = false

    @Input
    var recurse = false

    @Input
    var debug = false

    @Input
    var allowNegKeys = false

    @Input
    var allow64bitsConsts = false

    @Input
    var options = setOf<String>()

    abstract val metadata: Metadata

    open fun execute(changes: InputChanges) {
        val thriftFiles = mutableSetOf<String>()
        if (changes.isIncremental) {
            changes.getFileChanges(project.files(idlDir))
                .forEach { thriftFiles.addAll(collect(it.file)) }
        } else {
            thriftFiles.addAll(collect(idlDir))
        }
        compile(thriftFiles)
    }

    fun assembleCmd(): MutableList<String> {
        val ret = mutableListOf<String>()
        ret += thriftExecutable
        ret += if (createGenFolder) {
            "-o"
        } else {
            "-out"
        }
        ret += outDir.absolutePath
        includes.forEach {
            ret += "-I"
            ret += it.absolutePath
        }
        if (nowarn) {
            ret += "-nowarn"
        }
        if (strict) {
            ret += "-strict"
        }
        if (verbose) {
            ret += "-v"
        }
        if (recurse) {
            ret += "-r"
        }
        if (debug) {
            ret += "-debug"
        }
        if (allowNegKeys) {
            ret += "--allow-neg-keys"
        }
        if (allow64bitsConsts) {
            ret += "--allow-64bit-consts"
        }
        ret += "--gen"
        ret += if (options.isEmpty()) {
            metadata.language
        } else {
            metadata.language + ":" + options.joinToString(separator = ",")
        }
        return ret
    }

    private fun compile(sources: Set<String>) {
        sources.forEach { compile(it) }
    }

    private fun compile(source: String) {
        val cmd = assembleCmd()
        cmd += source
        project.exec { it.commandLine(cmd) }.assertNormalExitValue()
    }

    private fun collect(file: File): Set<String> {
        if (file.isFile && file.name.endsWith(".thrift")) {
            return setOf(file.absolutePath)
        } else if (file.isDirectory) {
            return project.fileTree(file) { it.include("**/*.thrift") }
                .map { it.absolutePath }
                .toHashSet()
        }
        return setOf()
    }
}