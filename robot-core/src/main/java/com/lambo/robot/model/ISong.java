package com.lambo.robot.model;

import java.io.IOException;
import java.io.InputStream;

/**
 * 音乐接口.
 * Created by lambo on 2017/7/26.
 */
public interface ISong {

    /**
     * 歌曲id.
     *
     * @return
     */
    String getSongId();

    /**
     * 歌曲名称.
     *
     * @return
     */
    String getTitle();

    /**
     * 艺术家.
     *
     * @return
     */
    String getArtists();

    /**
     * 播放流.
     *
     * @return
     */
    InputStream getInputStream() throws IOException;
}
