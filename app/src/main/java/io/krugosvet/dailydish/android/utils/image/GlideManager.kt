package io.krugosvet.dailydish.android.utils.image

import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey

fun withNoCache() = RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true).signature(ObjectKey(System.currentTimeMillis()))
