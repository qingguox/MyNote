








学习git
1. 重要 从 git 或者 GitHub上拉下来一个库
	git config --local user.name "wangqingwei"
	git config --local user.email "wangqingwei@kuaishou.com"

	github  others/gitPro
	git config --local user.name "1367379258"
	git config --local user.email "1367379258@qq.com"

	git config --list

	或者直接  git init ko 直接创建ko库，同时会创建一个.git 文件
	我们进入 cd .git  下有 config ，我们配置上面的local 或者 git config -e # 针对当前仓库 

	



2. cr： 
git pull orgin master 

然后 mvn clean package 
发cr
git push origin master 


post-review 
http://mmms/r/375090/1

rbt post -r 375090(上一个版本号) --parent=origin/master  

http://mmms/r/375090/1-2



post-review -u


1. 合并分支 

首先保证 自己分支 git push 
然后把master 拉下来 
再切到自己分支，合并master。git merge master 

然后再到master分支，合并字节分支。git merge 自己分支

然后再master上 git push


构建 master。在pipline上快速构建 

然后在容器云上上线


cd kuaishou-runn/log

tailf main.log

#!/bin/bash
if [ $# != 1 ] ; then
	echo "USAGE: 参数格式为1个 1=BeanName"
	echo " e.g.: ./kafkaConsumer.sh xxBinlog"
	exit 1;
fi

ulimit -SHn 65535
export LC_ALL=en_US.UTF-8
export LANG=en_US.UTF-8
export JAVA_HOME=/opt/java/default
echo $JAVA_HOME
service_name=$1
logs_home=/data/logs/wangqingwei/$service_name
/usr/bin/nohup $JAVA_HOME/bin/java -XX:+AggressiveOpts -XX:+UseStringDeduplication -Xss256k -Xmx8G -Dfile.encoding=UTF-8 -DLOG_HOME=$logs_home -Djava.library.path=/usr/local/lib -cp     dependency/*: com.kuaishou.runner.kafka.KafkaSingleProcessConsume    r  $service_name  &
