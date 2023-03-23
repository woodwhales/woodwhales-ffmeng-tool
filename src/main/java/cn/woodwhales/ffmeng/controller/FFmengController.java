package cn.woodwhales.ffmeng.controller;

import cn.woodwhales.common.model.vo.RespVO;
import cn.woodwhales.ffmeng.model.ParseParam;
import cn.woodwhales.ffmeng.model.VideoToAudioParam;
import cn.woodwhales.ffmeng.service.FFmengService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author woodwhales on 2023-03-22 12:45
 */
@RestController
@RequestMapping("/")
public class FFmengController {

    @Autowired
    private FFmengService FFmengService;

    @CrossOrigin
    @PostMapping("/parse")
    public RespVO<Void> parse(@RequestBody ParseParam param) throws Exception {
        return RespVO.resp(FFmengService.parse(param));
    }

    @CrossOrigin
    @PostMapping("/parseV2")
    public RespVO<List<List<String>>> parseV2(@RequestBody ParseParam param) throws Exception {
        return RespVO.resp(FFmengService.parseV2(param));
    }

    @CrossOrigin
    @PostMapping("/videoToAudio")
    public RespVO<Void> videoToAudio(@RequestBody VideoToAudioParam param) throws Exception {
        return RespVO.resp(FFmengService.videoToAudio(param));
    }

    @CrossOrigin
    @PostMapping("/videoToAudioV2")
    public RespVO<List<List<String>>> videoToAudioV2(@RequestBody VideoToAudioParam param) throws Exception {
        return RespVO.resp(FFmengService.videoToAudioV2(param));
    }

}
