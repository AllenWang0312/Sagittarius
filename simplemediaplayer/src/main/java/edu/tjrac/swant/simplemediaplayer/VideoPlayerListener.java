package edu.tjrac.swant.simplemediaplayer;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by wpc on 2019-08-21.
 */
public abstract class VideoPlayerListener implements IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnSeekCompleteListener {
}
