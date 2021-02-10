




发送一个kafka 消息

 @OmsActivityLoginNotRequired
    @SuppressWarnings("checkstyle:MagicNumber")
    @PostMapping("/pro")
    public void kafkaProducer(@RequestJsonParam("authorId") long authorId,
            @RequestJsonParam("featureType") int featureType,
            @RequestJsonParam("liveStreamId") long liveStreamId,
            @RequestJsonParam("timeStamp") long time) {
        LiveAuthorFeatureUseInfo message = LiveAuthorFeatureUseInfo.newBuilder()
                .setAuthorId(authorId)
                .setFeatureType(featureType)
                .setLiveStreamId(liveStreamId)
                .setTimestamp(time)
                .build();
        KafkaProducers.sendProto("live_author_feature_use_info_test", message);
        putData("1");
    }

    long authorId = 2107168379;
    int featureType = 1;      // 1 ：魔表  2： 音乐
    long time = 1609381248383；
    long liveStreamId = 1;        // 直播间
    LiveAuthorFeatureUseInfo message = LiveAuthorFeatureUseInfo.newBuilder()
                .setAuthorId(authorId)
                .setFeatureType(featureType)
                .setLiveStreamId(liveStreamId)
                .setTimestamp(time)
                .build();
        KafkaProducers.sendProto("live_author_feature_use_info_test", message);

1. mac for xmind 8 破解
    https://blog.csdn.net/wangletiancsdn/article/details/88990733?utm_medium=distribute.pc_feed_404.none-task-blog-BlogCommendFromBaidu-4.nonecase&depth_1-utm_source=distribute.pc_feed_404.none-task-blog-BlogCommendFromBaidu-4.nonecas




