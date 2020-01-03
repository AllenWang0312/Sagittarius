package edu.tjrac.swant.biubiu_launcher;

import android.media.MediaPlayer;
import android.net.Uri;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by wpc on 2020-01-03.
 */
public class LiveWallpaperService extends WallpaperService {

    private MediaPlayer mediaPlayer;

    @Override
    public Engine onCreateEngine() {
        return new LiveWallpaperEngine();
    }

    class LiveWallpaperEngine extends Engine {
        @Override
        public SurfaceHolder getSurfaceHolder() {
            return super.getSurfaceHolder();
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);

            initMediaPlayer(holder);
        }

        private void initMediaPlayer(SurfaceHolder holder) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });

            try {
                mediaPlayer.setSurface(holder.getSurface());
                Uri mUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_screen);
                mediaPlayer.setDataSource(LiveWallpaperService.this, mUri);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);

            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
    }
}
