package cn.woodwhales.ffmpeg.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author woodwhales on 2023-03-23 14:59
 */
@Data
public class MediaDto {

    private List<String> commandList;

    private String mediaDuration;

}
