package com.lambo.robot.manager.impl;

import com.lambo.robot.IApp;
import com.lambo.robot.drivers.IDriver;
import com.lambo.robot.manager.IRobotDriverManager;
import com.lambo.robot.model.enums.MsgTypeEnum;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 设备管理器.
 * Created by lambo on 2017/7/25.
 */
public class RobotDriverManager implements IRobotDriverManager {
    private final Map<MsgTypeEnum, AtomicInteger> msgTypeActiveMap = new ConcurrentHashMap<>();

    public RobotDriverManager() {
        for (MsgTypeEnum msgTypeEnum : MsgTypeEnum.values()) {
            msgTypeActiveMap.put(msgTypeEnum, new AtomicInteger(0));
        }
    }

    @Override
    public int get(MsgTypeEnum msgTypeEnum) {
        return msgTypeActiveMap.get(msgTypeEnum).get();
    }

    @Override
    public int incrementAndGet(MsgTypeEnum msgTypeEnum) {
        return msgTypeActiveMap.get(msgTypeEnum).incrementAndGet();
    }


    @Override
    public int decrementAndGet(MsgTypeEnum msgTypeEnum) {
        return msgTypeActiveMap.get(msgTypeEnum).decrementAndGet();
    }

    @Override
    public void regDriver(Class<? extends IDriver> driverClazz, IApp app) {
    }
}
