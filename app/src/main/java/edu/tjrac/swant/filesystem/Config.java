package edu.tjrac.swant.filesystem;

import org.jetbrains.annotations.Nullable;

/**
 * Created by wpc on 2018-08-25.
 */

public class Config {

    public static final String support_image_type=".gif.jpg.jpeg.png.bmp";
    public static final String support_music_type=".mp3";
    public static final String support_video_type=".mp4";
    @Nullable
    public static final String RESET_PATH_SETTING_PATH="/Pictures/path_reset";

    public class SP{
        public static final String setting="filesystem_setting";
        public static final String CARRAY_JSON="carry_json";
    }
}
