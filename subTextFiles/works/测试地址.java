





任务中心 成长任务
https://cp-h5-jason.test.gifshow.com/task/?layoutType=4&enableWK=1#/quest-centerz
主播任务测试地址：https://cp-h5-jason.test.gifshow.com/anchor-center/?enableWK=1&layoutType=4
https://cp-h5-jason.test.gifshow.com/anchor-task/live/?enableWK=1&layoutType=3#/?from=banner&nav=0
创作者中心
https://cp-h5-yjs1.test.gifshow.com/creator-center/?enableWK=1&layoutType=4

创作者中心   https://cp-h5-yjs2.test.gifshow.com/creator-center/?enableWK=1&layoutType=4
创作者中心-任务中心  https://cp-h5-yjs2.test.gifshow.com/task/?layoutType=4&enableWK=1#/quest-center
主播中心     https://cp-h5-yjs2.test.gifshow.com/anchor-center/?enableWK=1&layoutType=4
游戏        https://node-game-activity-dev3.test.gifshow.com/sf/carnival/activity/X_gameTask/?enableWK=1



kbu    dependency + jar
java -DLOG_HOME=./log/ -cp kuaishou-operation-activity-runner.jar:dependency/* com.kuaishou.infra.databus.client.DatabusMysqlClientStarter --databus-resolver UserProgressFinishedUpdateDataBusResolver --worker-thread 10

OperationActivityFeIndicatorDisplayConfig


if [ $# != 1 ] ; then
	echo "USAGE: 参数格式为1个 1=BeanName"
	echo " e.g.: ./consumer2.sh xxConsumer"
exit 1;
fi

ulimit -SHn 65535
export LC_ALL=en_US.UTF-8
export LANG=en_US.UTF-8
export JAVA_HOME=/opt/java/default
echo $JAVA_HOME
service_name=$1
logs_home=/data/logs/wangqingwei/$service_name
/usr/bin/nohup $JAVA_HOME/bin/java -XX:+AggressiveOpts -XX:+UseStringDeduplication -Xss256k -Xmx8G -Dfile.encoding=UTF-8 -DLOG_HOME=$logs_home -Djava.library.path=/usr/local/    lib -cp     dependency/*: com.kuaishou.runner.rocketmq.RocketMqConsumerStarter  $service_name  &

#!/bin/bash
if [ $# != 1 ] ; then
echo "USAGE: args -> BeanName"
echo " e.g.: ./rpc.sh xxRpcService"
exit 1;
fi
ulimit -SHn 65535
export LC_ALL=en_US.UTF-8
export LANG=en_US.UTF-8
export JAVA_HOME=/opt/java/default
service_name=$1
logs_home=/data/logs/wangqingwei/$service_name
/usr/bin/nohup $JAVA_HOME/bin/java -XX:+AggressiveOpts -XX:+UseStringDeduplication -Xss256k -Xmx2G -Dfile.encoding=UTF-8 -DLOG_HOME=$logs_home -Djava.library.path=/usr/local/    lib -cp dependency/*: com.kuaishou.framework.rpc.server.RpcServerStarter -e $service_name -t 20 --support-server-reflection  &



1.  
如果加入梯度 

那抹表设计

CREATE TABLE `operation_task_group_user_count` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activity_id` bigint(20) unsigned NOT NULL COMMENT '任务id',
  `group_id` bigint(20) unsigned NOT NULL COMMENT '条件组id',
  `count` bigint(20) unsigned NOT NULL COMMENT '完成人数',
  `create_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_activityId` (`activity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4733 DEFAULT CHARSET=utf8mb4 COMMENT='任务梯度完成人数表';


监控operation_activity_user_progress_detail

变化，  status 1->2   维度。activity_id group_id user_id status = 2

before ： status = 1 

after ： status = 2 

if (before != 1 || after != 2) {
	return;
}

// 普通和 策略都可以
count = operationActivityUserProgressDetailService.getActivityIdAndUserIdAndFinished(activityId, userId, 2);
		= 2

	// 相当于用户完成了这个任务
	if (count == 1) {
		这时候，operationUserCount + 1	
	} 


if (sub_id != 0) {
	return;
}
// 只有基础任务 梯度    周期变化
count = operationActivityUserProgressDetailService.getActivityIdAndGroupIdAndUserIdAndFinished(activityId, groupId, userId, 2);
		= 2
	if (count == 1) {
		这时候，operationGroupUserCount + 1
	}






2. es   count 功能

还是 resolver。 监听 userProgressDetail。

insert  ，，， 插入userProgressDetail。 并且插入 es

delete。      一样

update ，     更新即可



在查询梯度的那块，，写一个service 以供从es中楼数据
完成  activityId userId 2 去重
梯度完成，  activityId, groupId, userId 2 去重


去重 功能 由 es实现































