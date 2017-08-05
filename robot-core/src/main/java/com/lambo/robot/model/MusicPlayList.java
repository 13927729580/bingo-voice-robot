package com.lambo.robot.model;

import com.lambo.los.kits.io.IOKit;
import org.ho.yaml.Yaml;
import org.ho.yaml.YamlStream;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 播放列表.
 * Created by lambo on 2017/8/5.
 */
public class MusicPlayList {

    /**
     * 歌曲列表.
     */
    private final List<Song> localPlayList = new ArrayList<>();

    /**
     * 歌曲列表.
     */
    private final List<Song> playList = new ArrayList<>();

    /**
     * 当前索引号.
     */
    private final AtomicInteger index = new AtomicInteger(0);

    private final File localCacheName = new File(".local.music.cache");

    public MusicPlayList() {
        InputStream in =null;
        try {
            in = new FileInputStream(localCacheName);
            Song[] objects = Yaml.loadStreamOfType(in, Song[].class).next();
            if (null != objects) {
                localPlayList.addAll(Arrays.asList(objects));
            }
        } catch (Exception ignored) {
        }finally {
            IOKit.closeIo(in);
        }
    }

    public void setPlayList(List<Song> playList) {
        index.set(0);
        this.playList.clear();
        if (null != playList && !playList.isEmpty()) {
            this.playList.addAll(playList);
        }
    }

    public Song save() {
        Song song = curr();
        if (null != song && !localPlayList.contains(song)) {
            localPlayList.add(song);
            if (!localCacheName.exists()){
                try {
                    localCacheName.createNewFile();
                } catch (IOException ignored) {
                }
            }
            try {
                Yaml.dump(localPlayList, localCacheName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return song;
    }

    public int incrementAndGet() {
        return index.incrementAndGet();
    }

    public Song getNextSong() {
        return !playList.isEmpty() ? playList.get((index.get() + 1)% playList.size()) : null;
    }

    public Song curr() {
        return !playList.isEmpty() ? playList.get(index.get()% playList.size()) : null;
    }

    public List<Song> getLocalPlayList() {
        return localPlayList;
    }

    public List<Song> getPlayList() {
        return playList;
    }

    public Song delete() {
        Song song = curr();
        if (null != song && localPlayList.contains(song)) {
            localPlayList.remove(song);
            if (!localCacheName.exists()){
                try {
                    localCacheName.createNewFile();
                } catch (IOException ignored) {
                }
            }
            try {
                Yaml.dump(localPlayList, localCacheName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return song;
    }
}
