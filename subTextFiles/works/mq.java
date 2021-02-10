
系统内部的进度等都是mq ，而外部指标等等都是kafka

1. mqConsumer:

    @Lazy
    @Service
    public class OperationIndicatorRelayConsumer implements MqConsumer {
        @Override
        public ConsumeResult onMessage(MqMessage message, ConsumeContext context) {
            StopWatch watcher = PerfUtils.getWatcher();
            MqConsumeMessage consumeMessage = (MqConsumeMessage) message;
            try {
                OperationIndicatorBufferTrigger bufferTrigger =
                        OperationIndicatorBufferTrigger.parseFrom(consumeMessage.getData());
                return mqSyncSendResult.isSuccess() ? ConsumeResult.SUCCESS : ConsumeResult.LATER;
            } catch (Exception e) {
                LOGGER.error("[callbackToUserProgress] error, messageId = {}",
                        consumeMessage.getMessageId(), e);
                return ConsumeResult.LATER;
            } finally {
                PerfUtils.perf(PerfConstants.NAMESPACE, PerfConstants.SUB_TAG_INDICATOR, "indicatorRelay")
                        .micros(watcher.getTimeMicros()).logstashOnly();
            }
        }
        @Override
        public String consumerGroup() {
            return OPERATION_INDICATOR_RELAY.getConsumerGroup();
        }
        @Override
        public String getLogicTopic() {
            return OPERATION_INDICATOR_RELAY.getTopic();
        }
        @Override
        public BizDef bizDef() {
            return BizDef.OPERATION;
        }
        @Override
        public String appKey() {
            return OPERATION_INDICATOR_RELAY.getAppKey();
        }
    }

    RocketMq
        消息发送： 
        OperationOmsSettlementController
        OperationActivitySettlementServiceImpl.
        confirmSettlement

        MsgProducer producer = RocketMQTopicEnum.producer(RocketMQTopicEnum.OPERATION_ACTIVITY_REWARD);  // topic枚举类有多个topic信息
        MqMessage mqMessage = producer.createMsgBuilder(activityRewardMsg).build();
        producer.sendSync(mqMessage);

        消息接受： 
        OperationMessageSendMsgConsumer
        onMessage 调用
        OperationMessageServiceImpl.sendMessage
        私信 站内消息等等



2. kafka consumer: 
    @Lazy
    @Service
    public class OperationIndicatorLiveStreamPkReadyConsumer implements KsKafkaConsumer<LivePkDto> {

        private static final Logger LOGGER = LoggerFactory.getLogger(OperationIndicatorLiveStreamPkReadyConsumer.class);
        private static final RateLogger RATELOGGER = RateLogger.rateLogger(LOGGER);

        @Override
        public void consume(LivePkDto message, MessageContext context) {
            StopWatch watcher = PerfUtils.getWatcher();
            try {
            } finally {
                PerfUtils.perf(PerfConstants.NAMESPACE, PerfConstants.SUB_TAG_INDICATOR, "liveStreamPkReady")
                        .micros(watcher.getTimeMicros()).logstashOnly();
            }
        }
        @Override
        public String topic() {
            return LIVE_PK_READY.getTopicName();
        }
        @Override
        public String consumerGroup() {
            return debugHost() ? OPERATION_ACTIVITY_LIVE_PK_READY_GROUP_TEST : OPERATION_ACTIVITY_LIVE_PK_READY_GROUP;
        }
        @Override
        public LivePkDto decode(byte[] data) {
            return ObjectMapperUtils.fromJSON(data, LivePkDto.class);
        }
        @Nonnull
        @Override
        public BizDef bizDef() {
            return BizDef.OPERATION;
        }
    }






【任务】--报警群
229b0f04-5510-4b38-930f-929aaa5e1ae5

任务： 


可以看一下rocketmq
看一下结算和 奖励的consumer 还有相关的业务吧
看数据库表的关系哦




private List<OperationLightGradeCacheDTO> getGradeCacheDTOList() {
        List<OperationLightGrade> allGrade = operationLightGradeService.getAllList();
        return allGrade.stream().map(grade -> {
            OperationLightGradeCacheDTO cacheDTO = new OperationLightGradeCacheDTO();
            cacheDTO.setGrade(grade.getGrade());
            cacheDTO.setGradeName(grade.getGradeName());
            cacheDTO.setMinScore(grade.getMinScore());
            cacheDTO.setMaxScore(grade.getMaxScore());
            cacheDTO.setIcon(grade.getIcon());
            return cacheDTO;
        }).collect(Collectors.toList());
    }

    private List<OperationLightRightCacheDTO> getRightCacheDTOList() {
        List<OperationLightRight> allRights = operationLightRightService.getAll();
        Map<Integer, List<OperationLightRightStatus>> allRightStatus =
                operationLightRightStatusService.getAll();
        return allRights.stream().map(right -> {
            int rightId = right.getRight();
            List<OperationLightRightStatus> rightStatusList =
                    allRightStatus.getOrDefault(rightId, Collections.emptyList());

            OperationLightRightCacheDTO cacheDTO = new OperationLightRightCacheDTO();
            cacheDTO.setRight(rightId);
            cacheDTO.setName(right.getName());
            cacheDTO.setStatus(right.getStatus());
            cacheDTO.setLink(right.getLink());
            cacheDTO.setDescription(right.getDescription());
            cacheDTO.setSort(right.getSort());
            cacheDTO.setStatusCacheDTOS(rightStatusList.stream().map(rightStatus -> {
                OperationLightRightStatusCacheDTO statusCacheDTO = new OperationLightRightStatusCacheDTO();
                statusCacheDTO.setIcon(rightStatus.getIcon());
                statusCacheDTO.setStatus(rightStatus.getStatus());
                return statusCacheDTO;
            }).collect(Collectors.toList()));
            return cacheDTO;
        }).sorted(Comparator.comparingInt(OperationLightRightCacheDTO::getSort))
                .collect(Collectors.toList());
    }






















