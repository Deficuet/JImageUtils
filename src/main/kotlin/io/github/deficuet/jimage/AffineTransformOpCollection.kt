package io.github.deficuet.jimage

import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage

class AffineTransformOpCollection private constructor() {
    companion object {
        fun flipX(img: BufferedImage) = AffineTransformOp(
            AffineTransform(-1f, 0f, 0f, 1f, img.width.toFloat(), 0f),
            AffineTransformOp.TYPE_NEAREST_NEIGHBOR
        )
        fun flipY(img: BufferedImage) = AffineTransformOp(
            AffineTransform(1f, 0f, 0f, -1f, 0f, img.height.toFloat()),
            AffineTransformOp.TYPE_NEAREST_NEIGHBOR
        )
        fun rotate90deg(img: BufferedImage) = AffineTransformOp(
            AffineTransform(0f, 1f, -1f, 0f, img.height.toFloat(), 0f),
            AffineTransformOp.TYPE_NEAREST_NEIGHBOR
        )
        fun rotate180deg(img: BufferedImage) = AffineTransformOp(
            AffineTransform(-1f, 0f, 0f, -1f, img.width.toFloat(), img.height.toFloat()),
            AffineTransformOp.TYPE_NEAREST_NEIGHBOR
        )
        fun rotate270deg(img: BufferedImage) = AffineTransformOp(
            AffineTransform(0f, -1f, 1f, 0f, 0f, img.width.toFloat()),
            AffineTransformOp.TYPE_NEAREST_NEIGHBOR
        )
    }
}