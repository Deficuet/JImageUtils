package io.github.deficuet.jimage

import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage

class ImageTransform(
    private val image: BufferedImage,
    private val transform: AffineTransform
) {
    fun flipX(): ImageTransform {
        transform.preConcatenate(AffineTransformCollection.flipX(image))
        return this
    }

    fun flipY(): ImageTransform {
        transform.preConcatenate(AffineTransformCollection.flipY(image))
        return this
    }

    fun quadrantRotate(times: Int = 1): ImageTransform {
        when (times % 4) {
            1 -> transform.preConcatenate(AffineTransformCollection.rotate90deg(image))
            2 -> transform.preConcatenate(AffineTransformCollection.rotate180deg(image))
            3 -> transform.preConcatenate(AffineTransformCollection.rotate270deg(image))
            else -> {  }
        }
        return this
    }

    fun apply(flushSrc: Boolean = false): BufferedImage {
        val result = AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR)
            .filter(image, null)
        if (flushSrc) image.flush()
        return result
    }
}

fun BufferedImage.flipX() = ImageTransform(this, AffineTransformCollection.flipX(this))

fun BufferedImage.flipY() = ImageTransform(this, AffineTransformCollection.flipY(this))

fun BufferedImage.quadrantRotate(times: Int = 1): ImageTransform {
    return when (times % 4) {
        0 -> ImageTransform(this, AffineTransform())
        1 -> ImageTransform(this, AffineTransformCollection.rotate90deg(this))
        2 -> ImageTransform(this, AffineTransformCollection.rotate180deg(this))
        else -> ImageTransform(this, AffineTransformCollection.rotate270deg(this))
    }
}
