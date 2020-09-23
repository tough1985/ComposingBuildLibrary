package me.xiba.commitupload.filetemplate

/**
 * @Author:liukun
 * @Date: 2020-09-23 14:45
 * @Description: gitcommit.sh模板文本
 */
class GitCommitShellTemplate {

    companion object{
        const val GIT_COMMIT_SHELL_TEMPLATE = "#!/bin/bash\n" +
                "\n" +
                "# 传递参数\n" +
                "\n" +
                "if [ ! \$1 ]\n" +
                "then\n" +
                "    echo \"########## 请输入提交信息 ##########\"\n" +
                "    exit 1;\n" +
                "fi\n" +
                "\n" +
                "echo \"########## 开始提交 ##########\"\n" +
                "\n" +
                "echo \"commitMessage: \$1\"\n" +
                "\n" +
                "git add -A\n" +
                "git status\n" +
                "\n" +
                "#echo \"########## 请输入提交信息 ##########\"\n" +
                "\n" +
                "git commit -m \"\$1\"\n" +
                "\n" +
                "if [ \$? -ne 0 ]\n" +
                "then\n" +
                "  echo \"git commit 错误\"\n" +
                "  exit 1\n" +
                "fi\n" +
                "\n" +
                "git fetch\n" +
                "if [ \$? -ne 0 ]\n" +
                "then\n" +
                "  echo \"git fetch 错误\"\n" +
                "  exit 1\n" +
                "fi\n" +
                "\n" +
                "git rebase\n" +
                "if [ \$? -ne 0 ]\n" +
                "then\n" +
                "  echo \"git rebase 错误\"\n" +
                "  exit 1\n" +
                "fi\n" +
                "\n" +
                "git push -u origin\n" +
                "if [ \$? -ne 0 ]\n" +
                "then\n" +
                "  echo \"git push 错误\"\n" +
                "  exit 1\n" +
                "fi\n" +
                "\n" +
                "echo \"########## 提交结束 ##########\""
    }
}