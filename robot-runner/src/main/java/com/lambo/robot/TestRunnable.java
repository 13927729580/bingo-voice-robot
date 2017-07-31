package com.lambo.robot;

import com.google.gson.Gson;
import com.lambo.robot.drivers.records.IRecord;
import com.lambo.robot.drivers.records.impl.JavaSoundRecordImpl;
import com.lambo.robot.kits.BeepPlayer;
import com.lambo.robot.model.VoiceData;

/**
 * 热歌.
 * Created by lambo on 2017/7/21.
 */
public class TestRunnable implements Runnable {

    @Override
    public void run() {
        try {
            RobotConfig robotConfig = RobotConfig.getRobotConfig("classpath:/profile.yml");
            System.out.println(new Gson().toJson(robotConfig));
            IRecord record = new JavaSoundRecordImpl(robotConfig);
            BeepPlayer beepPlayer = robotConfig.getBeepPlayer();
            while (true) {
                VoiceData voiceData = record.record();
                if (null != voiceData) {
                    beepPlayer.playPCM(robotConfig.getRecordAudioFormat(), voiceData.getData());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
