package me.xiba.commitupload.filetemplate

/**
 * @Author:liukun
 * @Date: 2020-09-23 14:55
 * @Description: gitcommit.properties模板文本
 */
class GitCommitPropertiesTemplate {

    companion object{
        const val GIT_COMMIT_PROPERTIES_TEMPLATE = "#\n" +
                "version=1.0\n" +
                "commitMessage=\"input commit message\"\n" +
                "mainVersion=1.0"
    }
}