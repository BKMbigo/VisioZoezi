package com.github.bkmbigo.visiozoezi.common.ml.pose.models

enum class PoseEdges(val startLandmark: PoseLandmark, val endLandmark: PoseLandmark) {
    NOSE_TO_LEFT_EYE(PoseLandmark.NOSE, PoseLandmark.LEFT_EYE),
    NOSE_TO_RIGHT_EYE(PoseLandmark.NOSE, PoseLandmark.RIGHT_EYE),
    LEFT_EYE_TO_LEFT_EAR(PoseLandmark.LEFT_EYE, PoseLandmark.LEFT_EAR),
    RIGHT_EYE_TO_RIGHT_EAR(PoseLandmark.RIGHT_EYE, PoseLandmark.RIGHT_EAR),
    LEFT_SHOULDER_TO_RIGHT_SHOULDER(PoseLandmark.LEFT_SHOULDER, PoseLandmark.RIGHT_SHOULDER),
    LEFT_SHOULDER_LEFT_ELBOW(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_ELBOW),
    LEFT_SHOULDER_TO_LEFT_HIP(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_HIP),
    LEFT_SHOULDER_TO_RIGHT_HIP(PoseLandmark.LEFT_SHOULDER, PoseLandmark.RIGHT_HIP),
    RIGHT_SHOULDER_TO_RIGHT_ELBOW(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW),
    RIGHT_SHOULDER_TO_RIGHT_HIP(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_HIP),
    RIGHT_SHOULDER_TO_LEFT_HIP(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.LEFT_HIP),
    LEFT_ELBOW_TO_LEFT_WRIST(PoseLandmark.LEFT_ELBOW, PoseLandmark.LEFT_WRIST),
    RIGHT_ELBOW_TO_RIGHT_WRIST(PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_WRIST),
    LEFT_HIP_TO_RIGHT_HIP(PoseLandmark.LEFT_HIP, PoseLandmark.RIGHT_HIP),
    LEFT_HIP_TO_LEFT_KNEE(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_KNEE),
    RIGHT_HIP_TO_RIGHT_KNEE(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_KNEE),
    LEFT_KNEE_TO_LEFT_ANKLE(PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_ANKLE),
    RIGHT_KNEE_TO_RIGHT_ANKLE(PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_ANKLE);

    companion object {
        fun getPoseLines(points: List<PosePoint>): List<PoseLine>{
            val edges = PoseEdges.values()

            return edges.map {  edge ->
                PoseLine(
                    points[edge.startLandmark.position],
                    points[edge.endLandmark.position]
                )
            }
        }
    }

}