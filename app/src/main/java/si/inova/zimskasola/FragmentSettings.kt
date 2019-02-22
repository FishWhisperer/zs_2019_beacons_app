package si.inova.zimskasola

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.zimskasola.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData
import java.util.*
import kotlin.collections.ArrayList

private const val ANIM_LENGTH : Long = 1000

class FragmentSettings : Fragment() {

    lateinit var activityMain : ActivityMain

    lateinit var chart: LineChart
    lateinit var cl: ConstraintLayout
    lateinit var tvCurrentFloor: TextView
    lateinit var tvCurrentRoom: TextView

    val rnd: Random = Random()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        cl = root.findViewById(R.id.clSettings) as ConstraintLayout
        chart = root.findViewById(R.id.chart) as LineChart
        tvCurrentFloor = root.findViewById(R.id.tvSettingsNadstropje) as TextView
        tvCurrentRoom = root.findViewById(R.id.tvSettingsImeSobe) as TextView

        tvCurrentFloor.text = activityMain.currentFloor?.name
        tvCurrentRoom.text = activityMain.currentRoom?.name

        // GRAPH DATA
        // fill dataObjects with data
        // test start random hardcoded data
        var dataObjects = GraphData()
        var hoursInDay = mutableMapOf<Int, Int>()
        for (i in 0..23) {
            hoursInDay[i] = rnd.nextInt(10)
        }
        for (j in 0..5) {
            dataObjects.data.add(Visit("room $j", hoursInDay))
        }
        val entries = ArrayList<Entry>()
        for (i in 0..(dataObjects.data[0].count.size-1)) {
            Log.d(TAG, "$i")
            entries.add(Entry(i.toFloat(), dataObjects.data[0].count[i]!!.toFloat()))
        }
        // test end

        /* ACTUAL DATa
        val entries = ArrayList<Entry>()
        for (data in activityMain.roomVisitingFrequency.data) {
            if (data.roomName == activityMain.currentRoom?.name) {
                for (i in 0..data.count.size) {
                    if (data.count[i]!! > 0)
                        entries.add(Entry(i.toFloat(), data.count[i]!!.toFloat()))
                        Log.d(TAG, "entry added")
                }
            }
        }
        Log.d(TAG, $entries)
        */

        // GRAPH LOOKS
        // data settings
        val dataSet = LineDataSet(entries, resources.getString(R.string.graph_label))
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.label = resources.getString(R.string.graph_dataset_label)
        dataSet.setDrawFilled(true)
        dataSet.setDrawCircles(false)
        dataSet.lineWidth = 0.7f
        dataSet.setDrawValues(false)

        val lineData = LineData(dataSet)
        chart.data = lineData

        // graph settings
        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawGridLines(false)
        xAxis.axisMinimum = 0f
        xAxis.axisMaximum = 24f

        val left = chart.axisLeft
        left.setDrawLabels(false)
        left.setDrawGridLines(false)
        left.axisMinimum = 0f
        left.setDrawLabels(false)
        left.setDrawAxisLine(false)

        val right = chart.axisRight
        right.setDrawLabels(false)
        right.setDrawGridLines(false)
        right.setDrawAxisLine(false)

        chart.setDrawGridBackground(false)
        chart.setDrawBorders(false)
        chart.description = Description().apply { text = "" }
        chart.setNoDataText(resources.getString(R.string.graph_data_unavailable))

        // interaction settings
        chart.setTouchEnabled(false)

        // refresh
        chart.invalidate()

        cl.alpha = 0f
        cl.apply { animate().alpha(1f).setDuration(ANIM_LENGTH).setListener(null) }

        return root
    }

    override fun onResume() {
        super.onResume()
    }
}

class GraphData (
    var data: ArrayList<Visit> = ArrayList()
)

class Visit (
    var roomName: String,
    var count: MutableMap<Int, Int>
)