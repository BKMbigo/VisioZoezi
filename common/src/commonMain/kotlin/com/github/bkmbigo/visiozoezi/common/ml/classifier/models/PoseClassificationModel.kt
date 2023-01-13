package com.github.bkmbigo.visiozoezi.common.ml.classifier.models

enum class PoseClassificationModel (
    val exerciseIdList: List<Long>,     // Contains a list of id's of supported exercise
    val tfLiteModelName: String,    // Name of tfLite model (used by android application)
    val savedModelFolderName: String    // Path to the saved model created using KotlinDl
) {

    // The model classifies a person performing any type of push up exercise --> Returns true when the person
    PUSH_UP_POSE_CLASSIFICATION_MODEL(
        exerciseIdList = listOf(661, 662, 663, 1307, 1467, 3145),
        tfLiteModelName = "pose_classifier_push_up.tflite",
        savedModelFolderName = "push_up_keras_model"
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