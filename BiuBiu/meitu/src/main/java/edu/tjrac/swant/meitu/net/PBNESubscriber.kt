package edu.tjrac.swant.meitu.net

import edu.tjrac.swant.baselib.common.base.BaseView

//progress bar null error subscriber
abstract class  PBNESubscriber<O : BR<*>>(internal var v: BaseView?) : NESubscriber<O>(v){
    override fun onStart() {
        super.onStart()
        if(null!=v){
            v?.showProgressDialog()
        }
    }

    override fun onError(e: Throwable?) {
        super.onError(e)
        if(null!=v){
            v?.dismissProgressDialog()
        }
    }
    override fun onCompleted() {
        super.onCompleted()
        if(null!=v){
            v?.dismissProgressDialog()
        }
    }
}