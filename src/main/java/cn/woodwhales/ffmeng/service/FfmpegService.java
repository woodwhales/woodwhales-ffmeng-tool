package cn.woodwhales.ffmeng.service;

import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.ffmeng.model.param.ParseParam;
import cn.woodwhales.ffmeng.model.param.VideoToAudioParam;
import cn.woodwhales.ffmeng.model.resp.MediaVo;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author woodwhales on 2023-03-22 14:34
 */
@Slf4j
@Service
public class FfmpegService {

    private static String getFfmpegFilePath() {
        URL resource = FfmpegService.class.getClassLoader().getResource("ffmpeg.exe");
        return resource.getFile();
    }

    public OpResult<MediaVo> parseV2(ParseParam param) throws Exception {
        log.info("param={}", JSON.toJSONString(param));
        OpResult<Void> checkOpResult = param.check();
        if(checkOpResult.isFailure()) {
            return OpResult.error(checkOpResult.getBaseRespResult().getMessage());
        }
        MediaVo vo = new MediaVo();
        String ffmpegFilePath = getFfmpegFilePath();
        String srcFilePath = param.letSrcFilePath();
        vo.setMediaDuration(getMediaDuration(srcFilePath).getData());
        List<ParseParam.CommandDto> commandDtoList = param.convert();
        int index = 1;
        List<List<String>> excuteLogList = new ArrayList<>();
        for (ParseParam.CommandDto commandDto : commandDtoList) {
            OpResult<List<String>> commandListOpResult = commandDto.getCommandList(ffmpegFilePath, param, index);
            if (commandListOpResult.isFailure()) {
                OpResult<MediaVo> opResult = OpResult.error(commandListOpResult.getBaseRespResult().getMessage());
                vo.setExecuteLogList(Arrays.asList());
                opResult.setData(vo);
                return opResult;
            }
            List<String> commandList = commandListOpResult.getData();
            OpResult<List<String>> executeOpResult = this.execute(commandList);
            if(executeOpResult.isFailure()) {
                return OpResult.error(checkOpResult.getBaseRespResult().getMessage());
            }
            excuteLogList.add(executeOpResult.getData());

            index++;
        }
        vo.setExecuteLogList(excuteLogList);
        return OpResult.success(vo);
    }

    public OpResult<Void> parse(ParseParam param) throws Exception {
        log.info("param={}", JSON.toJSONString(param));
        OpResult<Void> checkOpResult = param.check();
        if(checkOpResult.isFailure()) {
            return checkOpResult;
        }

        String ffmpegFilePath = getFfmpegFilePath();

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

    public OpResult<MediaVo> videoToAudioV2(VideoToAudioParam param) throws Exception {
        log.info("param={}", JSON.toJSONString(param));
        MediaVo vo = new MediaVo();
        OpResult<Void> checkOpResult = param.check();
        if(checkOpResult.isFailure()) {
            return OpResult.error(checkOpResult.getBaseRespResult().getMessage());
        }

        String srcFilePath = param.letSrcFilePath();
        vo.setMediaDuration(getMediaDuration(srcFilePath).getData());

        String ffmpegFilePath = getFfmpegFilePath();
        VideoToAudioParam.CommandDto commandDto = param.convert();
        List<List<String>> executeLogList = new ArrayList<>();
        OpResult<List<String>> commandListOpResult = commandDto.getCommandList(ffmpegFilePath);
        if(commandListOpResult.isFailure()) {
            return OpResult.error(commandListOpResult.getBaseRespResult().getMessage());
        }
        List<String> commandList = commandListOpResult.getData();
        log.info("commandList={}", JSON.toJSONString(commandList));
        OpResult<List<String>> executeOpResult = this.execute(commandList);
        if(executeOpResult.isFailure()) {
            return OpResult.error(checkOpResult.getBaseRespResult().getMessage());
        }
        executeLogList.add(executeOpResult.getData());
        vo.setExecuteLogList(executeLogList);
        return OpResult.success(vo);
    }

    public OpResult<Void> videoToAudio(VideoToAudioParam param) throws Exception {
        log.info("param={}", JSON.toJSONString(param));
        OpResult<Void> checkOpResult = param.check();
        if(checkOpResult.isFailure()) {
            return checkOpResult;
        }

        String ffmpegFilePath = getFfmpegFilePath();
        VideoToAudioParam.CommandDto commandDto = param.convert();
        String finalCommand = commandDto.letFinalCommand(ffmpegFilePath);
        Process process = Runtime.getRuntime().exec(finalCommand);
        int processResult = process.waitFor();
        log.info("finalCommand = {}, processResult = {}", finalCommand, processResult);
        return OpResult.success();
    }

    public OpResult<List<String>> execute(List<String> commandList) throws Exception {
        ProcessBuilder builder = new ProcessBuilder();

        StringBuffer sb = new StringBuffer();
        for (String command : commandList) {
            sb.append(command).append(" ");
        }
        log.info("command={}", sb.toString());

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

    /**
     * 获取音视频的总时长
     * @param ffmpegFilePath
     * @param srcFilePath
     * @return
     * @throws Exception
     */
    public static OpResult<String> getMediaDuration(String ffmpegFilePath, String srcFilePath) throws Exception {
        return getMediaDuration(ffmpegFilePath, srcFilePath);
    }

    /**
     * 获取音视频的总时长
     * @param srcFilePath
     * @return
     * @throws Exception
     */
    public static OpResult<String> getMediaDuration(String srcFilePath) {
        List<String> commandList = new ArrayList<>();
        commandList.add(getFfmpegFilePath());
        commandList.add("-i");
        commandList.add(srcFilePath);
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(commandList);
        builder.redirectErrorStream(true);
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String mediaLength = "";
        String line;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = bufferedReader.readLine()) != null) {
                if(StringUtils.contains(line, "Duration:")) {
                    log.info("{}", line);
                    String lengthStr = StringUtils.trim(StringUtils.substringAfter(line, "Duration:"));
                    mediaLength = StringUtils.substring(lengthStr, 0, 8);
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return OpResult.success(mediaLength);
    }

}
