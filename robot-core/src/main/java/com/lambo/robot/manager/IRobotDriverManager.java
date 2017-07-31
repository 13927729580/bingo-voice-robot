package com.lambo.robot.manager;

import com.lambo.robot.IApp;
import com.lambo.robot.drivers.IDriver;
import com.lambo.robot.model.enums.MsgTypeEnum;

/**
 * 驱动管理器.
 * Created by lambo on 2017/7/25.
 */
public interface IRobotDriverManager {

    int get(MsgTypeEnum msgTypeEnum);

    int incrementAndGet(MsgTypeEnum msgTypeEnum);

    int decrementAndGet(MsgTypeEnum msgTypeEnum);

    void regDriver(Class<? extends IDriver> driverClazz, IApp app);
}
