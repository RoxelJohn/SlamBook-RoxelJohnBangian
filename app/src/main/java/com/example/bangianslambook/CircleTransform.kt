package com.example.bangianslambook // Use the appropriate package name

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class CircleTransform : BitmapTransformation() {

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update("circle".toByteArray())
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        // Determine the size of the square to crop out of the original image.
        val size = Math.min(toTransform.width, toTransform.height)

        // Center the crop region in the middle of the image.
        val x = (toTransform.width - size) / 2
        val y = (toTransform.height - size) / 2

        // Crop the image into a square.
        val squaredBitmap = Bitmap.createBitmap(toTransform, x, y, size, size)

        // Ensure the config is non-null; use ARGB_8888 as a default if null.
        val config = toTransform.config ?: Bitmap.Config.ARGB_8888

        // Create a new bitmap to hold the circular transformation.
        val bitmap = Bitmap.createBitmap(size, size, config)

        // Set up the canvas to draw the transformed image.
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        val rect = Rect(0, 0, size, size)
        val rectF = RectF(rect)

        // Draw a circle over the squared bitmap.
        canvas.drawOval(rectF, paint)

        // Set the mode to SRC_IN to ensure the image is drawn in the circular area.
        paint.xfermode = android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(squaredBitmap, rect, rect, paint)

        return bitmap
    }
}
