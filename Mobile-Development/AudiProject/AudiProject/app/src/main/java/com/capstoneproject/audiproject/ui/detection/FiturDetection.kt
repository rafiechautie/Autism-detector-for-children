package com.capstoneproject.audiproject.ui.detection

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.audiproject.R
import com.capstoneproject.audiproject.data.remote.response.IndonesiaItem
import com.capstoneproject.audiproject.utils.labels.Labels
import com.capstoneproject.audiproject.databinding.ActivityFiturDetectionBinding
import com.capstoneproject.audiproject.ml.Mobilenetv2Ft
import com.capstoneproject.audiproject.ml.ModelUnquant
import com.capstoneproject.audiproject.ui.home.consultation.ConsultationActivity
import com.capstoneproject.audiproject.ui.home.dashboard.HomeFragment
import com.capstoneproject.audiproject.ui.home.map.MapViewModel
import com.capstoneproject.audiproject.ui.home.map.MapsTherapyActivity
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import kotlin.math.roundToInt

class FiturDetection : AppCompatActivity() {

    private lateinit var binding: ActivityFiturDetectionBinding
    private val tfImage = TensorImage(DataType.FLOAT32)

    private lateinit var arrayListMaps: ArrayList<IndonesiaItem>
    private lateinit var viewModel: MapViewModel

    private val tfImageProcessor by lazy {
        ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 1f))
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFiturDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()



        viewModel = ViewModelProvider(this)[MapViewModel::class.java]
        viewModel.findLocation()

        viewModel.listLocation.observe(this){
            binding.apply {
                arrayListMaps = ArrayList()
                arrayListMaps.addAll(it)
            }
        }

        getUriBundle()
        setupButton()
    }

    private fun setupButton() {
        binding.apply {
            btnBack.setOnClickListener {
                onBackPressed()
                finish()
            }

            btnConsultation.setOnClickListener {
                startActivity(Intent(this@FiturDetection, ConsultationActivity::class.java))
            }

            btnNearestTheraphy.setOnClickListener {
                val intent = Intent(this@FiturDetection, MapsTherapyActivity::class.java)
                intent.putExtra(ARRAY_LIST_MAPS, arrayListMaps)
                startActivity(intent)
            }
        }
    }

    private fun getUriBundle() {
        val uriBundle: Bundle? = intent.extras
        if (uriBundle != null && uriBundle.containsKey(URI_IMAGE)) {
            val uriString = intent.extras?.getString(URI_IMAGE)
            val uri = Uri.parse(uriString)

            convertUriToBitmap(uri)
        }
    }

    private fun convertUriToBitmap(uri: Uri?) {
        if (uri != null) {
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            imagePredict(bitmap)
        }
    }

    //prediksi image
    private fun imagePredict(bitmap: Bitmap?) {
        val fileLabels = "labels.txt"
        val stringLabels = application.assets.open(fileLabels).bufferedReader().use { it.readText() }
        val townList = stringLabels.split("\n")
        val modelTensorflow = Mobilenetv2Ft.newInstance(this)

        if (bitmap != null) {
            val inputImage = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            tfImage.load(inputImage)
        }

        val tensorImage = tfImageProcessor.process(tfImage)
        val outputs = modelTensorflow.process(tensorImage.tensorBuffer).outputFeature0AsTensorBuffer

        val result = mutableListOf<Labels>()
        for (i in 0 until outputs.floatArray.size) {
            result.add(Labels(townList[i], outputs.floatArray[i]))
        }

        val outputResults = result.apply {
            sortByDescending {
                it.predictScore
            }
        }
        showOutputs(outputResults, bitmap)
        modelTensorflow.close()
    }

    private fun showOutputs(outputResults: MutableList<Labels>, bitmap: Bitmap?) {
        binding.imgPreview.setImageBitmap(bitmap)

        val score = outputResults[0].predictScore
        val roundingScore = (score * 100.0).roundToInt()

//        if (outputResults[0].labelName == "autistic") {
//            binding.resultLabel.text = getString(R.string.if_autistic)
//            binding.btnConsultation.visibility = View.VISIBLE
//            binding.btnNearestTheraphy.visibility = View.VISIBLE
//        } else if(outputResults[0].labelName == "non autistic") {
//            binding.resultLabel.text = getString(R.string.if_non_autistic)
//        }
        binding.resultLabel.text = outputResults[0].labelName
        binding.btnConsultation.visibility = View.VISIBLE
        binding.btnNearestTheraphy.visibility = View.VISIBLE
        binding.resultScore.text = roundingScore.toString() + "%"

    }

    companion object {
        const val URI_IMAGE = "uri"
        const val ARRAY_LIST_MAPS = "array_list_maps"
    }
}