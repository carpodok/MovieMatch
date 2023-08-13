package com.example.moviematchapp.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class RoundedCornersTransform(
    private val radiusTopLeft: Float,
    private val radiusTopRight: Float,
    private val radiusBottomLeft: Float,
    private val radiusBottomRight: Float
) : BitmapTransformation() {

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val result = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val path = Path()
        path.moveTo(0f, radiusTopLeft)
        path.lineTo(0f, outHeight.toFloat() - radiusBottomLeft)
        path.quadTo(0f, outHeight.toFloat(), radiusBottomLeft, outHeight.toFloat())
        path.lineTo(outWidth.toFloat() - radiusBottomRight, outHeight.toFloat())
        path.quadTo(outWidth.toFloat(), outHeight.toFloat(), outWidth.toFloat(), outHeight.toFloat() - radiusBottomRight)
        path.lineTo(outWidth.toFloat(), radiusTopRight)
        path.quadTo(outWidth.toFloat(), 0f, outWidth.toFloat() - radiusTopRight, 0f)
        path.lineTo(radiusTopLeft, 0f)
        path.quadTo(0f, 0f, 0f, radiusTopLeft)
        path.close()

        canvas.clipPath(path)
        canvas.drawBitmap(toTransform, 0f, 0f, paint)

        return result
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID.toByteArray())
    }

    companion object {
        private const val ID = "RoundedCornersTransform"
    }
}