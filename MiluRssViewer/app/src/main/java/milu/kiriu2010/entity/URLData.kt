package milu.kiriu2010.entity

import android.os.Parcel
import android.os.Parcelable
import java.net.URL

/*
http://www.parcelabler.com/
    protected URLData(Parcel in) {
        title = in.readString();
        url = (URL) in.readValue(URL.class.getClassLoader());
    }
 */
data class URLData( val id: Int, val genreId: Int, val title: String, val url: URL ): Parcelable {
    constructor( parcel: Parcel ): this(
            // id
            parcel.readInt(),
            // genreId
            parcel.readInt(),
            // title
            parcel.readString() ?: "",
            // URL
            parcel.readValue(URL::class.java.classLoader) as URL
    )

    override fun writeToParcel( dest: Parcel?, flag: Int) {
        dest?.let {
            it.writeInt(id)
            it.writeInt( genreId )
            it.writeString( title )
            it.writeValue( url )
        }
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<URLData> {
        override fun createFromParcel(parcel: Parcel): URLData {
            return URLData(parcel)
        }

        override fun newArray(size: Int): Array<URLData?> {
            return arrayOfNulls(size)
        }
    }

}
