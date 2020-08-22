package android.net

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable

/**
 * Uri 扩展
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */

/**
 * Uri转Bitmap
 * @receiver Uri uri
 * @param context Context context
 * @return (android.graphics.Bitmap..android.graphics.Bitmap?) bitmap
 */
fun Uri.toBitmap(context: Context): Bitmap? =
    BitmapFactory.decodeStream(context.contentResolver.openInputStream(this))

/**
 * Uri转BitmapDrawable
 * @receiver Uri uri
 * @param context Context context
 * @return BitmapDrawable bitmapDrawable
 */
fun Uri.toBitmapDrawable(context: Context) =
    BitmapDrawable(context.resources, toBitmap(context))
