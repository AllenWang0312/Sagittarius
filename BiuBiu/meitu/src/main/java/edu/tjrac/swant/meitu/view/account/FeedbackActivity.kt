package edu.tjrac.swant.meitu.view.account

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import edu.tjrac.swant.baselib.common.base.BaseBarActivity
import edu.tjrac.swant.baselib.util.StringUtils
import edu.tjrac.swant.common.adapter.UploadImageAdapter
import edu.tjrac.swant.common.bean.FileInfo
import edu.tjrac.swant.meitu.App
import edu.tjrac.swant.meitu.R
import edu.tjrac.swant.meitu.net.BR
import edu.tjrac.swant.meitu.net.NESubscriber
import edu.tjrac.swant.meitu.net.Net
import kotlinx.android.synthetic.main.activity_feedback.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by wpc on 2019-12-10.
 */
class FeedbackActivity : BaseBarActivity(),View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_phone -> {
//                startActivity(IntentUtil.getCallPhoneIntent(App.hot_line!!))
            }
            R.id.bt_submit -> {
                var content = et_content.text.toString().trim()
                var mobile = et_contract.text.toString().trim()

                if (content.isEmpty()) {
                    showToast("请填写问题描述")
                }
//                else if (StringUtils.isEmpty(mobile) || !StringUtils.isMobileNO(mobile)) {
//                    showToast("请填写正确的手机号")
//                }
//                else if(null==images||StringUtils.isEmpty(images.get(0).absPath)){
//                    showToast("请至少添加一张问题截图哦")
//                }else {
                if (images.size > 1) {
                    Net.instance.getApiService().uploadFiles(getBody(getFiles(images)))
                            .unsubscribeOn(Schedulers.io())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : NESubscriber<BR<ArrayList<String>>>(this) {
                                override fun onStart() {
                                    super.onStart()
                                    showProgressDialog("图片上传中")
                                }

                                override fun onSuccess(t: BR<ArrayList<String>>?) {
                                    dismissProgressDialog()
                                    if (t?.data !== null && t?.data?.size!! > 0) {
                                        submitFeedback(content, mobile, t?.data)
                                    }
                                }

                                override fun onCompleted() {
                                    super.onCompleted()
                                    dismissProgressDialog()
                                }
                            })
                } else {
                    submitFeedback(content, mobile, null)
                }

//                }

            }
        }
    }

    private fun submitFeedback(content: String, mobile: String?, data: ArrayList<String>?) {
        Net.instance.getApiService().feedback(App.token!!,
                data,
                content,
                mobile
        ).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : NESubscriber<BR<Objects>>(this@FeedbackActivity) {
                    override fun onSuccess(t: BR<Objects>?) {
                        showToast("提交成功")
                        finish()
                    }
                })
    }


    var images: ArrayList<FileInfo> = ArrayList()
    lateinit var adapter: UploadImageAdapter

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        setToolbar(findViewById(R.id.toolbar))


        recycler.layoutManager = GridLayoutManager(mContext, 4)
        images.add(FileInfo())
        adapter = UploadImageAdapter(4, images,R.layout.item_upload_file)
        adapter.infoView = tv_image_info
        adapter.setOnItemClickListener { adapter, view, position ->
            Log.i("playing_position", position.toString())
            if (position == images.size - 1) {
                // 进入相册 以下是例子：用不到的api可以不写
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                        .theme(R.style.picture_white_style)
                        .maxSelectNum(5 - images.size)// 最大图片选择数量 int
                        .imageSpanCount(4)// 每行显示个数 int
                        .previewImage(true)// 是否可预览图片 true or false
                        .isCamera(true)// 是否显示拍照按钮 true or false
                        .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .compress(true)// 是否压缩 true or false
                        .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                        .cropCompressQuality(50)// 裁剪压缩质量 默认90 int
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        .synOrAsy(true)//同步true或异步false 压缩 默认同步
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
            } else {

            }
        }
        et_content.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(500))

        et_content.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                var length = s.toString().length
                tv_num.text = (500 - length).toString()

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        recycler.adapter = adapter
        bt_submit.setOnClickListener(this)
//     Log.i("itemCount",adapter?.itemCount?.toString())
        tv_phone.text = App.hot_line
        tv_phone.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (null == resultCode) {

        } else
            if (resultCode == Activity.RESULT_OK) {
                when (requestCode) {
                    PictureConfig.CHOOSE_REQUEST -> {
                        val selectList = PictureSelector.obtainMultipleResult(data)
                        if (null === selectList || null == selectList || selectList.isEmpty() || selectList.size == 0) {

                        } else {
                            for (i in selectList) {
                                images.add(0, FileInfo(PictureConfig.TYPE_IMAGE,i.compressPath))
                            }
                            adapter.notifyDataSetChanged()
                            tv_image_info.text = if (images.size == 5) {
                                "最多只支持4张图片哦"
                            } else {
                                "还可上传" + (5 - images.size) + "张图片，单张图片最大3M"
                            }
                        }
                    }
                }
            }
    }

    companion object {
        fun getBody(files: ArrayList<File>): Array<MultipartBody.Part> {
            var array = Array<MultipartBody.Part>(files?.size) { i ->
                MultipartBody.Part.createFormData("files[]", files.get(i).name, RequestBody.create(MediaType.parse("image/png"), files.get(i)))
            }
            return array
        }

        fun getBody(map: HashMap<String, String>): Array<MultipartBody.Part?> {
            var array = arrayOfNulls<MultipartBody.Part>(map.keys.size)
            var index = 0
            for (i in map.keys) {
                var file = File(map.get(i))
                if (file.exists()) {
                    array[index] = MultipartBody.Part.createFormData(i, file.name, RequestBody.create(MediaType.parse("image/png"), file))
                    index++
                }
            }
            return array!!
        }

        fun getBody(map: HashMap<String, String>, token: String, cert_id: String): Array<MultipartBody.Part?> {
            var array = arrayOfNulls<MultipartBody.Part>(map.keys.size + 2)
            var index = 0
            for (i in map.keys) {
                var path = map.get(i)
                Log.e("path", path)
                var file = File(path)
                if (file.exists()) {
                    array[index] = MultipartBody.Part.createFormData(i, file.name, RequestBody.create(MediaType.parse("image/png"), file))
                    index++
                }
            }
            array[map.keys.size] = MultipartBody.Part.createFormData("user_ticket", token)
            array[map.keys.size + 1] = MultipartBody.Part.createFormData("cert_id", cert_id)
            return array!!
        }

        //        fun getFiles(selectList: List<LocalMedia>?): Array<File> {
//            var array = Array<File>(selectList?.size, { i -> File(selectList.get(i).cutPath) })
//            return array
//        }
        fun getFiles(selectList: ArrayList<FileInfo>): ArrayList<File> {
            var files = ArrayList<File>()
            for (i in selectList) {
                if (StringUtils.isEmpty(i.absPath)) {

                } else {
                    files.add(File(i.absPath))
                }
            }
            return files!!
//        var array = Array<File>(selectList!!.size, { i -> File(selectList.get(i).absPath) })
//        return array
        }

        fun getFiles(selectList: List<LocalMedia>): ArrayList<File> {
            var files = ArrayList<File>()
            for (i in selectList) {
                if (i.path.isEmpty()) {

                } else {
                    files.add(File(i.path))
                }
            }
            return files!!
//        var array = Array<File>(selectList!!.size, { i -> File(selectList.get(i).absPath) })
//        return array
        }
//        fun getParts(medias:List<LocalMedia>?):Array<MultipartBody.Part>{
//            var array=Array<MultipartBody.Part>(medias!!.size,{i->
//                MultipartBody.Part.createFormData("imageFiles[]", File(medias.get(i).cutPath).name, RequestBody.create(MediaType.parse("image/png"), File(medias.get(i).cutPath)))
//            })
//        }
    }


    override fun setToolbar(tool: android.view.View) {
        super.setToolbar(tool)
        enableBackIcon()
        setTitle("意见反馈")
    }

}