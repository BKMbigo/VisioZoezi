package com.github.bkmbigo.visiozoezi.training

import org.apache.commons.io.FilenameUtils
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.nd4j.common.resources.Downloader
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.lossfunctions.impl.LossMCXENT
import java.io.File
import java.net.URL


/**
 * Basic example for importing a Keras Sequential model into DL4J for training or inference.
 *
 * Let's say you want to create a simple MLP in Keras using the Sequential API. The model
 * takes mini-batches of vectors of length 100, has two Dense layers and predicts a total
 * of 10 categories. Such a model can be defined and serialized in HDF5 format as follows:
 *
 *
 * ```
 * from keras.models import Sequential
 * from keras.layers import Dense
 *
 * model = Sequential()
 * model.add(Dense(units=64, activation='relu', input_dim=100))
 * model.add(Dense(units=10, activation='softmax'))
 * model.compile(loss='categorical_crossentropy', optimizer='sgd', metrics=['accuracy'])
 *
 * model.save('simple_mlp.h5')
 * ```
 * This model hasn't been fit on any data yet, it's just the model definition with initial weights
 * and training configuration as provided in the compile step.
 *
 * You don't need to create this model yourself to run this example, we stored it in the resources
 * folder under 'modelimport/keras' for you.
 *
 * This example shows you how to load the saved Keras model into DL4J for further use. You could
 * either use the imported model for inference only, or train it on data in DL4J.
 *
 *
 * @author Max Pumperla
 */
object SimpleSequentialMlpImport {
    var dataLocalPath: String? = null
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        dataLocalPath = DownloaderUtility.MODELIMPORT.Download()
        val SIMPLE_MLP = File(dataLocalPath, "keras/simple_mlp.h5").absolutePath

        // Keras Sequential models correspond to DL4J MultiLayerNetworks. We enforce loading the training configuration
        // of the model as well. If you're only interested in inference, you can safely set this to 'false'.
        val model: MultiLayerNetwork =
            KerasModelImport.importKerasSequentialModelAndWeights(SIMPLE_MLP, true)

        // Test basic inference on the model.
        val input: INDArray = Nd4j.create(256, 100)
        val output: INDArray = model.output(input)

        // Test basic model training.
        model.fit(input, output)
        assert(
            model.conf().getOptimizationAlgo() == OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT
        )

        // The first layer is a dense layer with 100 input and 64 output units, with RELU activation
        val first: org.deeplearning4j.nn.api.Layer = model.getLayer(0)
        val firstConf: org.deeplearning4j.nn.conf.layers.DenseLayer =
            first.conf().getLayer() as org.deeplearning4j.nn.conf.layers.DenseLayer
        assert(firstConf.getActivationFn() == org.nd4j.linalg.activations.Activation.RELU.getActivationFunction())
        assert(firstConf.getNIn() == 100L)
        assert(firstConf.getNOut() == 64L)

        // The second later is a dense layer with 64 input and 10 output units, with Softmax activation.
        val second: org.deeplearning4j.nn.api.Layer = model.getLayer(1)
        val secondConf: org.deeplearning4j.nn.conf.layers.DenseLayer =
            second.conf().getLayer() as org.deeplearning4j.nn.conf.layers.DenseLayer
        assert(secondConf.getActivationFn() == org.nd4j.linalg.activations.Activation.SOFTMAX.getActivationFunction())
        assert(secondConf.getNIn() == 64L)
        assert(secondConf.getNOut() == 10L)

        // The loss function of the Keras model gets translated into a DL4J LossLayer, which is the final
        // layer in this MLP.
        val loss: org.deeplearning4j.nn.api.Layer = model.getLayer(2)
        val lossConf: org.deeplearning4j.nn.conf.layers.LossLayer =
            loss.conf().getLayer() as org.deeplearning4j.nn.conf.layers.LossLayer
        assert(lossConf.getLossFn() is LossMCXENT)
    }
}

enum class DownloaderUtility
/**
 * Downloads a zip file from a base url to a specified directory under the user's home directory
 *
 * @param baseURL    URL of file
 * @param zipFile    Name of zipfile to download from baseURL i.e baseURL+"/"+zipFile gives full URL
 * @param dataFolder The folder to extract to under ~/dl4j-examples-data
 * @param md5        of zipfile
 * @param dataSize   of zipfile
 */(
    private val BASE_URL: String,
    private val ZIP_FILE: String,
    private val DATA_FOLDER: String,
    private val MD5: String,
    private val DATA_SIZE: String
) {
    MODELIMPORT(
        "modelimport.zip",
        "dl4j-examples",
        "411df05aace1c9ff587e430a662ce621",
        "3MB"
    ),
    BERTEXAMPLE(
        "https://dl4jdata.blob.core.windows.net/testresources",
        "bert_mrpc_frozen_v1.zip",
        "bert-frozen-example",
        "7cef8bbe62e701212472f77a0361f443",
        "420MB"
    ),
    TFIMPORTEXAMPLES(
        "resources.zip",
        "tf-import-examples",
        "4895e40e71b17799e4d6fb75d5a22491",
        "3MB"
    );

    /**
     * For use with resources uploaded to Azure blob storage.
     *
     * @param zipFile    Name of zipfile. Should be a zip of a single directory with the same name
     * @param dataFolder The folder to extract to under ~/dl4j-examples-data
     * @param md5        of zipfile
     * @param dataSize   of zipfile
     */
    constructor(zipFile: String, dataFolder: String, md5: String, dataSize: String) : this(
        AZURE_BLOB_URL + "/" + dataFolder, zipFile, dataFolder, md5, dataSize
    ) {
    }

    @JvmOverloads
    @Throws(java.lang.Exception::class)
    fun Download(returnSubFolder: Boolean = true): String {
        val dataURL = "$BASE_URL/$ZIP_FILE"
        val downloadPath = FilenameUtils.concat(System.getProperty("java.io.tmpdir"), ZIP_FILE)
        val extractDir = FilenameUtils.concat(
            System.getProperty("user.home"),
            "dl4j-examples-data/$DATA_FOLDER"
        )
        if (!File(extractDir).exists()) File(extractDir).mkdirs()
        var dataPathLocal = extractDir
        if (returnSubFolder) {
            val resourceName = ZIP_FILE.substring(0, ZIP_FILE.lastIndexOf(".zip"))
            dataPathLocal = FilenameUtils.concat(extractDir, resourceName)
        }
        val downloadRetries = 10
        if (!File(dataPathLocal).exists() || File(dataPathLocal).list().size == 0) {
            println("_______________________________________________________________________")
            println("Downloading data ($DATA_SIZE) and extracting to \n\t$dataPathLocal")
            println("_______________________________________________________________________")
            Downloader.downloadAndExtract(
                "files",
                URL(dataURL),
                File(downloadPath),
                File(extractDir),
                MD5,
                downloadRetries
            )
        } else {
            println("_______________________________________________________________________")
            println("Example data present in \n\t$dataPathLocal")
            println("_______________________________________________________________________")
        }
        return dataPathLocal
    }

    companion object {
        private const val AZURE_BLOB_URL = "https://dl4jdata.blob.core.windows.net/dl4j-examples"
    }
}