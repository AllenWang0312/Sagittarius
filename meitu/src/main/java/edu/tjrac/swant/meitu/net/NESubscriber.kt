package edu.tjrac.swant.meitu.net


import android.annotation.TargetApi
import android.os.Build
import com.google.gson.JsonSyntaxException
import edu.tjrac.swant.baselib.common.base.BaseView
import edu.tjrac.swant.baselib.util.StringUtils
import edu.tjrac.swant.baselib.util.T
import retrofit2.adapter.rxjava.HttpException
import rx.Subscriber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by wpc on 2018/6/27.
 */
//none error subscriber
open abstract class NESubscriber<O : BR<*>>(internal var view: BaseView?) : Subscriber<O>() {


    override fun onStart() {
//        Log.e("onStart", "")
        super.onStart()
        //        view.showProgressGIF(R.drawable.run);
    }

    override fun onCompleted() {
//        Log.i("onCompleted", "")
//        view!!.dismissProgressDialog()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onError(e: Throwable?) {
        if (e is HttpException) {
            var message: String? = null
            when (e.code()) {
                400 -> {
                    message = "发出的请求有错误，服务器没有进行新建或修改数据的操作。"
                }
                401 -> {
                    message = "用户没有权限（令牌、用户名、密码错误）。"
                }
                403 -> {
                    message = "用户得到授权，但是访问是被禁止的。"
                }
                404 -> {
                    message = "发出的请求针对的是不存在的记录，服务器没有进行操作。"
                }
                406 -> {
                    message = "请求的格式不可得。"
                }
                410 -> {
                    message = "请求的资源被永久删除，且不会再得到的。"
                }
                422 -> {
                    message = "用户不存在或被冻结"
                }
                500 -> {
                    message = "服务器发生错误，请检查服务器。"
                }
                502 -> {
                    message = "网关错误。"
                }
                503 -> {
                    message = "服务不可用，服务器暂时过载或维护。"
                }
                504 -> {
                    message = "网关超时。"
                }
                else -> {
                    message = "网络超时"
                }
            }
            view?.showToast(message)
            if (e.code() == 401) {
//                App.token = ""
//                BaseApplication.instance?.exit()
//                if(view is BaseFragment){
//                    var context = view as BaseFragment
//                    context?.activity?.startActivity(Intent(context?.activity, MeituLoginActivity::class.java))
//                }else if(view is BaseActivity){
//                    var context = view as BaseActivity
//                    context?.startActivity(Intent(context, MeituLoginActivity::class.java))
//                }
                return
            }
            return
        } else if (e is UnknownHostException) {
            view?.showToast("服务器错误:找不到域名")
            return
        } else if (e is SocketTimeoutException) {
            view?.showToast("服务器错误:网络请求超时")
            return
        } else if (e is JsonSyntaxException) {
            view?.showToast("服务器错误:数据不符合规范")
            return
        } else if (e is IllegalArgumentException) {
            view?.showToast("服务器错误:找不到域名")
            return
        }
//        else if(e is ErrnoException){
//            view?.showToast("网络不可用", R.drawable.toast_faild)
//            return
//        }
        else if (e?.javaClass?.simpleName.equals("ErrnoException")) {
            view?.showToast("网络不可用")
            return
        }
//        Log.i("onError", e.toString())

        if (null === view || null == view) {
            return
        } else {
            if (null != e) {
                if (null != e?.message) {
//                    view.showToast(e.message?)
                    view?.showToast(e.message!!)
                    return
                }
//                else if(null!=e!!.cause&&null!=e!!.cause!!.message!!){
//                    view.showToast(e!!.cause!!.message!!)
//                }
//                else {
//
//                }
            }

        }
        view?.showToast("未知错误")
    }

    abstract fun onSuccess(t: O?)

    override fun onNext(t: O?) {
        if (t == null) {
            return onError(Throwable("接口错误"))
        } else {
            if (StringUtils.isEmpty(t.msg)) {
                if (!StringUtils.isEmpty(t.toast)) T.Companion.show(t.toast!!)
                onSuccess(t)
            }
        }

    }
}
