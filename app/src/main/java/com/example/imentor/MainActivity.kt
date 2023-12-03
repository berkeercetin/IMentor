package com.example.imentor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.imentor.interfaces.HideToolbarInterface

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val toolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)


        // Geri butonunu etkinleştirme
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Geri butonuna tıklandığında yapılacak işlemler
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}