


代理 ：cp-h5-yjs2

自己的 cp.wqw.test.gifshow.com


https://feproxy.corp.kuaishou.com/forward/config/cp-wqw


打runner的包。  需要

mvn -U clean install -pl ./kuaishou-operation-light-runner -am -DskipTests -DCicheck.skip
cd kuaishou-operation-light-runner/
mvn clean package dependency:copy-dependencies -Dmaven.test.skip=true -DskipTests -Dcheckstyle.skip && cp target/kuaishou-operation-activity-runne*.jar target/dependency && zip -r target/dependency.zip target/dependency



java -Xmx2G -cp ./*: com.kuaishou.framework.scheduler.SchedulerTaskStarter --task operationLightUserScoreHistoryRemoveTask --port 9999



测试环境： app的任务界面 https://cp-h5-jason.test.gifshow.com/anchor-center/?enableWK=1&layoutType=4 直接在app内访问




