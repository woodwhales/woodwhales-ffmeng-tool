package cn.woodwhales.ffmeng.model;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.woodwhales.common.business.DataTool;
import cn.woodwhales.common.model.result.OpResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;

/**
 * @author woodwhales on 2023-03-22 14:34
 */
@Data
public class ParseParam {

    private String srcFileName;
    private String srcFilePath;
    private String destFilePath;
    private List<List<String>> timeArrList;

    public OpResult<Void> check() throws Exception {
        if (!FileUtil.exist(this.srcFilePath)) {
            return OpResult.error("原始文件地址不合法");
        }
        String srcFile = srcFilePath + File.separator + srcFileName;
        if (!FileUtil.exist(srcFile)) {
            return OpResult.error("原始文件不存在");
        }

        int index = 1;
        for (List<String> timeArr : this.timeArrList) {
            String destFilePath = this.destFilePath + File.separator +
                    StringUtils.substringBeforeLast(this.srcFileName, ".") + "-" + index + "." +
                    StringUtils.substringAfterLast(this.srcFileName, ".");
            if (FileUtil.exist(destFilePath)) {
                return OpResult.error(String.format("目标文件:%s已存在，请及时清理", destFilePath));
            }
        }

        return OpResult.success();
    }

    public List<CommandDto> convert() {
        return DataTool.toList(this.timeArrList, timeArr -> {
            String startTime = timeArr.get(0);
            long duration = DateUtil.between(DateUtil.parse("2023-01-01 " + startTime), DateUtil.parse("2023-01-01 " + timeArr.get(1)), DateUnit.SECOND);
            return new CommandDto(startTime, Integer.valueOf(duration + ""));
        });
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommandDto {
        private String startTime;
        private Integer duration;

        public String letFinalCommand(String ffmengFilePath,
                                      ParseParam param,
                                      Integer index) {
            String srcFilePath = param.getSrcFilePath() + File.separator + param.getSrcFileName();
            String descFilePath = param.getDestFilePath() + File.separator +
                    StringUtils.substringBeforeLast(param.getSrcFileName(), ".") + "-" + index + "." +
                    StringUtils.substringAfterLast(param.getSrcFileName(), ".");
            return String.format("%s -ss %s -t %s -i %s -c copy %s", ffmengFilePath, startTime, duration, srcFilePath, descFilePath);
        }
    }

}
