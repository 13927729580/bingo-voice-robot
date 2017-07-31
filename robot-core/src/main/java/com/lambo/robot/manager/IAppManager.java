package com.lambo.robot.manager;

import com.lambo.robot.IApp;
import com.lambo.robot.RobotSystemContext;
import com.lambo.robot.model.RobotMsg;
import com.lambo.robot.model.enums.MsgTypeEnum;

/**
 * 应用管理器.
 * Created by lambo on 2017/7/25.
 */
public interface IAppManager {
    /**
     * 安装应用.
     *
     * @param app
     */
    void install(IApp app);

    void regListener(IApp app, MsgTypeEnum... msgTypeArgs);

    void removeListener(IApp app);

    /**
     * 消息处理器.
     *
     * @param systemContext
     * @param robotMsg
     * @return
     * @throws Exception
     */
    boolean msgHandle(RobotSystemContext systemContext, RobotMsg<?> robotMsg) throws Exception;

    void halt();
}