package com.capstoneproject.audiproject.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class MapsTherapyResponse(

	@field:SerializedName("indonesia")
	val indonesia: List<IndonesiaItem>
)

@Parcelize
data class IndonesiaItem(

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("lat")
	val lat: String,

	@field:SerializedName("alamat")
	val alamat: String
): Parcelable
