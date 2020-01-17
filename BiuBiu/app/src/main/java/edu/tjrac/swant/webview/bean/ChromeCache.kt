package edu.tjrac.swant.webview.bean

import android.os.Parcel
import android.os.Parcelable
import edu.tjrac.swant.webview.Histroy

/**
 * Created by wpc on 2020-01-15.
 */
class ChromeCache() : Parcelable {

    var createAt: Long? = 0L
    var updateAt: Long? = 0L
    var histroies :ArrayList<Histroy>?= ArrayList()

    constructor(parcel: Parcel) : this() {
        createAt = parcel.readValue(Long::class.java.classLoader) as? Long
        updateAt = parcel.readValue(Long::class.java.classLoader) as? Long
//        histroies = parcel.readArray(Histroy::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(createAt)
        parcel.writeValue(updateAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChromeCache> {
        override fun createFromParcel(parcel: Parcel): ChromeCache {
            return ChromeCache(parcel)
        }

        override fun newArray(size: Int): Array<ChromeCache?> {
            return arrayOfNulls(size)
        }
    }


}