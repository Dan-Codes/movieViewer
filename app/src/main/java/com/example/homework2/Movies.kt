package com.example.homework2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.core.view.GestureDetectorCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_movies.*
import kotlinx.android.synthetic.main.activity_movies.movieView
import java.lang.Math.abs
private const val DEBUG_TAG = "Gestures"
class Movies : AppCompatActivity(),GestureDetector.OnGestureListener {
    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        Log.d(DEBUG_TAG, "onSingleTapUp: $e")
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (mDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }

    override fun onDown(event: MotionEvent): Boolean {
        Log.d(DEBUG_TAG, "onDown: $event")
        return true
    }

    override fun onFling(
        event1: MotionEvent,
        event2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        Log.d(DEBUG_TAG, "onFling: $event1 $event2")

        when (event1?.action) {
            MotionEvent.ACTION_UP -> {
                //downX = event.x // starting point

            }
            MotionEvent.ACTION_DOWN -> {
                val deltaX = event1.x - event2.x // calculate distance
// horizontal swipe action detection
                if (kotlin.math.abs(deltaX) > 20F) {
// left to right or right to next?
                    if ( deltaX > 0 ){
// right to left (move to next movie)
                        movieIndex++
                        if (movieIndex >= movieList.size){ // last movie, stay!!
                            movieIndex = movieList.size - 1
                        } else {
                            loadMovieInfo()
                        }
                    } else {
// left to right (move to previous movie)
                        movieIndex--
                        if (movieIndex < 0) { // first movie, stay!!
                            movieIndex = 0
                        } else {
                            loadMovieInfo()
                        }
                    }
                }
            }
        }
        return true
    }

    override fun onLongPress(event: MotionEvent) {
        Log.d(DEBUG_TAG, "onLongPress: $event")
    }

    override fun onScroll(
        event1: MotionEvent,
        event2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        Log.d(DEBUG_TAG, "onScroll: $event1 $event2")
        return true
    }

    override fun onShowPress(event: MotionEvent) {
        Log.d(DEBUG_TAG, "onShowPress: $event")
    }

    lateinit var movieList: List<MovieData>
    lateinit var posterTable:MutableMap<String, Int>
    private var movieIndex: Int = 0
    private lateinit var snack: Snackbar
    //private const val DEBUG_TAG = "Gestures"
    var downX:Float = 0.0f
    private lateinit var mDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
        mDetector = GestureDetectorCompat(this, this)

        this.movieList = Gson().fromJson(movies, Array<MovieData>::class.java).asList()
        posterTable = mutableMapOf()
        posterTable["It Chapter Two"] = R.drawable.it
        posterTable["Spider-Man: Far from Home"] = R.drawable.spiderman
        posterTable["The Old Man & the Gun"] = R.drawable.old
        posterTable["Hustlers"] = R.drawable.hustlers
        posterTable["John Wick: Chapter 3 – Parabellum"] = R.drawable.johnwick
        loadMovieInfo()
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                           fromUser: Boolean) {

                  val w:Int = seekbar.width
                  val h = seekbar.height
                  var width = w*progress/100
                  var height = h*progress/100
                 movieView.layoutParams=ConstraintLayout.LayoutParams(width, height)
//                    movieView.scaleType = ImageView.ScaleType.CENTER_CROP
//                Toast.makeText(applicationContext, "seekbar progress: $progress", Toast.LENGTH_SHORT).show()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Toast.makeText(applicationContext, "seekbar touch stopped at ${seekBar.progress}%", Toast.LENGTH_SHORT).show()
            }
        })

        movieView.setOnClickListener {
            Toast.makeText(this, "Click detected", Toast.LENGTH_SHORT).show()
            snack=Snackbar.make(seekbar, "Hello", Snackbar.LENGTH_LONG)
            snack.show()

        }
        movieView.setOnLongClickListener{
            seekBar.progress = 50
            movieView.setImageResource(posterTable.get(movieList[movieIndex].title)!!)
            Toast.makeText(this, "Long click detected", Toast.LENGTH_SHORT).show()
            true
        }


        movieView.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.x // starting point
                }
                MotionEvent.ACTION_UP -> {
                    val deltaX = downX - event.x // calculate distance
// horizontal swipe action detection
                    if (kotlin.math.abs(deltaX) > 20F) {
// left to right or right to next?
                        if ( deltaX > 0 ){
// right to left (move to next movie)
                            movieIndex++
                            if (movieIndex >= movieList.size){ // last movie, stay!!
                                movieIndex = movieList.size - 1
                            } else {
                                loadMovieInfo()
                                Log.d("DEBUG", "onFling: $downX ${event.x}")
                            }
                        } else {
// left to right (move to previous movie)
                            movieIndex--
                            if (movieIndex < 0) { // first movie, stay!!
                                movieIndex = 0
                            } else {
                                loadMovieInfo()
                                Log.d("DEBUG", "onFling: $downX ${event.x}")
                            }
                        }
                    }
                }
            }
            v?.onTouchEvent(event) ?: true
        }
    }

//    override fun onFling(
//        event1: MotionEvent,
//        event2: MotionEvent,
//        velocityX: Float,
//        velocityY: Float
//    ): Boolean {
//        Log.d(DEBUG_TAG, "onFling: $event1 $event2")
//        return true
//    }

    private fun loadMovieInfo() {
// load image information with movieIndex
        movieView.setImageResource(posterTable.get(movieList[movieIndex].title)!!)
        movieTitle.text = movieList[movieIndex].title
//        movieID.text = movieList[movieIndex].id.toString()
        ratingBar.rating = ((movieList[movieIndex].vote_average.toFloat())*5)/10
        rating.text = (movieList[movieIndex].vote_average.toFloat()).toString()
        movieOverview.text = movieList[movieIndex].overview
//        imageHeight = `@+id/movieView`.drawable.intrinsicHeight
//        imageWidth = `@+id/movieView`.drawable.intrinsicWidth
//        changeImageViewSize(imageHeight, imageWidth)
    }
}


