package com.deletech.maps
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.deletech.maps.custom.Resource
import com.deletech.maps.custom.Status
import com.deletech.maps.models.Geometry
import com.deletech.maps.models.GeometryPolygon
import com.deletech.maps.models.Point
import kotlinx.android.synthetic.main.activity_main.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.Polyline
import kotlin.math.round
import kotlin.math.roundToLong


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var pointA: List<Double>
    private lateinit var pointB: List<Double>
    private lateinit var lineCoordinates: List<List<List<Float>>>
    private lateinit var polygonCoordinates: List<List<List<Float>>>
   private lateinit var cordinatesB:List<List<Float>>
    private lateinit var coordinatesPolygon:List<List<Float>>
    var coordinates: ArrayList<Float> = ArrayList()

    var latitudeA: String? =null
    var longitudeA:String?=null
    var latitudeB: String? =null
    var longitudeB:String?=null
    val lineLatitude = ArrayList<String>()
    val lineLongitude = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Configuration.getInstance().userAgentValue=BuildConfig.APPLICATION_ID
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        init()
        setUpUi()
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        map.tileProvider.clearTileCache()
        Configuration.getInstance().cacheMapTileCount = 12.toShort()
        Configuration.getInstance().cacheMapTileOvershoot = 12.toShort()
        // Create a custom tile source
        // Create a custom tile source
        map.setTileSource(object : OnlineTileSourceBase(
            "",
            1,
            20,
            512,
            ".png",
            arrayOf("https://a.tile.openstreetmap.org/")
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                return (baseUrl
                        + MapTileIndex.getZoom(pMapTileIndex)
                        + "/" + MapTileIndex.getX(pMapTileIndex)
                        + "/" + MapTileIndex.getY(pMapTileIndex)
                        + mImageFilenameEnding)
            }
        })

    }
  private fun  init() {
       viewModel.getGeoPoints()
        viewModel.getGeoLine()
       viewModel.getGeoPolygon()
    }
    private fun setStatus(data: Resource<Point>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status
        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
            window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
        if (status == Status.ERROR) {
            if (data.message != null) {
                empty_text.text = data.message

            }
            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener({ init() })
        }
    }
    private fun setStatusB(data: Resource<Geometry>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status
        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
            window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        if (status == Status.ERROR) {
            if (data.message != null) {
                empty_text.text = data.message
            }
            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener { init() }
        }
    }
    private fun setStatusC(data: Resource<GeometryPolygon>) {
        empty_layout.visibility = View.GONE
        main.visibility = View.VISIBLE
        val status: Status = data.status
        if (status == Status.LOADING) {
            avi.visibility = View.VISIBLE
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else if (status == Status.ERROR || status == Status.SUCCESS) {
            avi.visibility = View.GONE
            window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        if (status == Status.ERROR) {
            if (data.message != null) {
                empty_text.text = data.message

            }
            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener({ init() })
        }
    }
    fun setUpUi() {
        viewModel.observeGeoPoints().observe(this, Observer {
            setStatus(it)
            if (it.status == Status.SUCCESS){
                if (it.data?.pointA != null && it.data.pointB!=null){
                    setUpPointA(it.data.pointA!!)
                    setUpPointB(it.data.pointB!!)
                    distanceAB(it.data.pointA!!, it.data.pointB!!)


                }
            }
        })
        viewModel.observeOfflineGeoPointsObservable().observe(this) {
            if (it.data.isNullOrEmpty()) {
            } else {

            }
        }


        viewModel.observeGeoLinePoints().observe(this, Observer {
            setStatusB(it)
            if (it.status == Status.SUCCESS) {
                if (it.data?.coordinates != null ){
                    setUpLineCoordinates(it.data.coordinates!!)
                }
            }
        })

   viewModel.polygonObservable().observe(this, Observer {
       setStatusC(it)
       if (it.status == Status.SUCCESS) {
           if (it.data?.coordinates != null ){
               setUpPolygonCoordinates(it.data.coordinates!!)
           }

       }
   })


    }
    private fun distanceAB(pointA: List<Double>,pointB: List<Double>){
        this.pointA = pointA
        latitudeA=pointA.first().toString()
        longitudeA=pointA.last().toString()
        this.pointB = pointB
        latitudeB=pointB.first().toString()
        longitudeB=pointB.last().toString()
        val geoPoints: MutableList<GeoPoint> = ArrayList()
        geoPoints.add(GeoPoint(latitudeA!!.toDouble(), longitudeA!!.toDouble()))
        geoPoints.add(GeoPoint( latitudeB!!.toDouble(), longitudeB!!.toDouble()))
        val line = Polyline() //see note below!
        line.setPoints(geoPoints)
        distanceValue.text= round(line.distance).toString()
        line.setOnClickListener { polyline, mapView, eventPos ->

            false
        }
        map.overlayManager.add(line)


    }
    private  fun setUpPointA(pointA: List<Double>){
        this.pointA = pointA
        latitudeA=pointA.first().toString()
        longitudeA=pointA.last().toString()
        map.setMultiTouchControls(true)
        val mapController = map.controller
        val startPoint: GeoPoint = GeoPoint(pointA.first(),pointA.last())
        mapController.setZoom(16.0)
        mapController.setCenter(startPoint)
        map.invalidate()
        if (map == null) {
            return
        }
        val my_marker = Marker(map)
        my_marker.position = GeoPoint(pointA.first(), pointA.last())
        my_marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        my_marker.title = "point A"
        my_marker.setPanToView(true)
        map.overlays.add(my_marker)
        map.invalidate()

    }
    private  fun setUpPointB(pointB: List<Double>){
        this.pointB = pointB
        latitudeB=pointB.first().toString()
        longitudeB=pointB.last().toString()
        val my_marker = Marker(map)
        my_marker.position = GeoPoint(pointB.first(), pointB.last())
        my_marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        my_marker.title = "point B"
        my_marker.setPanToView(true)
        map.overlays.add(my_marker)
        map.invalidate()


    }
    private  fun setUpPolygonCoordinates(polygonCoordinates:List<List<List<Float>>>){
        this.polygonCoordinates = polygonCoordinates
        var a= polygonCoordinates.first()
        coordinatesPolygon=a
        val geoPoints: MutableList<GeoPoint> = ArrayList()
        for (i in coordinatesPolygon.indices) {
            geoPoints.add(GeoPoint(coordinatesPolygon[i].first().toDouble(),coordinatesPolygon[i].last().toDouble()))
        }
        val polygon = Polygon() //see note below

        polygon.fillColor = Color.argb(75, 255, 0, 0)
        geoPoints.add(geoPoints[0]) //forces the loop to close

        polygon.points = geoPoints
        polygon.title = "A sample polygon"

//polygons supports holes too, points should be in a counter-clockwise order

//polygons supports holes too, points should be in a counter-clockwise order
        val holes: MutableList<List<GeoPoint>> = java.util.ArrayList()
        holes.add(geoPoints)
        polygon.holes = holes

        map.overlayManager.add(polygon)


    }
    private  fun setUpLineCoordinates(lineCoordinates:List<List<List<Float>>>){
        this.lineCoordinates = lineCoordinates
        var a= lineCoordinates.first()
        cordinatesB=a
        val geoPoints: MutableList<GeoPoint> = ArrayList()
        for (i in cordinatesB.indices) {
            geoPoints.add(GeoPoint(cordinatesB[i].first().toDouble(),cordinatesB[i].last().toDouble()))
        }
            val line = Polyline()
            line.setPoints(geoPoints)
            line.setOnClickListener { polyline, mapView, eventPos ->

                false
            }
            map.overlayManager.add(line)

        for (i in cordinatesB.indices) {
        }

    }





}