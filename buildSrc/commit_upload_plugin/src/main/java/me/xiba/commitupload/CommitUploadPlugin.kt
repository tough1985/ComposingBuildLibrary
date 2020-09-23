package me.xiba.commitupload

import me.xiba.commitupload.filetemplate.GitCommitPropertiesTemplate.Companion.GIT_COMMIT_PROPERTIES_TEMPLATE
import me.xiba.commitupload.filetemplate.GitCommitShellTemplate.Companion.GIT_COMMIT_SHELL_TEMPLATE
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import java.io.*
import java.util.*

/**
 * @Author:liukun
 * @Date: 2020-09-23 11:29
 * @Description: 自动提交代码，生成版本号，提交到maven
 */
class CommitUploadPlugin : Plugin<Project> {

    companion object{
        const val FILENAME_GIT_COMMIT_SHELL = "gitcommit.sh"
        const val FILENAME_GIT_COMMIT_PROPERTIES = "gitcommit.properties"

        const val TASKNAME_COMMIT_UPLOAD = "commitUpload"

        const val PROPERTIES_KEY_COMMIT_MESSAGE = "commitMessage"
    }

    override fun apply(project: Project) {
        println("CommitUploadPlugin: ---------------------")

        // 判断是否有gitcommit.sh，如果没有创建文件
        var gitCommitFile = File("${project.projectDir}/$FILENAME_GIT_COMMIT_SHELL")
        checkAndGenerateFile(gitCommitFile, GIT_COMMIT_SHELL_TEMPLATE, FILENAME_GIT_COMMIT_SHELL)

        // 判断是否有gitcommit.properties，如果没有创建文件
        var gitCommitProperties = File("${project.projectDir}/$FILENAME_GIT_COMMIT_PROPERTIES")
        checkAndGenerateFile(gitCommitProperties, GIT_COMMIT_PROPERTIES_TEMPLATE, FILENAME_GIT_COMMIT_PROPERTIES)

        // 创建提交任务
        createCommitTask(project)

    }

    /**
     * 创建提交任务
     * 使用命令行执行shell文件
     */
    private fun createCommitTask(project: Project){
        var task = project.tasks.create("${project.name}_$TASKNAME_COMMIT_UPLOAD", Exec::class.java)
        // 任务的Group
        task.group = "CommitAndUpload"
        // 创建输出流，用来读取命令行的输出
        var out = ByteArrayOutputStream()

        // 执行前的设置
        task.doFirst {

            // 读取properties文件，获取commitMessage
            var gitCommitPropertiesFile = File("${project.projectDir}/$FILENAME_GIT_COMMIT_PROPERTIES")
            // 创建一个Properties
            var gitCommitProperties = Properties()

            // 如果文件存在 读取文件内容到gitCommitProperties
            if (gitCommitPropertiesFile.exists()){
                // 避免中文乱码
                gitCommitProperties.load(
                        InputStreamReader(gitCommitPropertiesFile.inputStream(), "UTF-8"))
            } else {
                println("You need create composing.properties file in your project dir!")
            }

            // 获取commitMessage
            var commitMessage = gitCommitProperties.getProperty(PROPERTIES_KEY_COMMIT_MESSAGE)

            // 设置工作目录
            task.workingDir(project.projectDir)
            // 设置非0 依然正常运行
            task.isIgnoreExitValue = true
            // 设置命令行输出
            task.standardOutput = out
            // 执行提交脚本
            task.commandLine("bash", FILENAME_GIT_COMMIT_SHELL, commitMessage)
        }

        // 执行后的设置
        task.doLast {
            // 获取退出值
            println("CommitUploadPlugin: ${project.name}_$TASKNAME_COMMIT_UPLOAD : 执行结果: ${task.execResult?.exitValue}")
            // 获取命令行的输出
            println("CommitUploadPlugin: ${project.name}_$TASKNAME_COMMIT_UPLOAD : 执行输出: \n\n${out.toString("UTF-8")}")

            // 如果脚本返回非0，异常退出
            if (task.execResult?.exitValue != 0){
                throw Exception("exitValue not 0")
            }
        }
    }

    /**
     * 检测文件是否存在，如果不存在，用模板生成文件
     */
    private fun checkAndGenerateFile(file: File, template: String, fileName: String){

        // 判断文件是否存在，如果不存在，使用模板创建文件
        if (!file.exists()){

            println("CommitUploadPlugin: 正在创建${fileName}文件")

            // 使用模板创建文件
            file.createNewFile()

            // 为了避免中文乱码
            var gitCommitPropertiesFileWriter = BufferedWriter(
                    OutputStreamWriter(
                            file.outputStream(), "UTF-8"))

            gitCommitPropertiesFileWriter.write(template)
            gitCommitPropertiesFileWriter.flush()
            gitCommitPropertiesFileWriter.close()
        }
    }
}