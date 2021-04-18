package com.hust.wuke.encrypt_tracker.xposed

import android.content.Context
import android.net.Uri
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.InputStreamReader

/**
 * @author wuke.caprus@bytedance.com
 * @date 4/10/21
 */
object FileUtil {
    fun readFileAsString(context: Context, path: String): String {
        return readFileAsString(context, File(path))
    }

    fun readFileAsString(context: Context, file: File): String {
        val stringBuilder = StringBuilder()
        val inputStream = context.contentResolver.openInputStream(Uri.fromFile(file))
        var line: String?
        BufferedReader(InputStreamReader(inputStream)).apply {
            line = readLine()
            while (line.isNullOrEmpty().not()) {
                stringBuilder.append(line)
                line = readLine()
            }
        }

        return stringBuilder.toString()
    }
}