package io.krugosvet.dailydish.android.utils.image

import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey

fun withNoCache() = RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true).signature(ObjectKey(System.currentTimeMillis()))

//fun uriImageCompress(context: Context) =
//        Function<Uri, ObservableSource<Uri>> { uri ->
//            return@Function RxImageConverters.uriToBitmap(context, uri).subscribe {
//                val stream = ByteArrayOutputStream()
//                it.compress(Bitmap.CompressFormat.JPEG, 50, stream)
//                stream.writeTo(FileOutputStream(File(uri.path)))
//            }
//        }
