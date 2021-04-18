package com.hust.wuke.encrypt_tracker

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hust.wuke.encrypt_tracker.xposed.FileUtil
import java.io.File

/**
 * @author wuke.caprus@bytedance.com
 * @date 4/5/21
 */
class EncryptFuncManager {

    fun getHookFuncList(configJson: String): List<HookFuncInfo> {
//        val funcListFile = File("/sdcard/xposed_func_list.json")
//        val funcListJsonStr = FileUtil.readFileAsString(funcListFile)
        val typeToken = object : TypeToken<List<HookFuncInfo>>() {}.type

        return Gson().fromJson(configJson, typeToken)
    }
}

data class HookFuncInfo(
    val funcName: String,
    val packageName: String,
    val className: String,
    val funcArgsType: List<Any>
)