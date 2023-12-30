package io.github.deficuet.jimage

import java.awt.Graphics2D
import java.awt.Image
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

inline fun BufferedImage(
    width: Int, height: Int, type: Int,
    initializer: Graphics2D.(BufferedImage) -> Unit
): BufferedImage {
    return BufferedImage(width, height, type).apply {
        with(createGraphics()) {
            initializer(this@apply)
            dispose()
        }
    }
}

inline fun fancyBufferedImage(
    width: Int, height: Int, type: Int,
    initializer: Graphics2D.(BufferedImage) -> Unit
): BufferedImage {
    return BufferedImage(width, height, type).apply {
        with(createGraphics()) {
            setRenderingHints(mapOf(
                RenderingHints.KEY_ANTIALIASING to RenderingHints.VALUE_ANTIALIAS_ON,
                RenderingHints.KEY_RENDERING to RenderingHints.VALUE_RENDER_QUALITY,
                RenderingHints.KEY_COLOR_RENDERING to RenderingHints.VALUE_COLOR_RENDER_QUALITY,
                RenderingHints.KEY_INTERPOLATION to RenderingHints.VALUE_INTERPOLATION_BICUBIC,
                RenderingHints.KEY_ALPHA_INTERPOLATION to RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY,
                RenderingHints.KEY_TEXT_ANTIALIASING to RenderingHints.VALUE_TEXT_ANTIALIAS_ON,
                RenderingHints.KEY_STROKE_CONTROL to RenderingHints.VALUE_STROKE_NORMALIZE,
                RenderingHints.KEY_FRACTIONALMETRICS to RenderingHints.VALUE_FRACTIONALMETRICS_ON
            ))
            initializer(this@apply)
            dispose()
        }
    }
}

inline fun BufferedImage.edit(processor: Graphics2D.(BufferedImage) -> Unit) = apply {
    with(createGraphics()) {
        processor(this@edit)
        dispose()
    }
}

inline fun BufferedImage.fancyEdit(processor: Graphics2D.(BufferedImage) -> Unit) = apply {
    with(createGraphics()) {
        setRenderingHints(mapOf(
            RenderingHints.KEY_ANTIALIASING to RenderingHints.VALUE_ANTIALIAS_ON,
            RenderingHints.KEY_RENDERING to RenderingHints.VALUE_RENDER_QUALITY,
            RenderingHints.KEY_COLOR_RENDERING to RenderingHints.VALUE_COLOR_RENDER_QUALITY,
            RenderingHints.KEY_INTERPOLATION to RenderingHints.VALUE_INTERPOLATION_BICUBIC,
            RenderingHints.KEY_ALPHA_INTERPOLATION to RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY,
            RenderingHints.KEY_TEXT_ANTIALIASING to RenderingHints.VALUE_TEXT_ANTIALIAS_ON,
            RenderingHints.KEY_STROKE_CONTROL to RenderingHints.VALUE_STROKE_NORMALIZE,
            RenderingHints.KEY_FRACTIONALMETRICS to RenderingHints.VALUE_FRACTIONALMETRICS_ON
        ))
        processor(this@fancyEdit)
        dispose()
    }
}

inline fun <T> BufferedImage.use(block: BufferedImage.() -> T): T {
    val ret = this.block()
    flush()
    return ret
}

fun BufferedImage.flipX(): BufferedImage {
    return AffineTransformOpCollection.flipX(this).filter(this, null)
}

fun BufferedImage.flipY(): BufferedImage {
    return AffineTransformOpCollection.flipY(this).filter(this, null)
}

fun BufferedImage.quadrantRotate(times: Int = 1): BufferedImage {
    return when (times % 4) {
        0 -> this.copy()
        1 -> AffineTransformOpCollection.rotate90deg(this).filter(this, null)
        2 -> AffineTransformOpCollection.rotate180deg(this).filter(this, null)
        else -> AffineTransformOpCollection.rotate270deg(this).filter(this, null)
    }
}

fun BufferedImage.resize(w: Int, h: Int) = BufferedImage(w, h, type) {
    drawImage(getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null)
}

fun BufferedImage.fancyResize(w: Int, h: Int) = fancyBufferedImage(w, h, type) {
    drawImage(this@fancyResize, 0, 0, w, h, null)
}

fun BufferedImage.paste(other: Image, x: Int, y: Int) = edit {
    drawImage(other, x, y, null)
}

fun BufferedImage.copy() = BufferedImage(colorModel, copyData(null), isAlphaPremultiplied, null)

fun BufferedImage.toByteArray(format: String): ByteArray {
    return ByteArrayOutputStream().use {
        ImageIO.write(this, format, it)
        it.toByteArray()
    }
}

fun BufferedImage.save(path: String): BufferedImage {
    val imageFile = File(path)
    ImageIO.write(this, imageFile.extension, imageFile)
    return this
}
