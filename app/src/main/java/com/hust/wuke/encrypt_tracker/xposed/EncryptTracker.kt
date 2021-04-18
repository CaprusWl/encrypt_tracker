package com.hust.wuke.encrypt_tracker.xposed

import android.app.AndroidAppHelper
import com.hust.wuke.encrypt_tracker.EncryptFuncManager
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import de.robv.android.xposed.callbacks.XC_LoadPackage


/**
 * @author wuke.caprus@bytedance.com
 * @date 4/5/21
 */
class EncryptTracker : IXposedHookLoadPackage {
    companion object {
        const val TAG = "EncryptTracker"
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        val encryptFuncManager = EncryptFuncManager()

        XposedBridge.log(lpparam?.packageName ?: "default packageName")
        val instrumentation = XposedHelpers.findClass(
            "android.app.Activity", lpparam!!.classLoader
        )

        XposedBridge.hookAllMethods(instrumentation, "onCreate", object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: MethodHookParam) {
                val context = AndroidAppHelper.currentApplication().applicationContext
                XposedBridge.log("Current Activity : " + context.javaClass.name)

                XposedBridge.log("------ contentResolver: ${context.contentResolver} ------")

                val configJson = FileUtil.readFileAsString(context, "/sdcard/xposed_func_list.json")
                XposedBridge.log("------ configJson: $configJson ------")

                encryptFuncManager.getHookFuncList(configJson).apply {
                    if (find { it.packageName == lpparam.packageName } == null) {
                        return
                    }

                    forEach {
                        findAndHookMethod(
                            it.className,
                            lpparam?.classLoader,
                            it.funcName,
                            object : XC_MethodHook() {
                                @Throws(Throwable::class)
                                override fun beforeHookedMethod(param: MethodHookParam) {
                                    super.beforeHookedMethod(param)
                                }

                                @Throws(Throwable::class)
                                override fun afterHookedMethod(param: MethodHookParam) {
                                    // this will be called after the clock was updated by the original method
                                    param.result = it.funcName
                                }
                            })
                    }
                }
            }
        })
    }
}