package com.example.imentor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.imentor.main.services.concrates.TaskManager
import com.example.imentor.services.concrates.UserManager
import com.google.android.material.navigation.NavigationView
import GlobalService
import android.widget.TextView
import com.example.imentor.R
import com.example.imentor.auth.LoginFragment
import com.example.imentor.auth.services.concrates.AuthManager
import com.example.imentor.entities.User
import com.example.imentor.main.AddTask
import com.example.imentor.main.HomeFragment
import com.example.imentor.main.ProfileFragment
import com.example.imentor.main.SettingsFragment


class MainActivity : AppCompatActivity() {

    private val authManager = AuthManager()
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
        navView.getHeaderView(0).findViewById<TextView>(R.id.navUserName).text = ""
        navView.getHeaderView(0).findViewById<TextView>(R.id.navUserEmail).text = ""

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Navigation Drawer'ın menü öğelerine tıklama işlemlerini tanımlama
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    val fragment = HomeFragment()
                    val transaction = this.supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
                R.id.nav_account -> {
                    // Hesabım sayfasına git
                    val fragment = ProfileFragment()
                    val transaction = this.supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
                R.id.nav_settings -> {
                    // Ayarlar sayfasına git
                    val fragment = SettingsFragment()
                    val transaction = this.supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
                R.id.nav_logout -> {
                    authManager.logout()
                    GlobalService.user = null
                    GlobalService.userId=""
                    val fragment = LoginFragment()
                    val transaction = this.supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragmentContainerView, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }
    fun setNavHeader(user: User){
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.getHeaderView(0).findViewById<TextView>(R.id.navUserName).text = user.name
        navView.getHeaderView(0).findViewById<TextView>(R.id.navUserEmail).text = user.email
    }

    }

