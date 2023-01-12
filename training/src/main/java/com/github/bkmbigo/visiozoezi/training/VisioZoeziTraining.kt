package com.github.bkmbigo.visiozoezi.training

import org.jetbrains.kotlinx.dl.api.core.SavingFormat
import org.jetbrains.kotlinx.dl.api.core.Sequential
import org.jetbrains.kotlinx.dl.api.core.WritingMode
import org.jetbrains.kotlinx.dl.api.core.activation.Activations
import org.jetbrains.kotlinx.dl.api.core.layer.core.Dense
import org.jetbrains.kotlinx.dl.api.core.layer.core.Input
import org.jetbrains.kotlinx.dl.api.core.loss.Losses
import org.jetbrains.kotlinx.dl.api.core.metric.Metrics
import org.jetbrains.kotlinx.dl.api.core.optimizer.Adam
import org.jetbrains.kotlinx.dl.api.summary.printSummary
import java.io.File

fun buildAndTrainModel1() {

    val model = Sequential.of(
        Input(34, name="Input"),
        Dense(outputSize = 152, activation = Activations.Relu),
        Dense(outputSize = 152, activation = Activations.Relu),
        Dense(outputSize = 38, activation = Activations.Relu),
        Dense(outputSize = 1, activation = Activations.Sigmoid)
    )

    model.use {
        it.compile(
            optimizer = Adam(),
            loss = Losses.BINARY_CROSSENTROPY,
            metric = Metrics.ACCURACY
        )

        it.printSummary()

        it.fit(
            dataset = createFromCSVFile("training/src/main/resources/data/total_data.csv", true),
            epochs = 20
        )

        it.save(
            File("training/src/main/resources/models/push_up_pose_estimation_model/"),
            writingMode = WritingMode.OVERRIDE
        )

        // Copy the saved model to the appropriate framework for detection
    }
}

fun main(args: Array<String>) {
    buildAndTrainModel1()
}