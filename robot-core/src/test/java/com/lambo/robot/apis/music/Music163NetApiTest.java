package com.lambo.robot.apis.music;

import com.lambo.robot.kits.MusicAudioPlayer;
import com.lambo.robot.model.ISong;
import org.junit.Test;

import java.util.List;

/**
 * 测试.
 * Created by lambo on 2017/7/26.
 */
public class Music163NetApiTest {

    @Test
    public void testSearch() throws Exception {
        IMusicNetApi musicNetApi = new Music163NetApi();
        MusicAudioPlayer player = new MusicAudioPlayer(musicNetApi);
        List<ISong> songs = player.search("李健", 10, 0);
        if (null != songs) {
            for (ISong song : songs) {
                System.out.println(song.getArtists() + " == " + song.getTitle());
            }
        }
        player.setPlayList(songs);
        player.next();
        player.play();
    }
}