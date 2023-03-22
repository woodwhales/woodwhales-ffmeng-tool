package cn.woodwhales.ffmeng.service;

import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.ffmeng.model.ParseParam;
import com.alibaba.fastjson.JSON;

class ParseServiceTest {

    public static void main(String[] args) throws Exception {
        String json = "{\"srcFileName\":\"让子弹飞.m4a\",\"srcFilePath\":\"D:\\\\movie\",\"destFilePath\":\"D:\\\\movie\",\"timeArrList\":[[\"00:00:12\",\"00:12:36\"],[\"00:12:36\",\"00:24:30\"]]}";
        ParseParam param = JSON.parseObject(json, ParseParam.class);
        ParseService parseService = new ParseService();
        OpResult<Void> opResult = parseService.parse(param);
        System.out.println("opResult = " + JSON.toJSON(opResult));
    }

}