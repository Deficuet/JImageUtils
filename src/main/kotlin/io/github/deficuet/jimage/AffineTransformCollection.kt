package io.github.deficuet.jimage

import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage

class AffineTransformCollection private constructor() {
    companion object {
        fun flipX(img: BufferedImage): AffineTransform {
            return AffineTransform(-1f, 0f, 0f, 1f, img.width.toFloat(), 0f)
        }

        fun flipY(img: BufferedImage): AffineTransform {
            return AffineTransform(1f, 0f, 0f, -1f, 0f, img.height.toFloat())
        }

        /**
         * Clockwise
         */
        fun rotate90deg(img: BufferedImage): AffineTransform {
            return AffineTransform(0f, 1f, -1f, 0f, img.height.toFloat(), 0f)
        }

        /**
         * Clockwise
         */
        fun rotate180deg(img: BufferedImage): AffineTransform {
            return AffineTransform(-1f, 0f, 0f, -1f, img.width.toFloat(), img.height.toFloat())
        }

        /**
         * Clockwise
         */
        fun rotate270deg(img: BufferedImage): AffineTransform {
            return AffineTransform(0f, -1f, 1f, 0f, 0f, img.width.toFloat())
        }
    }
}