package com.hust.wuke.encrypt_tracker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hust.wuke.encrypt_tracker.xposed.FileUtil
import java.io.File
import java.net.URI

const val PERMISSION_REQUEST_CODE = 1000

class MainActivity : AppCompatActivity() {

    private var configJson = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.hello_world).setOnClickListener {
            Toast.makeText(this, "${getFirstName()} ${getLastName()}", Toast.LENGTH_SHORT).show()
        }

        checkPermission(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (PERMISSION_REQUEST_CODE == requestCode) {
            grantResults.filter {
                it != PackageManager.PERMISSION_GRANTED
            }.apply {
                if (isEmpty()) {
                    runOnUiThread { onPermissionGranted() }
                } else {
                    runOnUiThread { onPermissionDenied() }
                }
            }
        }
    }

    private fun onPermissionDenied() {
        Toast.makeText(this, "请开启存储权限", Toast.LENGTH_SHORT).show()
    }

    private fun checkPermission(permissions: Array<String>) {
        permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.apply {
            if (isNotEmpty()) {
                ActivityCompat.requestPermissions(
                        this@MainActivity,
                        this.toTypedArray(),
                        PERMISSION_REQUEST_CODE
                )
            } else {
                runOnUiThread { onPermissionGranted() }
            }
        }
    }

    private fun onPermissionGranted() {
        loadConfigJson()
    }

    private fun loadConfigJson() {
        val funcListFile = File("/sdcard/xposed_func_list.json")
        configJson = FileUtil.readFileAsString(this, funcListFile)
    }

    private fun getFirstName(): String {
        return "Wu"
    }

    private fun getLastName(): String {
        return "Ke"
    }
}