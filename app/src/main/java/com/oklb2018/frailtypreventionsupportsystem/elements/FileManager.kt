package com.oklb2018.frailtypreventionsupportsystem.elements

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import com.oklb2018.frailtypreventionsupportsystem.fragments.BrainTrainingParameter
import com.oklb2018.frailtypreventionsupportsystem.fragments.WalkingFragment
import com.oklb2018.frailtypreventionsupportsystem.fragments.WalkingParameter
import java.io.*
import java.lang.StringBuilder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

public class FileManager {

    companion object {
        private val defaultPath: String = Environment.getExternalStorageDirectory().path
        private val appPath: String = defaultPath + "/FPSS/data/"

        private val REQUEST_PERMISSION: Int = 1000

        public val APP_DATE_FORMAT: String = "yyyy-MM-dd"
        public val APP_DATE_AND_TIME_FORMAT: String = "yyyy-MM-dd-HH-mm-ss"

        public val walkingParameterFileName: String = "walkingStates01.csv"
        public val checkListResultFileName: String = "checkListResult01.csv"
        public val mealsResultFileName: String = "mealsResult01.csv"
        public val brainTrainingResultFileName: String = "brainTrainingResult01.csv"

        public val walkingParameterFileHeader: String = "startCalendar,goal,result"
        public val checkListResultFileHeader: String = "startCalendar,Q1,Q2,Q3,Q4,Q5,Q6,Q7,Q8,Q9,Q10,Q11,Q12,Q13,Q14,Q15,score"
        public val mealsResultFileHeader: String = "startCalendar,Q1,Q2,Q3,Q4,Q5,Q6,Q7,Q8,Q9,Q10"
        public val brainTrainingResultFileHeader: String = "startCalendar,finishCalender,skin,difficulty,retry,total,blank,correct,location,questions,answers"
    }

    public fun makeDirectory(dirPath: String = appPath, dirName: String = "") {
        val dir: File = File(dirPath + dirName)
        if (!dir.exists()) {
            if (dir.mkdirs()) Log.e("FPSS", "Can not make directory... ${dirPath}")
        }
    }

    public fun makeFiles(filePath: String = appPath) {
        val walkingParameterFile: File = File(filePath + walkingParameterFileName)
        val checkListResultFile: File = File(filePath + checkListResultFileName)
        val mealsResultFile: File = File(filePath + mealsResultFileName)
        val brainTrainingResultFile: File = File(filePath + brainTrainingResultFileName)
        if (!walkingParameterFile.exists()) {
            CsvWriter(fileName = walkingParameterFileName).write(walkingParameterFileHeader)
        }
        if (!checkListResultFile.exists()) {
            CsvWriter(fileName = checkListResultFileName).write(checkListResultFileHeader)
        }
        if (!mealsResultFile.exists()) {
            initMealsData()
        }
        if (!brainTrainingResultFile.exists()) {
            initBrainTrainingParameter()
            //CsvWriter(fileName = brainTrainingResultFileName).write(brainTrainingResultFileHeader)
        }
    }

    public fun initWalkingParameter() {
        val r = Random()
        for (i in 0 until WalkingFragment.walkingParameters.size - 1) {
            val p = WalkingFragment.walkingParameters[i]
            if (p.steps == 0 && p.goal == 0) {
                val s = r.nextInt(2000 + 15000)
                p.goal = s
                p.steps = s + r.nextInt(10000)
            }
        }
        CsvWriter(fileName = FileManager.walkingParameterFileName)
            .overwriteWalkingParameter(WalkingFragment.walkingParameters)
    }

    public fun initBrainTrainingParameter() {
        CsvWriter(fileName = brainTrainingResultFileName).overwrite(brainTrainingResultFileHeader)
        CsvWriter(fileName = brainTrainingResultFileName).write("2018-12-23-16-50-25,2018-12-23-16-51-45,2,4,0,8,4,4,30222222EEEE,011001100000,30222222EEEE")
        CsvWriter(fileName = brainTrainingResultFileName).write("2018-12-26-16-52-25,2018-12-26-16-53-12,2,4,0,8,4,4,30222222EEEE,011001100000,30222222EEEE")
        CsvWriter(fileName = brainTrainingResultFileName).write("2018-12-26-16-54-25,2018-12-26-16-54-32,2,4,0,8,4,4,30222222EEEE,011001100000,30222222EEEE")
    }

    public fun initFiles() {
        CsvWriter(fileName = checkListResultFileName).overwrite(checkListResultFileHeader)
    }

    public fun initMealsData() {
        CsvWriter(fileName = mealsResultFileName).overwrite(mealsResultFileHeader)
        CsvWriter(fileName = mealsResultFileName).write("2019-01-01-21-19-40,1,2,1,2,3,2,2,2,0,2")
        CsvWriter(fileName = mealsResultFileName).write("2019-01-03-21-19-40,2,0,0,2,2,1,2,0,2,2")
        CsvWriter(fileName = mealsResultFileName).write("2019-01-07-21-19-40,2,2,2,2,3,2,2,2,1,1")
        CsvWriter(fileName = mealsResultFileName).write("2019-01-10-21-19-40,2,1,3,2,2,3,2,1,2,1")
        CsvWriter(fileName = mealsResultFileName).write("2019-01-11-21-19-40,3,2,3,2,2,3,2,2,2,2")
    }

    public inner class CsvReader(filePath: String = appPath, fileName: String = "") {
        val file: File = File(filePath + fileName)

        public fun read(): String {
            val stringBuilder = StringBuilder()
            val fileInputStream = FileInputStream(file)
            val inputStreamReader = InputStreamReader(fileInputStream, "UTF-8")
            val bufferedReader = BufferedReader(inputStreamReader)
            bufferedReader.use {
                var line = it.readLine()
                while (line != null) {
                    stringBuilder.append(line).append("\n")
                    line = it.readLine()
                }
            }
            return stringBuilder.toString()
        }

        public fun readLines(): ArrayList<String> {
            val strings = ArrayList<String>()
            val fileInputStream = FileInputStream(file)
            val inputStreamReader = InputStreamReader(fileInputStream, "UTF-8")
            val bufferedReader = BufferedReader(inputStreamReader)
            bufferedReader.use {
                var line = it.readLine()
                while (line != null) {
                    strings.add(line)
                    line = it.readLine()
                }
            }
            return strings
        }

        public fun readWalkingParameter(): ArrayList<WalkingParameter> {
            val parameters: ArrayList<WalkingParameter> = arrayListOf()
            val fileInputStream: FileInputStream = FileInputStream(file)
            val inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream, "UTF-8")
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
            bufferedReader.use {
                var line = it.readLine()
                line = it.readLine()
                while (line != null) {
                    val row = line.split(",")
                    if (row.size != 3) {
                        line = it.readLine()
                        continue
                    }
                    parameters.add(WalkingParameter(row[0].toDate()!!,row[0], Integer.parseInt(row[1]), Integer.parseInt(row[2])))
                    line = it.readLine()
                }
            }
            return parameters
        }
    }

    public inner class CsvWriter(filePath: String = appPath, fileName: String) {
        val file: File = File(filePath + fileName)

        public fun write(str: String) {
            val newLine = if (file.exists()) "\n" else ""
            val fileOutputStream: FileOutputStream = FileOutputStream(file, true)
            val outputStreamWriter: OutputStreamWriter = OutputStreamWriter(fileOutputStream, "UTF-8")
            val bufferedWriter: BufferedWriter = BufferedWriter(outputStreamWriter)
            bufferedWriter.write(newLine + str)
            bufferedWriter.flush()
        }

        public fun overwrite(str: String) {
            val fileOutputStream: FileOutputStream = FileOutputStream(file, false)
            val outputStreamWriter: OutputStreamWriter = OutputStreamWriter(fileOutputStream, "UTF-8")
            val bufferedWriter: BufferedWriter = BufferedWriter(outputStreamWriter)
            bufferedWriter.write(str)
            bufferedWriter.flush()
        }

        public fun writeWalkingParameter(walkingParameters: ArrayList<WalkingParameter>) {
            //val newLine = if (file.exists()) "\n" else ""
            val fileOutputStream: FileOutputStream = FileOutputStream(file, true)
            val outputStreamWriter: OutputStreamWriter = OutputStreamWriter(fileOutputStream, "UTF-8")
            val bufferedWriter: BufferedWriter = BufferedWriter(outputStreamWriter)
            //bufferedWriter.write(newLine)
            for (p in walkingParameters) {
                bufferedWriter.write(p.dateStr + "," + p.goal + "," + p.steps + "\n")
            }
            bufferedWriter.flush()
        }

        public fun overwriteWalkingParameter(walkingParameters: ArrayList<WalkingParameter>) {
            val fileOutputStream: FileOutputStream = FileOutputStream(file, false)
            val outputStreamWriter: OutputStreamWriter = OutputStreamWriter(fileOutputStream, "UTF-8")
            val bufferedWriter: BufferedWriter = BufferedWriter(outputStreamWriter)
            bufferedWriter.write(walkingParameterFileHeader + "\n")
            for (p in walkingParameters) {
                bufferedWriter.write(p.dateStr + "," + p.goal + "," + p.steps + "\n")
            }
            bufferedWriter.flush()
        }

        public fun overwriteBrainTrainingParameter(brainTrainingParameters: ArrayList<BrainTrainingParameter>) {
            val fileOutputStream: FileOutputStream = FileOutputStream(file, false)
            val outputStreamWriter: OutputStreamWriter = OutputStreamWriter(fileOutputStream, "UTF-8")
            val bufferedWriter: BufferedWriter = BufferedWriter(outputStreamWriter)
            bufferedWriter.write(brainTrainingResultFileHeader + "\n")
            for (p in brainTrainingParameters) {
                bufferedWriter.write(p.toString() + "\n")
            }
            bufferedWriter.flush()
        }

        public fun writeMealsData(calendar: Calendar, answers: ArrayList<Int>) {
            val fileOutputStream: FileOutputStream = FileOutputStream(file, true)
            val outputStreamWriter: OutputStreamWriter = OutputStreamWriter(fileOutputStream, "UTF-8")
            val bufferedWriter: BufferedWriter = BufferedWriter(outputStreamWriter)
            bufferedWriter.write("\n" + DateFormat.format(FileManager.APP_DATE_AND_TIME_FORMAT, calendar).toString())
            for (p in answers) {
                bufferedWriter.write("," + p.toString())
            }
            bufferedWriter.flush()
            Log.d("debug", "Write extra data")
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

public fun String.toDate(pattern: String = FileManager.APP_DATE_FORMAT): Calendar? {
    val sdFormat = try {
        SimpleDateFormat(pattern)
    } catch (e: IllegalArgumentException) {
        null
    }
    val date = sdFormat?.let {
        try {
            it.parse(this)
        } catch (e: ParseException) {
            null
        }
    }
    return Calendar.getInstance().apply { time = date }
}

public fun String.toDateAndTime(pattern: String = FileManager.APP_DATE_AND_TIME_FORMAT): Calendar? {
    val sdFormat = try {
        SimpleDateFormat(pattern)
    } catch (e: IllegalArgumentException) {
        null
    }
    val date = sdFormat?.let {
        try {
            it.parse(this)
        } catch (e: ParseException) {
            null
        }
    }
    return Calendar.getInstance().apply { time = date }
}