package cn.woodwhales.ffmeng.service;

import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.ffmeng.model.ParseParam;
import cn.woodwhales.ffmeng.model.VideoToAudioParam;
import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class ParseServiceTest {

    public static void main(String[] args) throws Exception {
        main();
        main2();
    }

    public static void main() throws Exception {
        String json = "{\"srcFileName\":\"让子弹飞.m4a\",\"srcFilePath\":\"D:\\\\movie\",\"destFilePath\":\"D:\\\\movie\",\"timeArrList\":[[\"00:00:12\",\"00:12:36\"],[\"00:12:36\",\"00:24:30\"]]}";
        ParseParam param = JSON.parseObject(json, ParseParam.class);
        FFmengService FFmengService = new FFmengService();
        OpResult<Void> opResult = FFmengService.parse(param);
        System.out.println("opResult = " + JSON.toJSON(opResult));
    }

    public static void main2() throws Exception {
        URL resource = ParseServiceTest.class.getClassLoader().getResource("ffmpeg.exe");
        VideoToAudioParam.CommandDto commandDto = new VideoToAudioParam.CommandDto();
        commandDto.setSrcFile("D:\\movie\\神探亨特张.mp4");
        commandDto.setDestFile("D:\\movie\\神探亨特张.m4a");
        OpResult<List<String>> commandListOpResult = commandDto.getCommandList(resource.getFile());
        FFmengService fFmengService = new FFmengService();
        OpResult<List<String>> execute = fFmengService.execute(commandListOpResult.getData());
        execute.getData().forEach(System.out::println);
    }

}