package cn.woodwhales.ffmeng.model.param;

import cn.hutool.core.io.FileUtil;
import cn.woodwhales.common.model.result.OpResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author woodwhales on 2023-03-22 17:13
 */
@Data
public class VideoToAudioParam {

    private String srcFileName;
    private String srcFilePath;
    private String destFilePath;

    public String letSrcFilePath() {
        return this.srcFilePath + File.separator + this.srcFileName;
    }

    public OpResult<Void> check() throws Exception {
        if (!FileUtil.exist(this.srcFilePath)) {
            return OpResult.error("原始文件地址不合法");
        }
        String srcFile = srcFilePath + File.separator + srcFileName;
        if (!FileUtil.exist(srcFile)) {
            return OpResult.error("原始文件不存在");
        }
        String destFile;
        if (StringUtils.endsWithIgnoreCase(this.srcFileName, ".mkv")) {
            destFile = this.destFilePath + File.separator + StringUtils.substringBeforeLast(this.srcFileName, ".") + "." + "mp3";
        } else {
            destFile = this.destFilePath + File.separator + StringUtils.substringBeforeLast(this.srcFileName, ".") + "." + "m4a";
        }
        if (FileUtil.exist(destFile)) {
            return OpResult.error(String.format("目标文件:%s已存在，请及时清理", destFile));
        }
        return OpResult.success();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommandDto {
        private String srcFile;
        private String destFile;

        public String letFinalCommand(String ffmpegFilePath) {
            if (StringUtils.endsWithIgnoreCase(this.srcFile, ".mkv")) {
                return String.format("%s -i %s -b:a 64K %s", ffmpegFilePath, this.srcFile, this.destFile);
            } else {
                return String.format("%s -i %s -vn -acodec copy %s", ffmpegFilePath, this.srcFile, this.destFile);
            }
        }

        public OpResult<List<String>> getCommandList(String ffmpegFilePath) {
            if (FileUtil.exist(this.destFile)) {
                return OpResult.error(String.format("目标文件:%s已存在，请及时清理", destFile));
            }
            List<String> command = new ArrayList<String>();
            command.add(ffmpegFilePath);
            command.add("-i");
            command.add(this.srcFile);
            if (!StringUtils.endsWithIgnoreCase(this.srcFile, ".mkv")) {
                command.add("-vn");
                command.add("-acodec");
                command.add("copy");
            } else {
                command.add("-b:a");
                command.add("64K");
            }
            command.add(this.destFile);
            return OpResult.success(command);
        }
    }

    public CommandDto convert() {
        String srcFile = this.srcFilePath + File.separator + this.srcFileName;
        String destFile;
        if(StringUtils.endsWithIgnoreCase(srcFile, ".mkv")) {
            destFile = this.destFilePath + File.separator + StringUtils.substringBeforeLast(this.srcFileName, ".") + "." + "mp3";
        } else {
            destFile = this.destFilePath + File.separator + StringUtils.substringBeforeLast(this.srcFileName, ".") + "." + "m4a";
        }
        return new CommandDto(srcFile, destFile);
    }

}
