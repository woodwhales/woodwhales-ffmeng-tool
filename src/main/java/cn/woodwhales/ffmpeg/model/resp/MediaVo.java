package cn.woodwhales.ffmpeg.model.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author woodwhales on 2023-03-23 14:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaVo {

    /**
     * ffmpeg 执行日志
     */
    private List<List<String>> executeLogList;

    /**
     * 音视频的总时长
     */
    private String mediaDuration;
}
