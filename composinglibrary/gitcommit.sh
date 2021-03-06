#!/bin/bash

# 传递参数

if [ ! $1 ]
then
    echo "########## 请输入提交信息 ##########"
    exit 1;
fi

echo "########## 开始提交 ##########"

echo "commitMessage: $1"

git add -A
git status

#echo "########## 请输入提交信息 ##########"

git commit -m "$1"

if [ $? -ne 0 ]
then
  echo "git commit 错误"
  exit 1
fi

git fetch
if [ $? -ne 0 ]
then
  echo "git fetch 错误"
  exit 1
fi

git rebase
if [ $? -ne 0 ]
then
  echo "git rebase 错误"
  exit 1
fi

git push -u origin
if [ $? -ne 0 ]
then
  echo "git push 错误"
  exit 1
fi

echo "########## 提交结束 ##########"