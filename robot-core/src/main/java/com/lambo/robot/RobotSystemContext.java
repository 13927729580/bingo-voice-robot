package com.lambo.robot;

import com.lambo.robot.manager.IAppManager;
import com.lambo.robot.manager.IMsgManager;
import com.lambo.robot.manager.IRobotDriverManager;
import com.lambo.robot.manager.impl.AppManagerImpl;
import com.lambo.robot.manager.impl.MsgManagerImpl;
import com.lambo.robot.manager.impl.RobotDriverManager;
import com.lambo.robot.model.RobotMsg;
import com.lambo.robot.model.enums.MsgTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 当前系统的上下文环境.
 * Created by lambo on 2017/7/24.
 */
public class RobotSystemContext {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final RobotConfig robotConfig;

    private final IAppManager appManager;
    protected final IRobotDriverManager robotDriverManager;
    protected final IMsgManager msgManager;

    private Map<MsgTypeEnum, RobotAppContext> focusMap = new HashMap<>();

    /**
     * 当前系统唤醒的用户id.
     */
    private String wakeUpUid;

    public RobotSystemContext(RobotConfig robotConfig, RobotDriverManager robotDriverManager) {
        this.robotConfig = robotConfig;
        this.robotDriverManager = robotDriverManager;
        this.appManager = new AppManagerImpl();
        this.msgManager = new MsgManagerImpl();
    }

    public void focus(MsgTypeEnum msgTypeEnum, RobotAppContext appContext) {
        focusMap.put(msgTypeEnum, appContext);
    }

    public RobotAppContext focus(MsgTypeEnum msgTypeEnum) {
        return focusMap.get(msgTypeEnum);
    }

    public IRobotDriverManager getRobotDriverManager() {
        return robotDriverManager;
    }

    public RobotConfig getRobotConfig() {
        return robotConfig;
    }

    public IAppManager getAppManager() {
        return appManager;
    }

    public IMsgManager getMsgManager() {
        return msgManager;
    }

    public void addMsg(RobotAppContext appContext, RobotMsg<?> robotMsg) {
        if (null == robotMsg) {
            return;
        }
        if (null != appContext) {
            robotMsg.setTargetPid(appContext.getPid());
        }
        msgManager.addRobotMsg(robotMsg);
        logger.info("addMsg success, robotEvent = {}", robotMsg);
    }

    public boolean isWaitWakeUp() {
        return robotDriverManager.get(MsgTypeEnum.waitWakeUp) > 0;
    }

    public void halt() {
        getAppManager().halt();
    }

    public String getWakeUpUid() {
        return wakeUpUid;
    }

    public void setWakeUpUid(String wakeUpUid) {
        this.wakeUpUid = wakeUpUid;
    }
}
