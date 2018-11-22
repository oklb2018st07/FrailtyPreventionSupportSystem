package com.oklb2018.frailtypreventionsupportsystem.fragments

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.oklb2018.frailtypreventionsupportsystem.R

class WalkingFragment : Fragment(), SensorEventListener {

    lateinit var manager: SensorManager
    private var detectorSensor: Sensor? = null
    private var counterSensor: Sensor? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sub_activity_main_walking, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setSensors()
    }

    private fun setSensors() {
        manager = layoutInflater.context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        detectorSensor = manager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        counterSensor = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        manager.registerListener(this, detectorSensor, SensorManager.SENSOR_DELAY_NORMAL)
        manager.registerListener(this, counterSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val sensor = event!!.sensor
        val values = event.values
        val timestamp = event.timestamp
        if (sensor.type == Sensor.TYPE_STEP_COUNTER) {
            Log.d("a", "stp cnt = ${values[0]}")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}