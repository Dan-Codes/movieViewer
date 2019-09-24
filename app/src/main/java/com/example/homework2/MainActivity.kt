package com.example.homework2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.myToolbar))
// Get support action bar
        val appBar = supportActionBar
// set App Title
        appBar!!.title = "Android Two"
// set sub title
        appBar.subtitle = "User Interaction"
// Display app icon in toolbar
        appBar.setDisplayShowHomeEnabled(true)
        appBar.setLogo(R.mipmap.ic_launcher)
        appBar.setDisplayUseLogoEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
// inflate the menu into toolbar
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
/* handle selected (pressed) toolbar menu items */
        when (item?.itemId) {
            R.id.page1 -> {
            val intent = Intent(this, linearlayout::class.java).apply {
            }
            startActivity(intent)
        }
            R.id.page2 -> {
                val intent = Intent(this, calculator::class.java)
                startActivity(intent)
            }
            R.id.page3 -> {
                val intent = Intent(this, Movies::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
