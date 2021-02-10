



2021
02-08:
    1. 制造数据，photoRewardConsumer
02-08:
    1. 申请kbus
    2. 上线resolver，打开开关，校验数据


01-20
    1. 上午看一会书，索引这方面
    2. 下午，笔记+文章

01-17：
    1. 修改流量直投的BoostExtendDetailConsumer 中的remark 添加extParams   ok
    2. 对粉条流量直投进行总结， 开发很慢。 问题，原因，解决方法，实际行动。        
    3. redis抖动的真正原因，可以总结
    4. mysql精通，索引。慢工出细活。




2020

怎样去熟悉精通mysql


12-21:
	
任务  +  看书/ rocketmq

感觉各个模块解耦靠mq




12-19：

1. 
	所有的存储引擎都以自己的方式显现了锁机制，还有事务

2. 
	lock table/ unlock table 是在服务层实现的 
	而行级锁，在存储引擎内实现

3. 
	锁只有在commit 或者 rollback时才会被释放，并且所有的锁都是在同一时刻被释放的 

4.
	修改表的列的默认值，很高效
	alter table db.stable
	alter column rental_duration set default 5;

	下面这个是不搞笑的，需要copy表结构，修改，和数据
	alter table db.stable 
	modify column rental_duration tinyint(3) not null default 5;

5. 	索引：
	对于索引列的值比较长，那么存储的时候，叶子结点大，索引效果不太好。
	解决：	
		hash索引，p148页  对所要条件使用 hash函数 f()    但是维护成本很高
		前缀索引： p156页

	对于多列索引，我们查询时怎样使性能更高呢
	将选择性最高的列放到索引的最前列。
	选择性： 
		select count(distinct cosumer_id)/count(cosumer_id) from table;
		指越大，选择性越高








校外毕设题目： 学生任务管理系统的设计与实现

对于学生任务管理系统的设计与实现这个课题，我有一下两个考虑：
1. 我在学校上课的时候，发现每次提交作业时，班长催促大家提交作业，每个班作业打成一个压缩包交给qq发给老师/发送到老师邮箱，但是这种增加了老师的工作量，老师需要去查看哪位同学提交了，哪位同学没提交，再次去问班长，这样增加了老师和班长的工作量。
所以想去设计一个学生任务型系统。

2. 我在公司也是做任务系统的，我想把任务系统赋予学生和老师的场景，去做一个毕设。

以上就是我对与我的校外毕设：学生任务管理系统的设计与实现的认识和思考。
您看这个毕设题目可以吗？










12-18：
一会可以看一下 kakfa简单的架构设计，
明天可以看高性能mysql









                // 将 流量券和免费券转换成元
                double totalRmb = 0;
                int rewardTypeValue = 0;
                long rewardCount = 0;
                OperationSettlementLiveFreeExtParams liveFreeExtParams = settlementExtParams.getLiveFree();
                if (liveFreeExtParams != null) {
                    totalRmb += liveFreeExtParams.getTotalFree();
                    rewardTypeValue = 2;
                    rewardCount = 0;
                }
                OperationSettlementTrafficFreeExtParams trafficFreeExtParams = settlementExtParams.getTrafficFree();
                if (trafficFreeExtParams != null) {
                    totalRmb += trafficFreeExtParams.getTotalFree() * 1d / 1000 * 18;
                    rewardTypeValue = 1;
                }
                OperationSettlementRMBExtParams rmbExtParams = settlementExtParams.getRmb();
                if (rmbExtParams != null) {
                    totalRmb += BigDecimal.valueOf(rmbExtParams.getTotalRMB() * 1d / 100)
                            .setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    rewardTypeValue = 5;
                }

                OperationSettlementKBExtParams kbExtParams = settlementExtParams.getKb();
                if (kbExtParams != null) {
                    totalRmb += BigDecimal.valueOf(kbExtParams.getTotalKB() * 1d / 10)
                            .setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    rewardTypeValue = 4;
                }

                OperationSettlementBoostExtParams boostExtParams = settlementExtParams.getBoost();
                if (boostExtParams != null) {
                    totalRmb += BigDecimal.valueOf(boostExtParams.getTotalCost() * 1d / 100)
                            .setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                    rewardTypeValue = 8;
                }
                OperationSettlementLiveAuthorEnsureExtParams liveAuthorEnsure =
                        settlementExtParams.getLiveAuthorEnsure();
                if (liveAuthorEnsure != null) {
                    totalRmb += BigDecimal.valueOf(liveAuthorEnsure.getTotalCost() * 1d / 1000 * 18)
                            .setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                }
                data.setRmb(RMB.format(totalRmb));


   private String buildSettlementValue(OperationSettlementExtParams extParams) {
        if (extParams == null) {
            return "";
        }
        OperationSettlementLiveFreeExtParams liveFree = extParams.getLiveFree();
        OperationSettlementTrafficFreeExtParams trafficFree = extParams.getTrafficFree();
        OperationSettlementProportionExtParams proportion = extParams.getProportion();
        OperationSettlementKBExtParams kb = extParams.getKb();
        OperationSettlementRMBExtParams rmb = extParams.getRmb();
        OperationSettlementLightExtParams light = extParams.getLight();
        OperationSettlementBoostExtParams boost = extParams.getBoost();
        OperationSettlementFlameExtParams flame = extParams.getFlame();
        OperationSettlementLiveAuthorEnsureExtParams liveAuthorEnsure = extParams.getLiveAuthorEnsure();

        List<String> settlementValues = Lists.newArrayList();
        if (liveFree != null) {
            settlementValues.add(liveFree.getTotalFree() + OperationRewardCategoryEnum.LIVE_FREE.getRewardTypeDesc());
        }
        if (trafficFree != null) {
            settlementValues
                    .add(trafficFree.getTotalFree() + OperationRewardCategoryEnum.TRAFFIC_FREE.getRewardTypeDesc());
        }
        if (proportion != null) {
            settlementValues.add("下发" + proportion.getTotalUser() + "次额外分成奖励");
        }
        if (kb != null) {
            settlementValues.add(kb.getTotalKB() + OperationRewardCategoryEnum.KB.getRewardTypeDesc());
        }
        if (rmb != null) {
            settlementValues.add(rmb.getTotalRMB() / 100 + OperationRewardCategoryEnum.RMB.getRewardTypeDesc());
        }
        if (light != null) {
            settlementValues.add(light.getTotalScore() + OperationRewardCategoryEnum.LIGHT.getRewardTypeDesc());
        }
        if (boost != null) {
            settlementValues.add(boost.getTotalCost() + OperationRewardCategoryEnum.BOOST.getRewardTypeDesc());
        }
        if (liveAuthorEnsure != null) {
            settlementValues.add(liveAuthorEnsure.getTotalCost() + LIVE_AUTHOR_ENSURE_TRAFFIC.getRewardTypeDesc());
        }
        return Joiner.on(",").join(settlementValues);
    }
  data.setUserCnt(settlementExtParams.getUserCnt());

                // 将 流量券和免费券转换成元
                double totalRmb = 0;
                OperationSettlementLiveFreeExtParams liveFreeExtParams = settlementExtParams.getLiveFree();
                if (liveFreeExtParams != null) {
                    totalRmb += liveFreeExtParams.getTotalFree();
                }
                OperationSettlementTrafficFreeExtParams trafficFreeExtParams = settlementExtParams.getTrafficFree();
                if (trafficFreeExtParams != null) {
                    totalRmb += trafficFreeExtParams.getTotalFree() * 1d / 1000 * 18;
                }
                OperationSettlementRMBExtParams rmbExtParams = settlementExtParams.getRmb();
                if (rmbExtParams != null) {
                    totalRmb += BigDecimal.valueOf(rmbExtParams.getTotalRMB() * 1d / 100)
                            .setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                }

                OperationSettlementKBExtParams kbExtParams = settlementExtParams.getKb();
                if (kbExtParams != null) {
                    totalRmb += BigDecimal.valueOf(kbExtParams.getTotalKB() * 1d / 10)
                            .setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                }

                OperationSettlementBoostExtParams boostExtParams = settlementExtParams.getBoost();
                if (boostExtParams != null) {
                    totalRmb += BigDecimal.valueOf(boostExtParams.getTotalCost() * 1d / 100)
                            .setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                }
                OperationSettlementLiveAuthorEnsureExtParams liveAuthorEnsure =
                        settlementExtParams.getLiveAuthorEnsure();
                if (liveAuthorEnsure != null) {
                    totalRmb += BigDecimal.valueOf(liveAuthorEnsure.getTotalCost() * 1d / 1000 * 18)
                            .setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                }
                data.setRmb(RMB.format(totalRmb));

   private String buildSettlementValue(OperationSettlementExtParams extParams) {
        if (extParams == null) {
            return "";
        }
        OperationSettlementLiveFreeExtParams liveFree = extParams.getLiveFree();
        OperationSettlementTrafficFreeExtParams trafficFree = extParams.getTrafficFree();
        OperationSettlementProportionExtParams proportion = extParams.getProportion();
        OperationSettlementKBExtParams kb = extParams.getKb();
        OperationSettlementRMBExtParams rmb = extParams.getRmb();
        OperationSettlementLightExtParams light = extParams.getLight();
        OperationSettlementBoostExtParams boost = extParams.getBoost();
        OperationSettlementFlameExtParams flame = extParams.getFlame();
        OperationSettlementLiveAuthorEnsureExtParams liveAuthorEnsure = extParams.getLiveAuthorEnsure();

        List<String> settlementValues = Lists.newArrayList();
        if (liveFree != null) {
            settlementValues.add(liveFree.getTotalFree() + OperationRewardCategoryEnum.LIVE_FREE.getRewardTypeDesc());
        }
        if (trafficFree != null) {
            settlementValues
                    .add(trafficFree.getTotalFree() + OperationRewardCategoryEnum.TRAFFIC_FREE.getRewardTypeDesc());
        }
        if (proportion != null) {
            settlementValues.add("下发" + proportion.getTotalUser() + "次额外分成奖励");
        }
        if (kb != null) {
            settlementValues.add(kb.getTotalKB() + OperationRewardCategoryEnum.KB.getRewardTypeDesc());
        }
        if (rmb != null) {
            settlementValues.add(rmb.getTotalRMB() / 100 + OperationRewardCategoryEnum.RMB.getRewardTypeDesc());
        }
        if (light != null) {
            settlementValues.add(light.getTotalScore() + OperationRewardCategoryEnum.LIGHT.getRewardTypeDesc());
        }
        if (boost != null) {
            settlementValues.add(boost.getTotalCost() + OperationRewardCategoryEnum.BOOST.getRewardTypeDesc());
        }
        if (liveAuthorEnsure != null) {
            settlementValues.add(liveAuthorEnsure.getTotalCost() + LIVE_AUTHOR_ENSURE_TRAFFIC.getRewardTypeDesc());
        }
        return Joiner.on(",").join(settlementValues);
    }

    private String buildSettlementValue(OperationSettlementExtParams extParams) {
        if (extParams == null) {
            return "";
        }
        OperationSettlementLiveFreeExtParams liveFree = extParams.getLiveFree();
        OperationSettlementTrafficFreeExtParams trafficFree = extParams.getTrafficFree();
        OperationSettlementProportionExtParams proportion = extParams.getProportion();
        OperationSettlementKBExtParams kb = extParams.getKb();
        OperationSettlementRMBExtParams rmb = extParams.getRmb();
        OperationSettlementLightExtParams light = extParams.getLight();
        OperationSettlementBoostExtParams boost = extParams.getBoost();
        OperationSettlementLiveAuthorEnsureExtParams liveAuthorEnsure = extParams.getLiveAuthorEnsure();

        List<String> settlementValues = Lists.newArrayList();
        if (liveFree != null) {
            settlementValues.add(liveFree.getTotalFree() + OperationRewardCategoryEnum.LIVE_FREE.getRewardTypeDesc());
        }
        if (trafficFree != null) {
            settlementValues
                    .add(trafficFree.getTotalFree() + OperationRewardCategoryEnum.TRAFFIC_FREE.getRewardTypeDesc());
        }
        if (proportion != null) {
            settlementValues.add("下发" + proportion.getTotalUser() + "次额外分成奖励");
        }
        if (kb != null) {
            settlementValues.add(kb.getTotalKB() + OperationRewardCategoryEnum.KB.getRewardTypeDesc());
        }
        if (rmb != null) {
            settlementValues.add(rmb.getTotalRMB() / 100 + OperationRewardCategoryEnum.RMB.getRewardTypeDesc());
        }
        if (light != null) {
            settlementValues.add(light.getTotalScore() + OperationRewardCategoryEnum.LIGHT.getRewardTypeDesc());
        }
        if (boost != null) {
            settlementValues.add(boost.getTotalCost() + OperationRewardCategoryEnum.BOOST.getRewardTypeDesc());
        }
        if (liveAuthorEnsure != null) {
            settlementValues.add(liveAuthorEnsure.getTotalCost() + LIVE_AUTHOR_ENSURE_TRAFFIC.getRewardTypeDesc());
        }
        return Joiner.on(",").join(settlementValues);
    }