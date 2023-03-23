package cn.woodwhales.ffmeng.service;

import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.ffmeng.model.ParseParam;
import cn.woodwhales.ffmeng.model.VideoToAudioParam;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author woodwhales on 2023-03-22 14:34
 */
@Slf4j
@Service
public class FFmengService {

    private String getFFmengFilePath() {
        URL resource = this.getClass().getClassLoader().getResource("ffmpeg.exe");
        return resource.getFile();
    }

    public OpResult<List<List<String>>> parseV2(ParseParam param) throws Exception {
        log.info("param={}", JSON.toJSONString(param));
        OpResult<Void> checkOpResult = param.check();
        if(checkOpResult.isFailure()) {
            return OpResult.error(checkOpResult.getBaseRespResult().getMessage());
        }

        String ffmpegFilePath = this.getFFmengFilePath();
        List<ParseParam.CommandDto> commandDtoList = param.convert();
        int index = 1;
        List<List<String>> result = new ArrayList<>();
        for (ParseParam.CommandDto commandDto : commandDtoList) {
            OpResult<List<String>> commandListOpResult = commandDto.getCommandList(ffmpegFilePath, param, index);
            if (commandListOpResult.isFailure()) {
                OpResult<List<List<String>>> opResult = OpResult.error(commandListOpResult.getBaseRespResult().getMessage());
                opResult.setData(result);
                return opResult;
            }
            List<String> commandList = commandListOpResult.getData();
            OpResult<List<String>> executeOpResult = this.execute(commandList);
            if(executeOpResult.isFailure()) {
                return OpResult.error(checkOpResult.getBaseRespResult().getMessage());
            }
            result.add(executeOpResult.getData());
            index++;
        }
        return OpResult.success(result);
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

    public OpResult<List<List<String>>> videoToAudioV2(VideoToAudioParam param) throws Exception {
        log.info("param={}", JSON.toJSONString(param));
        OpResult<Void> checkOpResult = param.check();
        if(checkOpResult.isFailure()) {
            return OpResult.error(checkOpResult.getBaseRespResult().getMessage());
        }
        String ffmpegFilePath = this.getFFmengFilePath();
        VideoToAudioParam.CommandDto commandDto = param.convert();
        List<List<String>> result = new ArrayList<>();
        OpResult<List<String>> commandListOpResult = commandDto.getCommandList(ffmpegFilePath);
        if(commandListOpResult.isFailure()) {
            return OpResult.error(commandListOpResult.getBaseRespResult().getMessage());
        }
        List<String> commandList = commandListOpResult.getData();
        OpResult<List<String>> executeOpResult = this.execute(commandList);
        if(executeOpResult.isFailure()) {
            return OpResult.error(checkOpResult.getBaseRespResult().getMessage());
        }
        result.add(executeOpResult.getData());
        return OpResult.success(result);
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

    public OpResult<List<String>> execute(List<String> commandList) throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(commandList);
        builder.redirectErrorStream(true);
        Process process = builder.start();
        List<String> result = new ArrayList<>();
        String line;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = bufferedReader.readLine()) != null) {
                result.add(line);
                log.info("{}", line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return OpResult.success(result);
    }

}
