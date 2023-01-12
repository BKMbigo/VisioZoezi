package com.github.bkmbigo.visiozoezi.training

import org.jetbrains.kotlinx.dl.dataset.OnHeapDataset
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

fun createFromCSVFile(filePath: String, hasHeader: Boolean = false): OnHeapDataset {
    val path = Paths.get(filePath)

    val xList = ArrayList<FloatArray>()
    val yList = ArrayList<Float>()

    try {
        Files.newBufferedReader(path).use { reader ->
            var line = reader.readLine()
            if(hasHeader){
                line = reader.readLine()
            }
            while (line != null) {
                val attributes = line.split(",")
                val tList = attributes.map<String, Float> { attr ->
                    attr.toFloat() // Throws: Number Format Exception
                }.toMutableList()
                // Remove the last element
                val y = tList.removeLast() // Throws: NoSuchElementException
                val x = tList.toFloatArray()
                xList.add(x)
                yList.add(y)

                line = reader.readLine()
            }
        }


        return OnHeapDataset.create(
            xList.toTypedArray(),
            yList.toFloatArray()
        )


    } catch (ioException: IOException) {
        ioException.printStackTrace()

        throw ioException
    } catch (e: NumberFormatException) {
        e.printStackTrace()

        throw e
    } catch (e: NoSuchElementException) {
        e.printStackTrace()

        throw e
    }

}