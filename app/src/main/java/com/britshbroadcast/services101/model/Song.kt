package com.britshbroadcast.services101.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song (val songInfo: String, val songResource: Int, val albumCover: String): Parcelable