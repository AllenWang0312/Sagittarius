package com.im_hero.diffpatch;

/**
 * Created by wpc on 2020-04-15.
 */
public class BsDiff {
    BsDiff() {
        System.loadLibrary("bsdiff");
    }

    native int bsdiff(String oldFile, String newFile, String patchFile);

    native int bspatch(String oldFile, String newFile, String patchFile);
}
