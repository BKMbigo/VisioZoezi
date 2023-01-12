package com.github.bkmbigo.visiozoezi.common.ml.classifier.models

import com.github.bkmbigo.visiozoezi.common.ml.pose.models.PoseResult

enum class PoseClassificationModel (
    val exerciseIdList: List<Long>,     // Contains a list of id's of supported exercise
    val tfLiteModelName: String,    // Name of tfLite model (used by android application)
    val savedModelPathName: String    // Path to the saved model created using KotlinDl
) {

    // The model classifies a person performing any type of push up exercise --> Returns true when the person
    PUSH_UP_POSE_CLASSIFICATION_MODEL(
        exerciseIdList = listOf(),
        tfLiteModelName = "pose_classifier_push_up.tflite",
        savedModelPathName = "push_up_model_pose_classifier"
    );

    companion object {
        val poseClassifiedExerciseList = PoseClassificationModel.values()
            .map { it.exerciseIdList }
            .flatten()
            .toSet()
            .toList()

        val poseClassificationMap = poseClassifiedExerciseList.associateWith { exerciseId ->
            PoseClassificationModel.values().filter { it.exerciseIdList.contains(exerciseId) }
        }

        fun getPoseClassificationModels(exerciseID: Long): List<PoseClassificationModel>{
            return poseClassificationMap[exerciseID] ?: emptyList()
        }
    }

}