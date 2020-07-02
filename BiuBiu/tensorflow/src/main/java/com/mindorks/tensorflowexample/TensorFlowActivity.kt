/*
 *    Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.mindorks.tensorflowexample

import android.graphics.Bitmap
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.wonderkiln.camerakit.*
import edu.tjrac.swant.tensorflow.R
import java.util.concurrent.Executors

@Route(path="/tensorflow/main")
class TensorFlowActivity : AppCompatActivity() {

    private var classifier: Classifier? = null
    private val executor = Executors.newSingleThreadExecutor()
    private var textViewResult: TextView? = null
    private var btnDetectObject: Button? = null
    private var btnToggleCamera: Button? = null
    private var imageViewResult: ImageView? = null
    private var cameraView: CameraView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tensorflow_main)
        cameraView = findViewById<View>(R.id.cameraView) as CameraView
        imageViewResult = findViewById<View>(R.id.imageViewResult) as ImageView
        textViewResult = findViewById<View>(R.id.textViewResult) as TextView
        textViewResult?.movementMethod = ScrollingMovementMethod()

        btnToggleCamera = findViewById<View>(R.id.btnToggleCamera) as Button
        btnDetectObject = findViewById<View>(R.id.btnDetectObject) as Button

        cameraView?.addCameraKitListener(object : CameraKitEventListener {
            override fun onEvent(cameraKitEvent: CameraKitEvent) {

            }

            override fun onError(cameraKitError: CameraKitError) {

            }

            override fun onImage(cameraKitImage: CameraKitImage) {

                var bitmap = cameraKitImage.bitmap

                bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false)

                imageViewResult?.setImageBitmap(bitmap)

                val results = classifier?.recognizeImage(bitmap)
                val sb = StringBuffer()
                for (i in results!!) {
                    sb.append(i.toString())
                }
                textViewResult?.text = sb.toString()

            }

            override fun onVideo(cameraKitVideo: CameraKitVideo) {

            }
        })

        btnToggleCamera?.setOnClickListener { cameraView?.toggleFacing() }

        btnDetectObject?.setOnClickListener { cameraView?.captureImage() }

        initTensorFlowAndLoadModel()
    }

    override fun onResume() {
        super.onResume()
        cameraView?.start()
    }

    override fun onPause() {
        cameraView?.stop()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.execute { classifier?.close() }
    }

    private fun initTensorFlowAndLoadModel() {
        executor.execute {
            try {
                classifier = TensorFlowImageClassifier.create(
                        assets,
                        MODEL_FILE,
                        LABEL_FILE,
                        INPUT_SIZE,
                        IMAGE_MEAN,
                        IMAGE_STD,
                        INPUT_NAME,
                        OUTPUT_NAME)
                makeButtonVisible()
            } catch (e: Exception) {
                throw RuntimeException("Error initializing TensorFlow!", e)
            }
        }
    }

    private fun makeButtonVisible() {
        runOnUiThread { btnDetectObject?.isEnabled = true }
    }

    companion object {

        private val INPUT_SIZE = 224
        private val IMAGE_MEAN = 117
        private val IMAGE_STD = 1f
        private val INPUT_NAME = "input"
        private val OUTPUT_NAME = "output"

        private val MODEL_FILE = "file:///android_asset/tensorflow_inception_graph.pb"
        private val LABEL_FILE = "file:///android_asset/imagenet_comp_graph_label_strings_cn.txt"
    }
}
