package milu.kiriu2010.excon2.b0x.b04

import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.ImageReader
import android.util.Size

object Camera2Util {
    @Throws(CameraAccessException::class)
    fun getCameraId(cameraManager: CameraManager, facing: Int): String? {
        for (cameraId in cameraManager.cameraIdList) {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            if (characteristics.get(CameraCharacteristics.LENS_FACING) == facing) {
                return cameraId
            }
        }
        return null
    }

    @Throws(CameraAccessException::class)
    fun getMaxSizeImageReader(map: StreamConfigurationMap, imageFormat: Int): ImageReader {
        val sizes = map.getOutputSizes(imageFormat)
        var maxSize = sizes[0]
        for (size in sizes) {
            if (size.width > maxSize.width) {
                maxSize = size
            }
        }
        return ImageReader.newInstance(
                //maxSize.getWidth(), maxSize.getHeight(), // for landscape.
                maxSize.height, maxSize.width, // for portrait.
                imageFormat, /*maxImages*/1)
    }

    @Throws(CameraAccessException::class)
    fun getBestPreviewSize(map: StreamConfigurationMap, imageSize: ImageReader): Size {
        //float imageAspect = (float) imageSize.getWidth() / imageSize.getHeight(); // for landscape.
        val imageAspect = imageSize.height.toFloat() / imageSize.width // for portrait
        var minDiff = 1000000000000f
        val previewSizes = map.getOutputSizes(SurfaceTexture::class.java)
        var previewSize = previewSizes[0]
        for (size in previewSizes) {
            val previewAspect = size.width.toFloat() / size.height
            val diff = Math.abs(imageAspect - previewAspect)
            if (diff < minDiff) {
                previewSize = size
                minDiff = diff
            }
            if (diff == 0.0f) break
        }
        return previewSize
    }
}