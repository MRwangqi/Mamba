package com.kiwi.mamba.loader

import android.util.Log
import com.codelang.api.IMambaLoader
import java.util.*
import kotlin.collections.HashMap

class CostTimeLoader : IMambaLoader {
    private val timeCache = HashMap<Class<*>, HashMap<String, Long>>()

    override fun methodEnter(clazz: Class<*>, methodName: String, args: Array<out Any>?) {
        var map = timeCache[clazz]
        if (map == null) {
            map = HashMap()
            timeCache[clazz] = map
        }
        map[methodName] = System.currentTimeMillis()
    }

    override fun methodExit(clazz: Class<*>, methodName: String) {
        val map = timeCache[clazz]
        if (map != null && map[methodName] != null) {
            val costTime = System.currentTimeMillis() - map[methodName]!!
            Log.e("time", "class=" + clazz.name.toString() + " methodName=" + methodName + " costTime=" + costTime + "ms")
        }
    }
}