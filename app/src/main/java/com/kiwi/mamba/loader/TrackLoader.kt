package com.kiwi.mamba.loader

import com.codelang.api.IMambaLoader
import com.codelang.library.TrackActivity
import com.kiwi.mamba.MainActivity
import com.kiwi.mamba.loader.utils.loge

class TrackLoader : IMambaLoader {

    override fun methodEnter(clazz: Class<*>?, methodName: String?,args: Array<out Any>?) {
        when (clazz) {
            MainActivity::class.java -> {
                mainPage(methodName)
            }
            TrackActivity::class.java -> {
                trackPage(methodName)
            }
        }
    }

    /**
     * main 页面
     */
    private fun mainPage(methodName: String?) {
        when (methodName) {
            MainPage.GO_PAGE -> {
                "进入跟踪页面".loge()
            }
        }
    }

    /**
     * track 页面
     */
    private fun trackPage(methodName: String?) {
        when (methodName) {
            TrackPage.OPEN -> {
                "打开".loge()
            }
            TrackPage.CLOSE -> {
                "关闭".loge()
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
    const val OPEN = "open"
    const val CLOSE = "close"
}