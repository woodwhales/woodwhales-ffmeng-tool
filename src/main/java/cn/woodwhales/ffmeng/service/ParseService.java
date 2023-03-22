package cn.woodwhales.ffmeng.service;

import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.ffmeng.model.ParseParam;
import cn.woodwhales.ffmeng.model.VideoToAudioParam;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;

/**
 * @author woodwhales on 2023-03-22 14:34
 */
@Slf4j
@Service
public class ParseService {

    private String getFFmengFilePath() {
        URL resource = this.getClass().getClassLoader().getResource("ffmpeg.exe");
        return resource.getFile();
    }

    public OpResult<Void> parse(ParseParam param) throws Exception {
        log.info("param={}", JSON.toJSONString(param));
        OpResult<Void> checkOpResult = param.check();
        if(checkOpResult.isFailure()) {
            return checkOpResult;
        }

        String ffmpegFilePath = this.getFFmengFilePath();

        List<ParseParam.CommandDto> commandDtoList = param.convert();
        int index = 1;
        for (ParseParam.CommandDto commandDto : commandDtoList) {
            String finalCommand = commandDto.letFinalCommand(ffmpegFilePath, param, index);
            Process process = Runtime.getRuntime().exec(finalCommand);
            int processResult = process.waitFor();
            log.info("finalCommand = {}, processResult = {}", finalCommand, processResult);
            index++;
        }
        return OpResult.success();
    }

    public OpResult<Void> videoToAudio(VideoToAudioParam param) throws Exception {
        log.info("param={}", JSON.toJSONString(param));
        OpResult<Void> checkOpResult = param.check();
        if(checkOpResult.isFailure()) {
            return checkOpResult;
        }

        String ffmpegFilePath = this.getFFmengFilePath();
        VideoToAudioParam.CommandDto commandDto = param.convert();
        String finalCommand = commandDto.letFinalCommand(ffmpegFilePath);
        Process process = Runtime.getRuntime().exec(finalCommand);
        int processResult = process.waitFor();
        log.info("finalCommand = {}, processResult = {}", finalCommand, processResult);
        return OpResult.success();
    }
}
