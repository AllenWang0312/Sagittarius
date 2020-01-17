package edu.tjrac.swant.webview

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by wpc on 2020-01-15.
 */
class Histroy : Parcelable {
    var title: String? = ""
    var url: String? = ""
    var icon: Bitmap? = null

    constructor(parcel: Parcel) {
        title = parcel.readString()
        url = parcel.readString()
        icon = parcel.readParcelable(Bitmap::class.java.classLoader)
    }

    constructor(title: String?, url: String?, icon: Bitmap?) {
        this.title = title
        this.url = url
        this.icon = icon
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(url)
        parcel.writeParcelable(icon, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Histroy> {
        override fun createFromParcel(parcel: Parcel): Histroy {
            return Histroy(parcel)
        }

        override fun newArray(size: Int): Array<Histroy?> {
            return arrayOfNulls(size)
        }
    }
}