package si.inova.zimskasola

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zimskasola.R

private const val ANIM_LENGTH : Long = 1000

class FragmentPlaces: Fragment() {

    lateinit var activityMain: ActivityMain
    lateinit var tvRooms: TextView
    lateinit var recycler: RecyclerView

    var rooms: ArrayList<RoomRecycler>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        var root = inflater.inflate(R.layout.fragment_places, container, false)
        if (activityMain.currentBuilding == null) {
            Log.d(TAG, "Fragment places: activityMain.currentBuilding was null, initializing...")
            activityMain.initialize()
        } else {
            tvRooms = root.findViewById(R.id.textViewProstori) as TextView
            recycler = root.findViewById(R.id.recyclerProstori) as RecyclerView

            tvRooms.apply { alpha = 0f }
            recycler.apply { alpha = 0f }

            recycler.layoutManager = LinearLayoutManager(this.context)

            rooms = ArrayList()
            var counter = 0
            for (floor in activityMain.currentBuilding?.floors!!) {
                for (room in floor.rooms) {
                    if (counter == 0)
                        rooms?.add(RoomRecycler(true, floor.name, room.name))
                    else
                        rooms?.add(RoomRecycler(false, floor.name, room.name))
                    counter++
                }
                counter = 0
            }

            recycler.adapter = RecyclerAdapterPlaces(rooms!!, this.context!!)
            recycler.itemAnimator = DefaultItemAnimator()

            tvRooms.apply { animate().alpha(1f).setDuration(ANIM_LENGTH).setListener(null) }
            recycler.apply { animate().alpha(1f).setDuration(ANIM_LENGTH).setListener(null) }
        }
        return root
    }
}

class RoomRecycler (
    var isFirst: Boolean,
    var floor: String,
    var room: String
)