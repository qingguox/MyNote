





2021 01-17
流量直投结束我的思考

1. 首先在交流和设计方案方面，很弱，没有一个完整的设计方案，从需求来源就有问题，开发过程中存在需求不完整，没有想到对其他业务的影响。

	如果影响之后，怎么办，推动去处理。

2. cr方面， 必须提升自己的代码clean code能力，太差了。  集合类stream collect(Collector.toMap(Class::, this.cc) 
	还有对魔数, 抽出一些公共代码（爆黄，这就可以优化了）


2021 01-19 
作品带魔表我的思考：  基础指标 -> 模板指标 -> 动态指标
1. 方向错误，下一次先考虑清楚，简单写个方案，然后让大哥们看一下。再行动

2. 魔表的初始化有问题，因为他是template指标，所以template——id=0 而 template_pre_id = 3（基础指标 发视频）


 List<IndicatorDetailCacheDTO> dynamicIndicatorList =
                    CollectionUtils.emptyIfNull(operationIndicatorCacheService
                            .getIndicatorItemListByPreIndicator(context.getCode().getIndicatorId()))
                            .stream().filter(composePredicateForMagicFaceId(context))
                            .collect(Collectors.toList());
拿到的动态指标，在 OperationIndicatorCacheServiceImpl generatePreIndicatorListMapping初始化本地缓存的时候，就过滤了templateid=0 也就是模板指标和基础指标。剩下的都是动态指标，然后composePredicateForMagicFaceId拿到自己需要的动态指标。

下面就是for 然后根据动态指标id拿去相关联的注册任务。然后推到进度
List<IndicatorRegisterCacheDTO> registerCacheDTOList = operationIndicatorCacheService
                                .getIndicatorRegisterList(itemCacheDTO.getIndicatorId(), context.getUserId());




\{"userId":2199753384,"businessId":"5839","indicatorId":32,"templateId":0,"cycleId":408824153,"actionTime":1612451508920,"actionValue":3,"actionDate":20210204,"category":0,"cycleStartTime":0,"cycleEndTime":0,"subId":1010024154,"extParams":{}}
