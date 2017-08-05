package com.lambo.robot.apis;

import com.lambo.robot.model.ISong;

import java.io.IOException;
import java.util.List;

/**
 * 网络api.
 * Created by lambo on 2017/7/26.
 */
public interface IMusicNetApi {

    /**
     * 搜索歌曲.
     *
     * @param text
     * @return
     */
    List<ISong> search(String text, int limit, int offset) throws IOException;
}
