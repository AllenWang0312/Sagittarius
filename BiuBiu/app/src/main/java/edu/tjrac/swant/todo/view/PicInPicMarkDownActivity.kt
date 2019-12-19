package edu.tjrac.swant.todo.view

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import edu.tjrac.swant.wjzx.R

@TargetApi(Build.VERSION_CODES.O)
class PicInPicMarkDownActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pic_in_pic_mark_down)
        //        enterPictureInPictureMode();

    }
}
