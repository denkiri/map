package com.deletech.maps
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.deletech.maps.custom.Resource
import com.deletech.maps.custom.Status
import com.deletech.maps.models.LineData
import com.deletech.maps.models.Point
import com.deletech.maps.models.PolygonData
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var pointA: List<Double>
    private lateinit var pointB: List<Double>
    var latitudeA: String? =null
    var longitudeA:String?=null
    var latitudeB: String? =null
    var longitudeB:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        init()
        setUpUi()
        button.setOnClickListener {
            val intent = Intent(this@MainActivity, MapActivity::class.java)
            intent.putExtra("latitudeA", latitudeA!!.toDouble())
            intent.putExtra("longitudeA", longitudeA!!.toDouble())
            intent.putExtra("latitudeB", latitudeB!!.toDouble())
            intent.putExtra("longitudeB", longitudeB!!.toDouble())
            startActivity(intent)
        }
    }
  private fun  init() {
     // viewModel.getOfflinePoint()
       viewModel.getGeoPoints()
       // viewModel.getGeoLine()
        //viewModel.getGeoPolygon()
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
                 //Snackbar.make(View(this@MainActivity), data.message.toString(), Snackbar.LENGTH_LONG).show()
            }
            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener({ init() })
        }
    }
    private fun setStatusB(data: Resource<LineData>) {
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
                //Snackbar.make(View(this@MainActivity), data.message.toString(), Snackbar.LENGTH_LONG).show()
            }
            empty_layout.visibility = View.VISIBLE
            main.visibility = View.GONE
            empty_button.setOnClickListener({ init() })
        }
    }
    private fun setStatusC(data: Resource<PolygonData>) {
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
                //Snackbar.make(View(this@MainActivity), data.message.toString(), Snackbar.LENGTH_LONG).show()
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
                  //  txt.text = it.data.pointA.toString()\
                    setUpPointA(it.data.pointA!!)
                    setUpPointB(it.data.pointB!!)


                }
            }
        })
        viewModel.observeOfflineGeoPointsObservable().observe(this) {
            if (it.data.isNullOrEmpty()) {
            } else {
               // txt.text= (it.data as ArrayList<PointA>).toString()
               // setUpPointA(it.data)
            }
        }

        /*
        viewModel.observeGeoLinePoints().observe(this, Observer {
            setStatusB(it)
            if (it.status == Status.SUCCESS) {
                 //   txt.text= it.data?.features.toString()

            }
        })
        viewModel.polygonObservable().observe(this, Observer {
            setStatusC(it)
            if (it.status == Status.SUCCESS) {
              //  txt.text= it.data?.type.toString()

            }
        })

         */
    }
    private  fun setUpPointA(pointA: List<Double>){
        this.pointA = pointA
        latitudeA=pointA.first().toString()
        longitudeA=pointA.last().toString()
        //txt.text= pointA.last().toString()

    }
    private  fun setUpPointB(pointB: List<Double>){
        this.pointB = pointB
        latitudeB=pointB.first().toString()
        longitudeB=pointB.last().toString()
        //txt.text= pointA.last().toString()

    }

}