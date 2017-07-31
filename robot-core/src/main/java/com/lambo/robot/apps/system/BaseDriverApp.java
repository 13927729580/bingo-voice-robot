package com.lambo.robot.apps.system;

import com.lambo.robot.RobotAppContext;
import com.lambo.robot.apps.MsgTypeBaseApp;
import com.lambo.robot.drivers.IDriver;
import com.lambo.robot.model.RobotMsg;
import com.lambo.robot.model.enums.MsgTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统应用.
 * Created by lambo on 2017/7/25.
 */
public abstract class BaseDriverApp extends MsgTypeBaseApp {
    private List<Class<? extends IDriver>> drivers = new ArrayList<>();

    public BaseDriverApp(MsgTypeEnum... msgTypeEnums) {
        super(msgTypeEnums);
    }

    public List<Class<? extends IDriver>> getDrivers() {
        return drivers;
    }

    @Override
    public boolean canStart(RobotMsg<?> robotMsg) {
        if (!drivers.isEmpty()) {
            return true;
        }
        return super.canStart(robotMsg);
    }

    @Override
    public void init(RobotAppContext appContext) {
        super.init(appContext);
        for (Class<? extends IDriver> driver : drivers) {
            appContext.regDriver(driver, this);
        }
        appContext.setRunningLevel(-1);
    }

}
