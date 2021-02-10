

1. 
	public Map<String, Account> getNameAccountMap(List<Account> accounts) {
	    return accounts.stream().collect(Collectors.toMap(Account::getUsername, Function.identity()));
	}


2. https://mqadmin.corp.kuaishou.com/queryMsgDetail.html?env=hb1&topic=operation_activity_message&msgId=0ACE3AE100637F89779D6E79B1A75B8C
	根据topic 和 messageId 拿到message数据

	String source =
                "8 ,18 ,16 ,105 ,24 ,5 ,34 ,3 ,54 ,55 ,55 ,48 ,-39 ,17 ,56 ,-45 ,-53 ,-82 ,-63 ,6 ,66 ,18 ,10 ,11 ,84 ,72 "
                        + ",73 ,82 ,68 ,95 ,82 ,73 ,71 ,72 ,84 ,18 ,3 ,48 ,47 ,49 ,66 ,23 ,10 ,10 ,70 ,73 ,82 ,83 ,84 ,95 ,76 ,69 ,70"
                        + " ,84 ,18 ,9 ,-27 ,-82 ,-116 ,-26 ,-120 ,-112 ,-27 ,-66 ,-105 ,66 ,21 ,10 ,11 ,83 ,69 ,67 ,79 ,78 ,68 ,95 ,76 ,69 ,"
                        + "70 ,84 ,18 ,6 ,-27 ,-68 ,-128 ,-26 ,-110 ,-83 ,66 ,21 ,10 ,15 ,87 ,73 ,68 ,71 ,69 ,84 ,83 ,95 ,86 ,69 ,82 ,83 ,73 ,79 ,78 ,18 ,2 ,49 ,48 ,66 ,21 ,10 ,17 ,87 ,73 ,68 ,71 ,69 ,84 ,83 ,95 ,84 ,73 ,77 ,69 ,83 ,84 ,65 ,77 ,80 ,18 ,0 ,66 ,21 ,10 ,12 ,83 ,69 ,67 ,79 ,78 ,68 ,95 ,82 ,73 ,71 ,72 ,84 ,18 ,5 ,49 ,48 ,47 ,49 ,48 ,66 ,31 ,10 ,11 ,70 ,73 ,82 ,83 ,84 ,95 ,82 ,73 ,71 ,72 ,84 ,18 ,16 ,56 ,-26 ,-84 ,-95 ,-27 ,-82 ,-98 ,-26 ,-105 ,-74 ,-26 ,-75 ,-127 ,-23 ,-121 ,-113 ,66 ,41 ,10 ,10 ,84 ,72 ,73 ,82 ,68 ,95 ,76 ,69 ,70 ,84 ,18 ,27 ,-25 ,-101 ,-76 ,-26 ,-110 ,-83 ,-26 ,-105 ,-74 ,-28 ,-67 ,-65 ,-25 ,-108 ,-88 ,-23 ,-83 ,-108 ,-26 ,-77 ,-107 ,-24 ,-95 ,-88 ,-26 ,-125 ,-123 ,66 ,19 ,10 ,12 ,70 ,79 ,85 ,82 ,84 ,72 ,95 ,82 ,73 ,71 ,72 ,84 ,18 ,3 ,48 ,47 ,49 ,66 ,39 ,10 ,11 ,70 ,79 ,85 ,82 ,84 ,72 ,95 ,76 ,69 ,70 ,84 ,18 ,24 ,-25 ,-101 ,-76 ,-26 ,-110 ,-83 ,-24 ,-65 ,-98 ,-25 ,-70 ,-65 ,-24 ,-127 ,-118 ,-27 ,-92 ,-87 ,-26 ,-84 ,-95 ,-26 ,-107 ,-80 ,66 ,19 ,10 ,14 ,87 ,73 ,68 ,71 ,69 ,84 ,83 ,95 ,83 ,84 ,65 ,84 ,85 ,83 ,18 ,1 ,50 ,66 ,16 ,10 ,12 ,87 ,73 ,68 ,71 ,69 ,84 ,83 ,95 ,84 ,89 ,80 ,69 ,18 ,0 ,74 ,24 ,53 ,49 ,52 ,57 ,95 ,52 ,55 ,48 ,52 ,53 ,49 ,48 ,48 ,49 ,95 ,50 ,51 ,55 ,55 ,50 ,57 ,50 ,51 ,53 ,80 ,1 ,98 ,68 ,104 ,116 ,116 ,112 ,115 ,58 ,47 ,47 ,106 ,115 ,46 ,97 ,46 ,107 ,115 ,112 ,107 ,103 ,46 ,99 ,111 ,109 ,47 ,98 ,115 ,50 ,47 ,97 ,100 ,109 ,105 ,110 ,66 ,108 ,111 ,99 ,107 ,47 ,112 ,108 ,97 ,116 ,102 ,111 ,114 ,109 ,95 ,111 ,112 ,101 ,114 ,97 ,116 ,105 ,111 ,110 ,106 ,68 ,103 ,97 ,74 ,80 ,111 ,120 ,46 ,112 ,110 ,103 ,104 ,10 ,112 ,-94 ,-37 ,-126 ,-64 ,-14 ,46";
        try {
            final List<String> byteList =
                    Arrays.stream(StringUtils.split(source, ",")).map(StringUtils::trim).collect(Collectors.toList());

            byte[] buf = new byte[byteList.size()];
            for (int i = 0; i < byteList.size(); i++) {
                buf[i] = new Byte(byteList.get(i));
            }
            System.out.println(ObjectMapperUtils.toJSON(buf));
            System.out.println(ObjectMapperUtils.toJSON(OperationActivityProgressMsg.parseFrom(buf)));
        } catch (Exception e) {
            e.printStackTrace();
        }

3. 三层循环的改进 打平   

	ListUtils.emptyIfNull(conditionGroupList)
                .stream()
                .map(ConditionGroup::getConditionList)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(x -> x.getIndicator() == OperationIndicatorBasicCodeEnum.UPLOAD_PHOTO_WITH_MAGIC_FACE
                        .getIndicatorId())
                .forEach(condition -> {
                    String magicId = condition.getTemplateParams();
                    if (!NumberUtils.isDigits(magicId)) {
                        throw new AdminApiException(PARAM_VALIDATION_FAILURE, "魔法表情ID必须为数字");
                    }
                    MagicFace magicFace = magicFaceService.getMagicFaceById(Long.parseLong(magicId));
                    if (magicFace == null) {
                        String error = "魔表ID：" + magicId + " 输入有误，请输入正确的魔法表情ID";
                        throw new AdminApiException(PARAM_VALIDATION_FAILURE, error);
                    }
                });
curl 'http://kim-aliyun.internal/kim-api/api/robot/send?key=2fe479b3-75f0-4fff-b760-fc8595ba290b' \ -H 'Content-Type: application/json' \ -d ' {
    "msgtype": "text",
    "text": {
      "content": "hello world"
    }
}'

4. 

select *, from_unixtime(create_time/1000) from operation_activity_settlement_detail where activity_id = 5637  and user_id = 1330929661;





