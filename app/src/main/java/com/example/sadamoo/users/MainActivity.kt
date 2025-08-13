package com.example.sadamoo.users

import com.example.sadamoo.R
import android.graphics.Color
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.google.firebase.firestore.FirebaseFirestore
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.sadamoo.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Load user info
        loadUserInfo()
        loadViewPagerSapi()
        // Setup navigation
        setupBottomNavigation()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.e("MainActivity", "Tidak ada user yang login, kembali ke LoginActivity")
            startActivity(Intent(this, com.example.sadamoo.LoginActivity::class.java))
            finish()
        } else {
            Log.d("MainActivity", "User login: ${currentUser.uid}")
        }
    }


    private fun loadUserInfo() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            Log.d("MainActivity", "UID user login: $uid")

            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val nama = document.getString("name") ?: "User"
                        Log.d("MainActivity", "Data Firestore: ${document.data}")
                        binding.tvWelcome.text = "Selamat Datang $nama!"
                    } else {
                        Log.e(
                            "MainActivity",
                            "Dokumen tidak ditemukan untuk UID: $uid"
                        )
                        binding.tvWelcome.text = "Selamat Datang!"
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("MainActivity", "Gagal ambil data: ${e.message}", e)
                    binding.tvWelcome.text = "Selamat Datang!"
                }
        } else {
            Log.e("MainActivity", "Tidak ada user yang login!")
        }
    }

    private fun loadViewPagerSapi() {
        val sapiList = listOf(
            Pair("Sapi Brahman", R.drawable.sapi_brahmana),
            Pair("Sapi Brangus", R.drawable.sapi_brangus),
            Pair("Sapi Simental", R.drawable.sapi_simmental),
            Pair("Sapi Limosin", R.drawable.sapi_limosin),
            Pair("Sapi Brahman Cross", R.drawable.sapi_brahman_cross),
            Pair("Sapi Ongole", R.drawable.sapi_ongole),
            Pair("Sapi Peranakan Ongole", R.drawable.sapi_po),
            Pair("Sapi Aceh", R.drawable.sapi_aceh),
            Pair("Sapi Bali", R.drawable.sapi_bali),
            Pair("Sapi Madura", R.drawable.sapi_madura)
        )

        val adapter = SapiPagerAdapter(sapiList)
        binding.viewPagerSapi.adapter = adapter
    }


    private fun setupBottomNavigation() {
        // Default: Beranda aktif
        setActiveNav(binding.navBeranda)

//        binding.fabDeteksi.setOnClickListener {
//            startActivity(Intent(this, CameraScanActivity::class.java))
//        }

        binding.navBeranda.setOnClickListener {
            setActiveNav(binding.navBeranda)
        }

        binding.navInformasi.setOnClickListener {
            setActiveNav(binding.navInformasi)
            // startActivity(Intent(this, InformationActivity::class.java))
        }

        binding.navRiwayat.setOnClickListener {
            setActiveNav(binding.navRiwayat)
            // startActivity(Intent(this, HistoryActivity::class.java))
        }

        binding.navProfil.setOnClickListener {
            setActiveNav(binding.navProfil)
            // startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun setActiveNav(activeNav: LinearLayout) {
        val allNavs =
            listOf(binding.navBeranda, binding.navInformasi, binding.navRiwayat, binding.navProfil)
        val activeColor = Color.parseColor("#4A90E2") // Biru
        val inactiveColor = Color.parseColor("#B0B0B0") // Abu-abu

        for (nav in allNavs) {
            val icon = nav.getChildAt(0) as ImageView
            val label = nav.getChildAt(1) as TextView

            if (nav == activeNav) {
                icon.setColorFilter(activeColor)
                label.setTextColor(activeColor)
            } else {
                icon.setColorFilter(inactiveColor)
                label.setTextColor(inactiveColor)
            }
        }
    }

}
