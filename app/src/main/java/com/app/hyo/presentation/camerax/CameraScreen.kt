package com.app.hyo.presentation.camerax

import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.camera.view.PreviewView
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.app.hyo.util.WebSocketManager
import androidx.compose.runtime.getValue
import kotlinx.serialization.json.Json


@Composable
fun CameraScreen(modifier: Modifier = Modifier, viewModel: PredictViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = context as LifecycleOwner
    val predict by viewModel.uiState.collectAsState(initial = "Initial Value")

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }

            WebSocketManager.init(viewModel)
            CameraStarter.start(ctx, previewView, lifecycleOwner)

            previewView
        }
    )

    Text(
        text = predict,
        modifier = Modifier,
        style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
    )
}