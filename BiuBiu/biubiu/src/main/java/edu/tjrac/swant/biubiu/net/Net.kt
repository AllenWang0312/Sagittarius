package edu.tjrac.swant.biubiu.net

import com.google.gson.Gson
import edu.tjrac.swant.baselib.common.base.CustomizeGsonConverterFactory
import edu.tjrac.swant.baselib.common.base.net.BaseNet
import edu.tjrac.swant.baselib.util.SUtil
import edu.tjrac.swant.biubiu.BiuBiuApp
import edu.tjrac.swant.biubiu.BuildConfig
import edu.tjrac.swant.biubiu.Config
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory

/**
 * Created by wpc on 2019-09-05.
 */
class Net : BaseNet() {
    override fun getInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val request: Request
            val modifiedUrl = originalRequest.url().newBuilder()
                    // Provide your custom parameter here
                    .addQueryParameter("platform", "android")
                    .addQueryParameter("version", BuildConfig.VERSION_NAME)
                    .build()

            var builder = originalRequest.newBuilder()
            builder.addHeader("Content-Type", "text/pain;charset=utf-8")
            if (!SUtil.isEmpty(BiuBiuApp.token)) {
                builder.addHeader("token", BiuBiuApp.token)
            }
            request = builder.url(modifiedUrl).build()
            chain.proceed(request)
        }
    }

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Net()
        }
        private var api: MeituApi? = null
//        private var sso_api: SSOApi? = null
//        private var old_api: OldApi? = null
    }

    override fun getRetrofit(url: String): Retrofit? {
        synchronized(mRetrofitLock) {
            if (null === retrofitMap.get(url)) {
//                val gson = GsonBuilder()
//                        .setLenient()
//                        .create()
                var retrofit = Retrofit.Builder().client(getOkHttpClient())
                        .baseUrl(url)
//                        .addConverterFactory(getResponseConverter())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                        .addConverterFactory(GsonConverterFactory.create(Gson()))
                        .addConverterFactory(CustomizeGsonConverterFactory.create(Gson()))
                        .build()
                retrofitMap.put(url, retrofit)
                return retrofit
            } else {
                return retrofitMap.get(url)
            }
        }
    }

    override fun getOkHttpClient(): OkHttpClient {
        return super.getOkHttpClient()

    }

    //add
    open fun getApiService(): MeituApi {
        if (null === api) {
            api = getRetrofit(Config.URL.API_SERVER)!!.create<MeituApi>(MeituApi::class.java!!)
        }
        return api!!
    }

//    open fun getSSOApiService(): SSOApi {
//        if (null === sso_api) {
//            sso_api = getRetrofit(Config_v4.URL.SSOAPI)!!.create<SSOApi>(SSOApi::class.java!!)
//        }
//        return sso_api!!
//    }

//    open fun getOldApiService(): OldApi {
//        if (null === old_api) {
//            old_api = getRetrofit(Config_v4.URL.OLDAPI)!!.create<OldApi>(OldApi::class.java!!)
//        }
//        return old_api!!
//    }
}