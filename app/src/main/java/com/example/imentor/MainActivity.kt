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
import android.content.pm.PackageManager
import android.os.Build
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.imentor.R
import com.example.imentor.auth.LoginFragment
import com.example.imentor.auth.services.concrates.AuthManager
import com.example.imentor.entities.User
import com.example.imentor.main.AddTask
import com.example.imentor.main.HomeFragment
import com.example.imentor.main.ProfileFragment
import com.example.imentor.main.SettingsFragment
import android.Manifest.permission.POST_NOTIFICATIONS
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.imentor.main.StaticsFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {
    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()

            // FCM SDK (and your app) can post notifications.
        } else {
            Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            }   else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val authManager = AuthManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        askNotificationPermission()
        if (isPermissionGranted()) {
            requestPermission()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val toolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.i("FCM_TOKEN", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast

            Log.d("FCM_TOKEN1", token)
        })


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
                R.id.nav_statics -> {
                    val fragment = StaticsFragment()
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

    val ACTIVITY_RECOGNITION_REQUEST_CODE = 100

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),
                ACTIVITY_RECOGNITION_REQUEST_CODE
            )
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACTIVITY_RECOGNITION
        ) != PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ACTIVITY_RECOGNITION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                }
            }
        }
    }

    }

