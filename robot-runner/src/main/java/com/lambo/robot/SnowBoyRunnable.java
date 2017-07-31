package com.lambo.robot;

import com.lambo.los.kits.io.IOKit;
import com.lambo.robot.drivers.wakes.IWakeUp;
import com.lambo.robot.drivers.wakes.impl.SnowBoyWakeUpImpl;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 热歌.
 * Created by lambo on 2017/7/21.
 */
public class SnowBoyRunnable implements Runnable {

    @Override
    public void run() {
        try {
            InputStream inputStream = IOKit.getInputStream("classpath:/profile.yml");
            IWakeUp wakeUp = new SnowBoyWakeUpImpl(RobotConfig.getRobotConfig(inputStream));
            IOKit.closeIo(inputStream);
            while (true) {
                System.out.println(wakeUp.waitWakeUp());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
