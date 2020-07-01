package com.example.myphotostock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.myphotostock.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //display albums fragment
        loadFragment(AlbumsFragment())
        menu_bottom.setItemSelected(R.id.albmus)

        auth = FirebaseAuth.getInstance()

        menu_bottom.setOnItemSelectedListener { id->
            when(id){
                R.id.camera->loadFragment(CameraFragment())
                R.id.albmus->loadFragment(AlbumsFragment())
                R.id.scanner->loadFragment(ScannerFragment())
                R.id.logout-> {
                    auth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                else->{return@setOnItemSelectedListener}
            }
        }
    }


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().also { fragmentTransaction ->
            fragmentTransaction.replace(R.id.fragmentContainer, fragment)
            fragmentTransaction.commit()
        }
    }
}