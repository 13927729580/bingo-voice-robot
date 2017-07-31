package com.lambo.robot.apps.user;

import com.lambo.robot.RobotAppContext;
import com.lambo.robot.RobotSystemContext;
import com.lambo.robot.apis.music.IMusicNetApi;
import com.lambo.robot.apps.MsgTypeBaseApp;
import com.lambo.robot.kits.MusicAudioPlayer;
import com.lambo.robot.model.ISong;
import com.lambo.robot.model.RobotMsg;
import com.lambo.robot.model.enums.MsgTypeEnum;
import com.lambo.robot.model.enums.SystemMsgContentEnum;
import com.lambo.robot.model.msgs.HearMsg;
import com.lambo.robot.model.msgs.SpeakMsg;
import com.lambo.robot.model.msgs.SystemMsg;
import javazoom.jl.decoder.JavaLayerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * 网络音乐播放应用..
 * Created by lambo on 2017/7/24.
 */
public class MusicNetPlayApp extends MsgTypeBaseApp {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MusicAudioPlayer player;

    public MusicNetPlayApp(IMusicNetApi musicNetApi) {
        super(MsgTypeEnum.hear);
        this.player = new MusicAudioPlayer(musicNetApi);
    }

    @Override
    public void interrupt() {
        if (this.player.getState() == 1) { //播放时进行暂停.
            this.player.pause();
        }
    }

    @Override
    public void handleSystemMsg(RobotSystemContext systemContext, SystemMsg msg) {
        super.handleSystemMsg(systemContext, msg);
        if (msg.getContent() == SystemMsgContentEnum.interruptReset && this.player.getState() == 2) {
            try {
                this.player.play();
            } catch (IOException | JavaLayerException ignored) {
            }
        }
    }

    @Override
    public boolean handle(RobotAppContext appContext, RobotMsg<?> msg) throws Exception {
        String content = (String) msg.getContent();
        if (content.contains("播放音乐") || content.startsWith("音乐")) {
            if (!this.player.hasMusic()) {
                appContext.addMsg(new SpeakMsg("播放器歌单暂时没有音乐"));
                return true;
            }
            appContext.say(new SpeakMsg("即将播放音乐：" + player.getPlayList().get(0).getArtists() + " 的 " + player.getPlayList().get(0).getTitle()));
            this.player.play();
            return true;
        }

        if (content.contains("停止播放")) {
            this.player.stop();
            appContext.say(new SpeakMsg("停止音乐成功"));
            return true;
        }

        if (content.contains("下一首歌") || content.contains("下首歌") || content.contains("切歌")) {
            if (!this.player.hasMusic()) {
                appContext.say(new SpeakMsg("播放器歌单暂时没有音乐"));
                return true;
            }
            appContext.say(new SpeakMsg("即将播放音乐：" + player.getPlayList().get(0).getArtists() + " 的 " + player.getPlayList().get(0).getTitle()));
            this.player.next();
            return true;
        }
        if (content.startsWith("播放") && content.length() > 3) {
            String musicName = content.substring(content.indexOf("播放") + 2);
            return search(appContext, musicName);
        }

        if (content.contains("我要听") && content.length() >= (content.indexOf("我要听") + 4)) {
            String musicName = content.substring(content.indexOf("我要听") + 3);
            return search(appContext, musicName);
        }

        if (content.contains("搜索") && (content.contains("音乐") || content.contains("歌曲"))) {
            appContext.say(new SpeakMsg("请在叮的一声后说出您的关键字."));
            HearMsg hearEvent = appContext.listening();
            if (!hearEvent.isSuccess()) {
                logger.info("search listening failed, hearEvent = {}", hearEvent);
                appContext.say(new SpeakMsg("没有听清你的关键字，搜索失败！"));
                return true;
            }
            return search(appContext, hearEvent.getContent());
        }
        return false;
    }

    private boolean search(RobotAppContext appContext, String content) throws Exception {
        appContext.say(new SpeakMsg("正在为您搜索：" + content));
        List<ISong> search = this.player.search(content, 50, 0);
        if (null == search || search.isEmpty()) {
            appContext.say(new SpeakMsg("没有搜索结果，搜索失败！"));
            return true;
        }
        for (ISong song : search) {
            logger.debug(song.getArtists() + " == " + song.getTitle());
        }
        appContext.say(new SpeakMsg("总共搜索到 " + search.size() + " 首歌曲。"));
        this.player.setPlayList(search);
        appContext.say(new SpeakMsg("即将播放音乐：" + search.get(0).getArtists() + " 的 " + search.get(0).getTitle()));
        this.player.stop();
        this.player.play();
        return true;
    }
}