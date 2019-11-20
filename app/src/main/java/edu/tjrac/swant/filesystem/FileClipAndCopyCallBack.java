package edu.tjrac.swant.filesystem;

import java.io.File;

/**
 * Created by wpc on 2018/1/12 0012.
 */

public interface FileClipAndCopyCallBack {

    public void onFileCliped(File file);
    public void onFileUnCliped(File file);
    public void onFileCopyed(File file);
    public void onFileUnCopyed(File file);

}
