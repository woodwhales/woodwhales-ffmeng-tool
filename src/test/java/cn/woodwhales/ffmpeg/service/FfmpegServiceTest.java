package cn.woodwhales.ffmpeg.service;

import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.ffmpeg.model.param.ParseParam;
import cn.woodwhales.ffmpeg.model.param.VideoToAudioParam;
import com.alibaba.fastjson.JSON;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

class FfmpegServiceTest {

    public static void main(String[] args) throws Exception {
        main5();
    }

    public static void main4() throws Exception {
        URL resource = FfmpegServiceTest.class.getClassLoader().getResource("ffmpeg.exe");
        OpResult<String> execute = FfmpegService.getMediaDuration(resource.getFile(), "D:\\movie\\new\\让子弹飞.m4a");
        System.out.println("execute = " + execute.getData());
    }

    public static void main3() throws UnsupportedAudioFileException, IOException {
        File file = new File("D:\\movie\\让子弹飞.m4a");
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
        AudioFormat format = audioInputStream.getFormat();
        long frames = audioInputStream.getFrameLength();
        double durationInSeconds = (frames+0.0) / format.getFrameRate();
        System.out.println("durationInSeconds = " + durationInSeconds);
    }

    public static void main() throws Exception {
        String json = "{\"srcFileName\":\"让子弹飞.m4a\",\"srcFilePath\":\"D:\\\\movie\",\"destFilePath\":\"D:\\\\movie\",\"timeArrList\":[[\"00:00:12\",\"00:12:36\"],[\"00:12:36\",\"00:24:30\"]]}";
        ParseParam param = JSON.parseObject(json, ParseParam.class);
        FfmpegService FfmpegService = new FfmpegService();
        OpResult<Void> opResult = FfmpegService.parse(param);
        System.out.println("opResult = " + JSON.toJSON(opResult));
    }

    public static void main2() throws Exception {
        URL resource = FfmpegServiceTest.class.getClassLoader().getResource("ffmpeg.exe");
        VideoToAudioParam.CommandDto commandDto = new VideoToAudioParam.CommandDto();
        commandDto.setSrcFile("D:\\movie\\神探亨特张.mp4");
        commandDto.setDestFile("D:\\movie\\神探亨特张.m4a");
        OpResult<List<String>> commandListOpResult = commandDto.getCommandList(resource.getFile());
        FfmpegService ffmpegService = new FfmpegService();
        OpResult<List<String>> execute = ffmpegService.execute(commandListOpResult.getData());
        execute.getData().forEach(System.out::println);
    }

    public static void main5() throws Exception {
        URL resource = FfmpegServiceTest.class.getClassLoader().getResource("ffmpeg.exe");
        VideoToAudioParam.CommandDto commandDto = new VideoToAudioParam.CommandDto();
        commandDto.setSrcFile("D:\\movie\\功夫.m4a");
        commandDto.setDestFile("D:\\movie\\功夫.mp3");
        OpResult<List<String>> commandListOpResult = commandDto.m4aToMp3(resource.getFile());
        FfmpegService ffmpegService = new FfmpegService();
        OpResult<List<String>> execute = ffmpegService.execute(commandListOpResult.getData());
        execute.getData().forEach(System.out::println);
    }

}