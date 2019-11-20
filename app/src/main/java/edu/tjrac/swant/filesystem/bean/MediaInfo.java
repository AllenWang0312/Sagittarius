package edu.tjrac.swant.filesystem.bean;

import edu.tjrac.swant.filesystem.MediaUtil;

/**
 * Created by wpc on 2018/2/5 0005.
 */

public class MediaInfo   {
    private MediaUtil.MediaType type;
    String name;
    String filePath;
    long size;
    int width;
    int height;

    String auther;

    public MediaInfo(MediaUtil.MediaType type,
                     String name,
                     String filePath,
                     long size, int width,
                     int height) {
        this.type = type;
        this.name = name;
        this.filePath = filePath;
        this.size = size;
        this.width = width;
        this.height = height;
        this.auther = auther;
    }
}
