package com.capstoneproject.audiproject.ui.detection

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.audiproject.databinding.ActivityAutismDetectionBinding
import com.capstoneproject.audiproject.utils.tensorflow.Classifier
import com.capstoneproject.audiproject.utils.tensorflow.TensorflowImageClassifier
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AutismDetectionActivity: AppCompatActivity() {

    private val MODEL_PATH = "model_unquant.tflite"
    private val LABEL_PATH = "labels.txt"
    private val INPUT_SIZE = 224

    private var classifier: Classifier? = null
    private val compositeDisposable: CompositeDisposable? = null

    private lateinit var binding: ActivityAutismDetectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAutismDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        getUriData()

        initTensorFlow()?.subscribe(object : CompletableObserver {
            override fun onSubscribe(d: Disposable) {
                compositeDisposable?.add(d)
            }

            override fun onComplete() {
                Log.i("initTensorFlow", "complete")
                showResult(false, null, "")

            }

            override fun onError(e: Throwable) {
                Log.e("initTensorFlow", e.message!!)
            }
        })
    }

    private fun getUriData() {
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
            val convertBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false)
            val results: List<Classifier.Recognition?>? = classifier?.recognizeImage(convertBitmap)
            Log.d("resultss", results.toString())

            showResult(true, convertBitmap, generateResults(results))
        }
    }

    private fun generateResults(data: List<Classifier.Recognition?>?): String {
        var result = ""
        result = """
             $result
             ${data?.get(0)}
             """.trimIndent()

        return result
    }

    private fun closeClassifier(): Completable? {
        return Completable.fromAction { classifier?.close() }.subscribeOn(Schedulers.newThread())
    }


    private fun initTensorFlow(): Completable? {
        return Completable.fromAction {
            classifier = TensorflowImageClassifier.create(
                assets,
                MODEL_PATH,
                LABEL_PATH,
                INPUT_SIZE
            )
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }

    override fun onDestroy() {
        closeClassifier()?.subscribe(object : CompletableObserver {
            override fun onSubscribe(d: Disposable) {
                compositeDisposable?.add(d)
            }

            override fun onComplete() {
                Log.i("closeClassifier", "completed")
            }

            override fun onError(e: Throwable) {
                Log.e("closeClassifier", e.message!!)
            }
        })
        super.onDestroy()
        compositeDisposable?.clear()
    }

    private fun showResult(show: Boolean, convertBitmap: Bitmap?, results: String) {
        if (show) {
            val output = results.replace("\\d".toRegex(), "")
            binding.resultLabel.text = output
            binding.imgPreview.setImageBitmap(convertBitmap)
        }
    }

    companion object {
        const val URI_IMAGE = "uri"
    }
}

