import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizerResult
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker

@Composable
fun GestureOverlay(
    result: GestureRecognizerResult?,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        result?.let {
            for (landmarkList in it.landmarks()) {
                // Draw connections
                HandLandmarker.HAND_CONNECTIONS.forEach { connection ->
                    val start = landmarkList[connection.start()]
                    val end = landmarkList[connection.end()]
                    val path = Path().apply {
                        moveTo(start.x() * size.width, start.y() * size.height)
                        lineTo(end.x() * size.width, end.y() * size.height)
                    }
                    drawPath(
                        path = path,
                        color = Color.Red,
                        style = Stroke(width = 5f)
                    )
                }

                // Draw points
                landmarkList.forEach { landmark ->
                    drawCircle(
                        color = Color.Blue,
                        radius = 10f,
                        center = androidx.compose.ui.geometry.Offset(
                            landmark.x() * size.width,
                            landmark.y() * size.height
                        )
                    )
                }
            }
        }
    }
}