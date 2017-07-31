package com.lambo.robot.kits;

/**
 * record.
 * Created by lambo on 2017/7/28.
 */
public class RMSUtil {

    /**
     * 查询音量.
     *
     * @param audioData 语音文件.
     * @return
     */
    public static double getRMS(byte[] audioData) {
        double sum = 0;
        for (int i = 0; i < audioData.length / 2; i++) {
            double sample = (audioData[i * 2] | audioData[i * 2 + 1] << 8) / 32768.0;
            sum += Math.abs(sample);
        }
        return 77.0 * Math.log10(Math.sqrt(sum / audioData.length / 2) / 65535.0);
    }
}
