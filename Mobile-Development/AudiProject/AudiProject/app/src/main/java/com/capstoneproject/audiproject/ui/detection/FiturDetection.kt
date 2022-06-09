package com.capstoneproject.audiproject.ui.detection

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.capstoneproject.audiproject.Labels
import com.capstoneproject.audiproject.R
import com.capstoneproject.audiproject.databinding.ActivityFiturDetectionBinding
import com.capstoneproject.audiproject.ml.Inceptionv301
import com.capstoneproject.audiproject.ml.Mobilenet01
import com.capstoneproject.audiproject.ml.ModelUnquant
import com.capstoneproject.audiproject.ui.home.HomePageActivity
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import kotlin.math.roundToInt

class FiturDetection : AppCompatActivity() {

    private lateinit var binding: ActivityFiturDetectionBinding
    private val tfImage = TensorImage(DataType.FLOAT32)

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

        getUriBundle()
        setupButton()
    }

    private fun setupButton() {
        binding.apply {

            btnBack.setOnClickListener {
                startActivity(Intent(this@FiturDetection, HomePageActivity::class.java))
                finish()
            }

        }
    }

    private fun getUriBundle() {
        val uriBundle: Bundle? = intent.extras
        if (uriBundle != null && uriBundle.containsKey(AutismDetectionActivity.URI_IMAGE)) {
            val uriString = intent.extras?.getString(AutismDetectionActivity.URI_IMAGE)
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

    private fun imagePredict(bitmap: Bitmap?) {
        val fileLabels = "labels.txt"
        val stringLabels = application.assets.open(fileLabels).bufferedReader().use { it.readText() }
        val townList = stringLabels.split("\n")
        val modelTensorflow = Mobilenet01.newInstance(this)

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

        binding.resultScore.text = outputResults[0].predictScore.toString()
        binding.resultLabel.text = outputResults[0].labelName
    }

    companion object {
        const val URI_IMAGE = "uri"
    }
}