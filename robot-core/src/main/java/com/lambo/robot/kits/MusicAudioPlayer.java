package com.lambo.robot.kits;

import com.lambo.los.kits.io.IOKit;
import com.lambo.robot.apis.music.IMusicNetApi;
import com.lambo.robot.model.ISong;
import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 百度一听的播放器.
 * Created by lambo on 2017/7/24.
 */
public class MusicAudioPlayer {
    private final IMusicNetApi musicNetApi;
    private List<ISong> playList = new LinkedList<>();
    private AudioPlayer audioPlayer = new AudioPlayer();
    private JavaLayerPlayer advancedPlayer = null;
    private ISong currSong;
    private int lastPosition = 0;

    /**
     * 状态.0.初始。1.正在播放。2.暂停 3.停止.
     */
    private AtomicInteger state = new AtomicInteger(0);

    public MusicAudioPlayer(IMusicNetApi musicNetApi) {
        this.musicNetApi = musicNetApi;
    }

    public boolean hasMusic() {
        return null != playList && !playList.isEmpty();
    }

    public int getState() {
        return state.get();
    }

    public List<ISong> getPlayList() {
        return playList;
    }

    public void play() throws IOException, JavaLayerException {
        if (state.get() == 1) {
            return;
        }
        if (!hasMusic() && null == currSong) {
            return;
        }
        if (null == currSong) {
            lastPosition = 0;
            currSong = playList.remove(0);
        }
        if (null != advancedPlayer) {
            advancedPlayer.close();
        }
        final InputStream inputStream = currSong.getInputStream();
        advancedPlayer = audioPlayer.playMP3(inputStream);
        state.set(1);
        new Thread() {
            @Override
            public void run() {
                try {
                    advancedPlayer.play(lastPosition, Integer.MAX_VALUE);
                    if (null != advancedPlayer && advancedPlayer.isComplete()) {//如果是正常播放停止的自动播放下一首。
                        lastPosition = 0;
                        MusicAudioPlayer.this.next();
                    }
                } catch (JavaLayerException | IOException ignored) {
                } finally {
                    IOKit.closeIo(inputStream);
                }
            }
        }.start();
    }

    public void pause() {
        state.set(2);
        lastPosition = 0;
        if (null != advancedPlayer) {
            advancedPlayer.close();
            lastPosition = advancedPlayer.getLastPosition();
        }
    }

    public void next() throws IOException, JavaLayerException {
        this.stop();
        if (hasMusic()) {
            this.play();
        }
    }

    public void stop() {
        state.set(3);
        if (null != advancedPlayer) {
            lastPosition = 0;
            advancedPlayer.close();
        }
        this.advancedPlayer = null;
        this.currSong = null;
    }

    public List<ISong> search(String content, int limit, int offset) throws IOException {
        return musicNetApi.search(content, limit, offset);
    }

    public void setPlayList(List<ISong> playList) {
        this.playList.clear();
        if (null != playList && !playList.isEmpty()) {
            this.playList.addAll(playList);
        }
    }
}
