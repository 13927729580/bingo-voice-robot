package com.lambo.robot;

import com.lambo.los.kits.RunnableMainRunner;
import com.lambo.robot.apps.system.RecordSystemApp;
import com.lambo.robot.apps.system.SpeakSystemApp;
import com.lambo.robot.apps.system.VoiceDataBaiDuSystemApp;
import com.lambo.robot.apps.system.WakeUpSystemApp;
import com.lambo.robot.apps.user.*;
import com.lambo.robot.drivers.records.impl.JavaSoundRecordImpl;
import com.lambo.robot.drivers.speaks.ISpeak;
import com.lambo.robot.drivers.speaks.impl.BaiDuVoiceSpeakImpl;
import com.lambo.robot.drivers.wakes.IWakeUp;
import com.lambo.robot.drivers.wakes.impl.SnowBoyWakeUpImpl;
import com.lambo.robot.drivers.wakes.impl.SystemReadWakeUpImpl;
import com.lambo.robot.manager.impl.RobotDriverManager;
import com.lambo.robot.model.msgs.SpeakMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * test.
 * Created by Administrator on 2017/7/20.
 */
public class RobotRunnable implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RunnableMainRunner.Value
    private String test;

    @Override
    public void run() {
        try {
            String configPath = "profile.yml";
            if (!new File(configPath).exists()) {
                configPath = "classpath:/" + configPath;
            }
            RobotConfig robotConfig = RobotConfig.getRobotConfig(configPath);

            IWakeUp wakeUp;
            if ("true".equalsIgnoreCase(test)) {
                wakeUp = new SystemReadWakeUpImpl();
            } else {
                wakeUp = new SnowBoyWakeUpImpl(robotConfig);
            }

            ISpeak speak = new BaiDuVoiceSpeakImpl(robotConfig.getVoiceApi());

            RobotDriverManager robotDriverManager = new RobotDriverManager();
            IRobotOperatingSystem system = new RobotOperatingSystem(robotConfig, robotDriverManager);
            //使用系统输入作为唤醒的应用.
            system.install(new WakeUpSystemApp(wakeUp));
            system.install(new SpeakSystemApp(speak));
            system.install(new VolumeApp());
//            system.install(new SpeakSystemOutApp());
            system.install(new VoiceDataBaiDuSystemApp(robotConfig.getVoiceApi()));
            system.install(new WebServerApp(robotConfig.webPort));
            system.install(new RecordSystemApp(new JavaSoundRecordImpl(robotConfig)));
            system.install(new MusicNetPlayApp(robotConfig.getMusicNetApi()));
            system.install(new TuLingRobotApp());

            if (!"true".equalsIgnoreCase(test)) {
                speak.say(new SpeakMsg(robotConfig.getWelcomeMsg()));
            }
            system.run();
        } catch (Exception e) {
            logger.error("robot start failed", e);
        }
    }
}