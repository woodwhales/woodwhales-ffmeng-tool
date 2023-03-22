package cn.woodwhales.ffmeng.controller;

import cn.woodwhales.common.model.vo.RespVO;
import cn.woodwhales.ffmeng.model.ParseParam;
import cn.woodwhales.ffmeng.service.ParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author woodwhales on 2023-03-22 12:45
 */
@RestController
@RequestMapping
public class ParseController {

    @Autowired
    private ParseService parseService;

    @CrossOrigin
    @PostMapping("/parse")
    public RespVO<Void> index(@RequestBody ParseParam param) throws Exception {
        return RespVO.resp(parseService.parse(param));
    }

}
