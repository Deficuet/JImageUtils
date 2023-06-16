package io.github.deficuet.jimage

import ar.com.hjg.pngj.FilterType
import java.awt.Graphics2D
import java.awt.Image
import java.awt.RenderingHints
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.imageio.ImageIO
import it.geosolutions.imageio.plugins.png.PNGWriter

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

fun BufferedImage.flipY(): BufferedImage {
    return AffineTransformOp(
        AffineTransform.getScaleInstance(1.0, -1.0).apply {
            translate(0.0, -height.toDouble())
        },
        AffineTransformOp.TYPE_NEAREST_NEIGHBOR
    ).filter(this, null)
}

fun BufferedImage.resize(width: Int, hidth: Int) = BufferedImage(width, hidth, type) {
    drawImage(getScaledInstance(width, hidth, Image.SCALE_SMOOTH), 0, 0, null)
}

fun BufferedImage.fancyResize(width: Int, hidth: Int) = fancyBufferedImage(width, height, type) {
    drawImage(this@fancyResize, 0, 0, width, height, null)
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

fun BufferedImage.savePng(path: String, compressionLevel: Int = 7): BufferedImage {
    assert(type == BufferedImage.TYPE_4BYTE_ABGR)
    FileOutputStream(File(path)).use { output ->
        PNGWriter().writePNG(
            this, output, (9f - compressionLevel) / 9f,
            FilterType.FILTER_DEFAULT
        )
    }
    return this
}
