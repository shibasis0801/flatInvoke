package dev.shibasis.reaktor.media.image

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import dev.shibasis.reaktor.core.framework.Dispatch
import dev.shibasis.reaktor.io.network.httpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

@Composable
fun AsyncImage(
    url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillBounds
) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var size by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(url) {
        Dispatch.IO.launch {
            val fileName = url.split("/").last()

            val cachedBitmap = BitmapCache.retrieve(fileName)
            imageBitmap = if (cachedBitmap != null)
                cachedBitmap
            else {
                val cached = MultiLevelCache.retrieveWithFetch(fileName) {
                    val response = httpClient.get(url)
                    if (response.status == HttpStatusCode.OK)
                        response.body<ByteArray>()
                    else null
                }

                if (cached != null)
                    BitmapCache.store(fileName, cached)

                BitmapCache.retrieve(fileName)
            }
        }
    }

    imageBitmap?.let {
        Image(
            bitmap = it,
            "url",
            modifier = modifier.onSizeChanged { size = it },
            contentScale = contentScale
        )
    }
}