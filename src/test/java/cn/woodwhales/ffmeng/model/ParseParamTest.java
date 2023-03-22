package cn.woodwhales.ffmeng.model;

import com.alibaba.fastjson.JSON;

import java.util.List;

class ParseParamTest {

    public static void main(String[] args) {
        String json = "{\"srcFileName\":\"让子弹飞.m4a\",\"srcFilePath\":\"D:\\\\movie\",\"destFilePath\":\"D:\\\\movie\",\"timeArrList\":[[\"00:00:12\",\"00:12:36\"],[\"00:12:36\",\"00:24:30\"]]}";
        ParseParam param = JSON.parseObject(json, ParseParam.class);
        List<ParseParam.CommandDto> commandDtoList = param.convert();
        int index = 1;
        for (ParseParam.CommandDto commandDto : commandDtoList) {
            String finalCommand = commandDto.letFinalCommand("ffmeng.exe", param, index);
            System.out.println("finalCommand = " + finalCommand);
            index++;
        }
        main2(args);
    }

    public static void main2(String[] args) {
        String json = "{\"srcFileName\":\"神探亨特张.mp4\",\"srcFilePath\":\"D:\\\\movie\",\"destFilePath\":\"D:\\\\movie\"}";
        VideoToAudioParam param = JSON.parseObject(json, VideoToAudioParam.class);
        VideoToAudioParam.CommandDto commandDto = param.convert();
        String finalCommand = commandDto.letFinalCommand("ffmeng.exe");
        System.out.println("finalCommand = " + finalCommand);
    }

}