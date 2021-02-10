


sql 优化

1. sql优化   500ms 以上的 优化

select activity_id,count(distinct user_id) as num from operation_activity_user_progress where activity_id in (?,) and finished > ? group by activity_id;


2.
select * from operation_activity_settlement_bill where activity_id = ? and id > ? and settlement_status = ? and send_status = ? order by id limit ?



3. select activity_id,count(*) from operation_activity_settlement_bill group by activity_id order by activity_id desc
暂不优化，已经是最优的  , dao内没有


4. select count(*) from operation_activity_user_progress_item where activity_id = ?  



explain select count(distinct user_id) from operation_activity_user_progress where activity_id=4880 and finished>0;


OperationActivityLiveStreamUserProgressConsumer



    private final Logger logger = LoggerFactory.getLogger(OperationActivityLiveStreamUserProgressConsumer.class);

    private final Kconf<Boolean> filterLiveStreamUserProgressRandom =
            Kconfs.ofBoolean("operation.activity.filterLiveStreamUserProgressRandom", true).build();

    private BufferTrigger<OperationActivityUserIndicatorProgressDTO> bufferTrigger;
    @Resource
    private OperationActivityUserProgressProcessService operationActivityUserProgressProcessService;

    @PostConstruct
    public void initBufferTrigger() {
        bufferTrigger = BufferTrigger.<OperationActivityUserIndicatorProgressDTO> batchBlocking()
                .bufferSize(OperationActivityIntegerKconf.BUFFER_TRIGGER_BUFFER_SIZE.get())
                .batchSize(OperationActivityIntegerKconf.BUFFER_TRIGGER_BATCH_SIZE.get())
                .linger(() -> Duration.ofSeconds(OperationActivityIntegerKconf.BUFFER_TRIGGER_DURATION.get()))
                .setConsumerEx(this::processProgressList)
                .build();
        TermHelper.addTerm(() -> {
            bufferTrigger.manuallyDoTrigger();
            logger.info(
                    "[OperationActivityLiveStreamUserProgressConsumer] bufferTrigger closed, manual do the trigger");
        });
    }

    private void processProgressList(List<OperationActivityUserIndicatorProgressDTO> progressDTOList) {
        Map<OperationActivityUserIndicatorProgressUniqKey, List<OperationActivityUserIndicatorProgressDTO>> dtoMap =
                ListUtils.emptyIfNull(progressDTOList).stream().collect(
                        Collectors.groupingBy(
                                OperationActivityUserIndicatorProgressDTO::generateActivityUserIndicatorProgressUniqueKey));
        MapUtils.emptyIfNull(dtoMap).forEach((uniqKey, dtoList) -> {
            OperationActivityUserIndicatorProgressDTO maxDTO = null;
            long maxActionValue = 0L;
            for (OperationActivityUserIndicatorProgressDTO dto : dtoList) {
                if (dto.getActionValue() > maxActionValue) {
                    maxDTO = dto;
                    maxActionValue = dto.getActionValue();
                }
            }
            if (maxDTO != null) {
                try {
                    operationActivityUserProgressProcessService.processProgress(maxDTO);
                } catch (Exception e) {
                    logger.error(
                            "[OperationActivityLiveStreamUserProgressConsumer] consumer error(bufferTrigger), will "
                                    + "reSend {}", ObjectMapperUtils.toJSON(maxDTO), e);
                    operationActivityUserProgressProcessService
                            .reSendProgressMsg(OPERATION_ACTIVITY_LIVE_STREAM_PROGRESS,
                                    maxDTO.generateProgressMessage());
                }
            }
        });
    }