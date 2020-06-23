package edu.tjrac.swant.common.bean

import android.os.Parcel
import android.os.Parcelable

import edu.tjrac.swant.baselib.common.base.BaseApplication
import org.json.JSONObject

import java.io.File
import java.io.Serializable

class FileInfo :Serializable, Parcelable ,JSONObject{
    //    val PictureConfig.TYPE_IMAGE = 1
//    val PictureConfig.TYPE_VIDEO = 2
//    val PictureConfig.TYPE_AUDIO = 3
    var type = 0
    var fileName: String? = ""
    var url: String? = ""//文件是否上传成功依据
    //        File dir;
    //        if (StringUtils.isEmpty(savePath)) {
    //            dir = new File(savePath);
    //            if (!dir.exists()) {
    //                dir.mkdirs();
    //            }
    //
    //        }
    var savePath: String? = ""
    var absPath: String? = ""

    constructor(fileName: String, url: String, cachePath: String?) {
        this.fileName = fileName
        this.url = url
        if (cachePath == null) {
            savePath = BaseApplication.instance?.cacheDir?.absolutePath!!
        } else {
            savePath = cachePath
        }
    }

    constructor(fileName: String, url: String) {
        this.fileName = fileName
        this.url = url
        savePath = BaseApplication.instance?.cacheDir?.absolutePath!!
    }
    constructor( url: String) {
        this.url = url
    }

    constructor(type: Int, path: String) {
        val file = File(path)
        this.type = type
        fileName = file.name
        absPath = path
        savePath = absPath?.substring(0, absPath?.lastIndexOf("/")!!)
    }

    constructor() {}

    protected constructor(`in`: Parcel) {
        fileName = `in`.readString()
        url = `in`.readString()
        savePath = `in`.readString()
        absPath = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(fileName)
        dest.writeString(url)
        dest.writeString(savePath)
        dest.writeString(absPath)
    }

    companion object CREATOR : Parcelable.Creator<FileInfo> {
        override fun createFromParcel(parcel: Parcel): FileInfo {
            return FileInfo(parcel)
        }

        override fun newArray(size: Int): Array<FileInfo?> {
            return arrayOfNulls(size)
        }
    }
}
