package com.example.imentor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val toolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)


        // Geri butonunu etkinleştirme
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Navigation Drawer'ı ayarlama
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Navigation Drawer'ın menü öğelerine tıklama işlemlerini tanımlama
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_account -> {
                    // Hesabım sayfasına git
                    Toast.makeText(this, "My Account", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_settings -> {
                    // Ayarlar sayfasına git
                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_logout -> {
                    // Çıkış yap
                    Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }


    }

