package dev.shibasis.reaktor.media

import androidx.compose.runtime.Composable
import dev.shibasis.reaktor.core.framework.Feature
import dev.shibasis.reaktor.media.Camera
import dev.shibasis.reaktor.navigation.screen.Props

@Composable
fun CameraScreen(props: Props) {
    Feature.Camera?.Render()
}