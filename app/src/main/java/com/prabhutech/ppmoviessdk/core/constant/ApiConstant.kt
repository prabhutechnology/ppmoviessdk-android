package com.prabhutech.ppmoviessdk.core.constant

import com.prabhutech.ppmoviessdk.BuildConfig

object ApiConstant {
    var BASE_URL = if (BuildConfig.DEBUG) "https://stagesys.prabhupay.com/api/"
    else "https://sys.prabhupay.com/api/"
}