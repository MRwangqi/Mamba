package com.kiwi.mamba.loader

import com.codelang.api.IMambaLoader
import com.codelang.library.TrackActivity
import com.kiwi.mamba.MainActivity
import com.kiwi.mamba.loader.utils.loge

class TrackLoader : IMambaLoader {

    override fun methodEnter(clazz: Class<*>?, methodName: String?, args: Array<out Any>?) {
        when (clazz) {
            MainActivity::class.java -> {
                mainPage(methodName, args)
            }
            TrackActivity::class.java -> {
                trackPage(methodName, args)
            }
        }
    }

    /**
     * main 页面
     */
    private fun mainPage(methodName: String?, args: Array<out Any>?) {
        when (methodName) {
            MainPage.GO_PAGE -> {
                "触发 $methodName 方法".loge()
                args?.forEach {
                    "捕捉到参数值:$it ".loge()
                }
            }
        }
    }

    /**
     * track 页面
     */
    private fun trackPage(methodName: String?, args: Array<out Any>?) {
        when (methodName) {
            TrackPage.TRACK_OPEN, TrackPage.TRACK_CLOSE -> {
                "触发 $methodName 方法".loge()
                args?.forEach {
                    "捕捉到参数值:$it ".loge()
                }
            }
        }
    }


    override fun methodExit(clazz: Class<*>?, methodName: String?) {

    }
}

object MainPage {
    const val GO_PAGE = "goPage"
}

object TrackPage {
    const val TRACK_OPEN = "open"
    const val TRACK_CLOSE = "close"
}