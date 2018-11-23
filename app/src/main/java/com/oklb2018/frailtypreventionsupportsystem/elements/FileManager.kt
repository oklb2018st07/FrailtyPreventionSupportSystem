package com.oklb2018.frailtypreventionsupportsystem.elements

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

public class FileManager {

    private val defaultPath: String = Environment.getExternalStorageDirectory().path
    private val appPath: String = defaultPath + "/FPSS/data/"

    private val REQUEST_PERMISSION: Int = 1000

    public fun makeDirectory(dirPath: String = appPath, dirName: String = "") {
        val dir: File = File(dirPath + dirName)
        if (!dir.exists()) {
            if (dir.mkdirs()) Log.e("FPSS", "Can not make directory... ${dirPath}")
        }
    }

    public inner class CsvReader(filePath: String = appPath, fileName: String) {
        val list: List<List<String>>? = null
    }

    public inner class CsvWriter(filePath: String = appPath, fileName: String) {
        val file: File = File(filePath + fileName)

        public fun write(str: String) {
            val fileOutputStream: FileOutputStream = FileOutputStream(file, true)
            val outStreamWriter: OutputStreamWriter = OutputStreamWriter(fileOutputStream, "UTF-8")
            val bufferedWriter: BufferedWriter = BufferedWriter(outStreamWriter)
            bufferedWriter.write(str)
            bufferedWriter.flush()
        }
    }

    public fun checkExternalStoragePermission(activity: Activity) {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission(activity)
        }
    }

    private fun requestLocationPermission(activity: Activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSION
            )
        } else {
            Toast.makeText(activity, "No permission, this app will not work", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSION
            )
        }
    }
}