package io.krugosvet.dailydish.android.utils.image

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.mlsdev.rximagepicker.RxImageConverters
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import java.io.File

fun withNoCache() = RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true).signature(ObjectKey(System.currentTimeMillis()))

fun uriToBitmapMapper(context: Context) =
        Function<Uri, ObservableSource<Bitmap>> { uri ->
            val bitmap = RxImageConverters.uriToBitmap(context, uri)
            File(uri.path).delete()
            return@Function bitmap
        }
