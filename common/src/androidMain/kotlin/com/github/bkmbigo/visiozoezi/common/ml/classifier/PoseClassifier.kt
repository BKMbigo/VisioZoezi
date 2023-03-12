package com.github.bkmbigo.visiozoezi.common.ml.classifier

import android.content.Context
import android.util.Log
import com.github.bkmbigo.visiozoezi.common.ml.classifier.PoseClassificationResult
import com.github.bkmbigo.visiozoezi.common.ml.classifier.PoseClassifier
import com.github.bkmbigo.visiozoezi.common.ml.classifier.models.PoseClassificationModel
import com.github.bkmbigo.visiozoezi.common.ml.pose.models.PoseResult
import kotlinx.datetime.Clock
import org.tensorflow.lite.InterpreterApi
import org.tensorflow.lite.support.common.FileUtil
import java.io.IOException
import java.nio.FloatBuffer


actual class PoseClassifier(
    private val interpreterApi: InterpreterApi
) {


    actual fun analyze(
        poseResult: PoseResult, onAnalysisComplete: (PoseClassificationResult) -> Unit
    ) {
        try {
            var output = FloatBuffer.allocate(2)

            val startTime = Clock.System.now()
            interpreterApi.run(
                poseResult.points.map { listOf(it.x, it.y) }.flatten().toFloatArray(), output
            )
            val endTime = Clock.System.now()

            val floatArray = output.array()

            onAnalysisComplete(
                PoseClassificationResult.WithResult(
                    (floatArray[0] > 0.5f), endTime - startTime
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    actual fun close() {
        interpreterApi.close()
    }

    actual class Builder(
        private val context: Context
    ) {

        actual fun build(model: PoseClassificationModel): PoseClassifier {
            val interpreterOptions = InterpreterApi.Options()
            interpreterOptions.numThreads = 2
            return try {
                val modelFile = FileUtil.loadMappedFile(context, model.tfLiteModelName)
                val interpreter = InterpreterApi.create(modelFile, interpreterOptions)
                PoseClassifier(interpreter)
            } catch (e: IOException) {
                Log.e("Pose Classifier", "setUpModel: ")
                throw e
            }
        }

    }

}