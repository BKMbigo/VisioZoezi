package com.github.bkmbigo.visiozoezi.common.ml.pose

import ai.djl.Application
import ai.djl.modality.cv.Image
import ai.djl.modality.cv.ImageFactory
import ai.djl.modality.cv.output.Joints
import ai.djl.modality.cv.util.NDImageUtils
import ai.djl.ndarray.NDArray
import ai.djl.ndarray.NDList
import ai.djl.ndarray.types.DataType
import ai.djl.repository.zoo.Criteria
import ai.djl.repository.zoo.ZooModel
import ai.djl.tensorflow.zoo.TfModelZoo
import ai.djl.training.util.ProgressBar
import ai.djl.translate.NoBatchifyTranslator
import ai.djl.translate.TranslatorContext
import com.github.bkmbigo.visiozoezi.common.ml.pose.models.AnalysisResult
import com.github.bkmbigo.visiozoezi.common.ml.pose.models.ImageMetadata
import com.github.bkmbigo.visiozoezi.common.ml.pose.models.PoseResult
import com.github.bkmbigo.visiozoezi.common.presentation.components.camera.CameraImage
import kotlinx.datetime.Clock
import kotlin.time.DurationUnit

actual class ImageAnalyzer(
    private val model: ZooModel<Image, Joints>
) {
    actual fun analyze(
        image: CameraImage,
        uiUpdateCallback: (AnalysisResult) -> Unit,
        isImageFlipped: Boolean
    ) {
        try {

            val startTime = Clock.System.now()
            val result =
                model.newPredictor().predict(ImageFactory.getInstance().fromImage(image.image))
            val endTime = Clock.System.now()

            uiUpdateCallback(
                if (result.joints.isEmpty()) {
                    AnalysisResult.Empty(
                        processTime = (endTime - startTime).toLong(DurationUnit.MILLISECONDS)
                    )
                } else {
                    AnalysisResult.WithResult(
                        poseResult = PoseResult(
                            points = result.joints.toPosePoints()
                        ),
                        processTime = (endTime - startTime).toLong(DurationUnit.MILLISECONDS),
                        imageMetadata = ImageMetadata(
                            width = image.width,
                            height = image.height,
                            isImageFlipped = isImageFlipped,
                            rotationDegrees = 0
                        )
                    )
                }
            )
        } catch (_: Exception) {
        }
    }

    actual fun close() {
        model.close()
    }

    actual companion object {
        actual class Builder() {
            val criteria = Criteria.builder()
                .optApplication(Application.CV.POSE_ESTIMATION)
                .setTypes(Image::class.java, Joints::class.java)
                .optModelUrls("https://tfhub.dev/google/movenet/singlepose/lightning/4?tf-hub-format=compressed")
                .optProgress(ProgressBar())
                .optTranslator(MyTranslator())
                .build()


            actual fun build(): ImageAnalyzer {
                return ImageAnalyzer(TfModelZoo.loadModel(criteria))
            }


            class MyTranslator : NoBatchifyTranslator<Image, Joints> {
                override fun processInput(ctx: TranslatorContext?, input: Image?): NDList {
                    return if (ctx == null || input == null) {
                        NDList(192 * 192 * 3) // Ensure the model is fed data
                    } else {
                        var array = input.toNDArray(ctx.ndManager, Image.Flag.COLOR)
                        array = NDImageUtils.resize(array, 192)
                        array = array.reshape(1, 192, 192, 3)
                        array = array.toType(DataType.INT32, true)
                        NDList(array)
                    }
                }

                override fun processOutput(ctx: TranslatorContext?, list: NDList?): Joints {
                    if (ctx == null || list == null) {
                        return Joints(emptyList())
                    } else {

                        var value = FloatArray(1)

                        for (array: NDArray in list) {
                            value = array.get(0).toFloatArray()
                        }

                        val y = FloatArray(17)
                        val x = FloatArray(17)
                        val conf = FloatArray(17)

                        for (i in 0..16) {
                            y[i] = value[i * 3]
                            x[i] = value[i * 3 + 1]
                            conf[i] = value[i * 3 + 2]
                        }

                        val joints = mutableListOf<Joints.Joint>()

                        if (conf.maxOf { it } > 0.5f) {
                            for (i in 0..16) {
                                joints.add(
                                    Joints.Joint(
                                        x[i].toDouble(),
                                        y[i].toDouble(),
                                        conf[i].toDouble()
                                    )
                                )
                            }
                        }

                        return Joints(joints)
                    }
                }
            }
        }
    }

}