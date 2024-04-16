package cn.woodwhales.ffmpeg.controller;

import cn.woodwhales.common.model.vo.RespVO;
import cn.woodwhales.ffmpeg.model.param.ParseParam;
import cn.woodwhales.ffmpeg.model.param.VideoToAudioParam;
import cn.woodwhales.ffmpeg.model.resp.MediaVo;
import cn.woodwhales.ffmpeg.service.FfmpegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author woodwhales on 2023-03-22 12:45
 */
@RestController
@RequestMapping("/")
public class FFmpegController {

    @Autowired
    private FfmpegService FfmpegService;

    @CrossOrigin
    @PostMapping("/parse")
    public RespVO<Void> parse(@RequestBody ParseParam param) throws Exception {
        return RespVO.resp(FfmpegService.parse(param));
    }

    @CrossOrigin
    @PostMapping("/parseV2")
    public RespVO<MediaVo> parseV2(@RequestBody ParseParam param) throws Exception {
        return RespVO.resp(FfmpegService.parseV2(param));
    }

    @CrossOrigin
    @PostMapping("/videoToAudioV2")
    public RespVO<MediaVo> videoToAudioV2(@RequestBody VideoToAudioParam param) throws Exception {
        return RespVO.resp(FfmpegService.videoToAudioV2(param));
    }

}
