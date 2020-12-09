package cn.bootz.thrift.gradle.plugin.task

/**
 * @author tony.zhuby
 * @date 2020/12/9
 */
enum class Metadata(
    val clazz: Class<out ThriftGenTask>,
    val taskName: String,
    val language: String
) {

    GEN_JAVA(GenJava::class.java, "thriftGenJava", "java")
}