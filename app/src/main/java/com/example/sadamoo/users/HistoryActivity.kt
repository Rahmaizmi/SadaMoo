package com.example.sadamoo.users

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sadamoo.R
import com.example.sadamoo.databinding.ActivityHistoryBinding
import com.example.sadamoo.users.adapters.HistoryAdapter
import com.example.sadamoo.users.models.HistoryItem
import com.example.sadamoo.users.models.HistoryType
import com.example.sadamoo.users.dialogs.AdvancedFilterDialog
import java.util.*

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private var allHistory = listOf<HistoryItem>()
    private var filteredHistory = listOf<HistoryItem>()
    private var currentFilter = "all"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
        setupHistoryData()
        setupRecyclerView()
        setupSearch()
        setupFilters()
    }

    private fun setupHistoryData() {
        // Mock data - in real app, load from Firebase/database
        allHistory = listOf(
            HistoryItem(
                id = "1",
                type = HistoryType.SCAN,
                title = "Scan Sapi Madura",
                subtitle = "Terdeteksi: Lumpy Skin Disease",
                date = Calendar.getInstance().apply { add(Calendar.HOUR, -2) }.time,
                status = "Selesai",
                cattleType = "Sapi Madura",
                diseaseDetected = "Lumpy Skin Disease",
                severity = "Berat"
            ),
            HistoryItem(
                id = "2",
                type = HistoryType.CONSULTATION,
                title = "Konsultasi dengan Dr. Ahmad",
                subtitle = "Pembahasan pengobatan LSD",
                date = Calendar.getInstance().apply { add(Calendar.HOUR, -5) }.time,
                status = "Selesai",
                doctorName = "Dr. Ahmad Veteriner",
                consultationDuration = "25 menit"
            ),
            HistoryItem(
                id = "3",
                type = HistoryType.SCAN,
                title = "Scan Sapi Brahman",
                subtitle = "Terdeteksi: Cacingan",
                date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) }.time,
                status = "Selesai",
                cattleType = "Sapi Brahman",
                diseaseDetected = "Cacingan",
                severity = "Sedang"
            ),
            HistoryItem(
                id = "4",
                type = HistoryType.CONSULTATION,
                title = "Konsultasi dengan Dr. Sari",
                subtitle = "Konsultasi pencegahan penyakit",
                date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -2) }.time,
                status = "Selesai",
                doctorName = "Dr. Sari Hewan",
                consultationDuration = "18 menit"
            ),
            HistoryItem(
                id = "5",
                type = HistoryType.SCAN,
                title = "Scan Sapi Limosin",
                subtitle = "Sapi dalam kondisi sehat",
                date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -3) }.time,
                status = "Selesai",
                cattleType = "Sapi Limosin",
                diseaseDetected = "Tidak ada penyakit",
                severity = "Sehat"
            )
        )

        filteredHistory = allHistory
        updateEmptyState()
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter(filteredHistory) { historyItem ->
            when (historyItem.type) {
                HistoryType.SCAN -> {
                    val intent = Intent(this, ScanResultActivity::class.java).apply {
                        putExtra("cattle_type", historyItem.cattleType)
                        putExtra("disease_detected", historyItem.diseaseDetected)
                        putExtra("severity", historyItem.severity)
                        putExtra("is_from_history", true)
                    }
                    startActivity(intent)
                }
                HistoryType.CONSULTATION -> {
                    // Navigate to chat consultation
                    val intent = Intent(this, ChatConsultationActivity::class.java).apply {
                        putExtra("doctor_name", historyItem.doctorName)
                        putExtra("consultation_id", historyItem.id)
                    }
                    startActivity(intent)
                }
            }
        }

        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            adapter = historyAdapter
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().lowercase().trim()
                filterHistory(query, currentFilter)
            }
        })

        binding.ivFilter.setOnClickListener {
            val dialog = AdvancedFilterDialog { criteria ->
                applyAdvancedFilter(criteria)
            }
            dialog.show(supportFragmentManager, "AdvancedFilter")
        }
    } //

        private fun applyAdvancedFilter(criteria: AdvancedFilterDialog.FilterCriteria) {
            var filtered = allHistory

            // Filter by date range
            if (criteria.dateRange.first != null && criteria.dateRange.second != null) {
                filtered = filtered.filter { item ->
                    item.date.after(criteria.dateRange.first) && item.date.before(criteria.dateRange.second)
                }
            }

            // Filter by status
            if (criteria.status != "Semua Status") {
                filtered = filtered.filter { it.status == criteria.status }
            }

            // Filter by severity
            if (criteria.severity != "Semua Tingkat") {
                filtered = filtered.filter { it.severity == criteria.severity }
            }

            // Filter by cattle type
            if (criteria.cattleType != "Semua Jenis") {
                filtered = filtered.filter { it.cattleType == criteria.cattleType }
            }

            filteredHistory = filtered
            historyAdapter.updateData(filteredHistory)
            updateEmptyState()

            android.widget.Toast.makeText(this, "Filter diterapkan: ${filtered.size} hasil", android.widget.Toast.LENGTH_SHORT).show()
        }

    private fun setupFilters() {
        binding.btnFilterAll.setOnClickListener {
            setActiveFilter("all")
            filterHistory(binding.etSearch.text.toString(), "all")
        }

        binding.btnFilterScan.setOnClickListener {
            setActiveFilter("scan")
            filterHistory(binding.etSearch.text.toString(), "scan")
        }

        binding.btnFilterConsultation.setOnClickListener {
            setActiveFilter("consultation")
            filterHistory(binding.etSearch.text.toString(), "consultation")
        }
    }

    private fun setActiveFilter(filter: String) {
        currentFilter = filter

        // Reset all buttons
        binding.btnFilterAll.background = getDrawable(R.drawable.filter_button_inactive)
        binding.btnFilterAll.setTextColor(Color.parseColor("#4A90E2"))
        binding.btnFilterScan.background = getDrawable(R.drawable.filter_button_inactive)
        binding.btnFilterScan.setTextColor(Color.parseColor("#4A90E2"))
        binding.btnFilterConsultation.background = getDrawable(R.drawable.filter_button_inactive)
        binding.btnFilterConsultation.setTextColor(Color.parseColor("#4A90E2"))

        // Set active button
        when (filter) {
            "all" -> {
                binding.btnFilterAll.background = getDrawable(R.drawable.filter_button_active)
                binding.btnFilterAll.setTextColor(Color.WHITE)
            }
            "scan" -> {
                binding.btnFilterScan.background = getDrawable(R.drawable.filter_button_active)
                binding.btnFilterScan.setTextColor(Color.WHITE)
            }
            "consultation" -> {
                binding.btnFilterConsultation.background = getDrawable(R.drawable.filter_button_active)
                binding.btnFilterConsultation.setTextColor(Color.WHITE)
            }
        }
    }

    private fun filterHistory(query: String, filter: String) {
        var filtered = allHistory

        // Filter by type
        when (filter) {
            "scan" -> filtered = filtered.filter { it.type == HistoryType.SCAN }
            "consultation" -> filtered = filtered.filter { it.type == HistoryType.CONSULTATION }
        }

        // Filter by search query
        if (query.isNotEmpty()) {
            filtered = filtered.filter { item ->
                item.title.lowercase().contains(query) ||
                        item.subtitle.lowercase().contains(query) ||
                        item.cattleType?.lowercase()?.contains(query) == true ||
                        item.diseaseDetected?.lowercase()?.contains(query) == true ||
                        item.doctorName?.lowercase()?.contains(query) == true
            }
        }

        filteredHistory = filtered
        historyAdapter.updateData(filteredHistory)
        updateEmptyState()
    }

    private fun updateEmptyState() {
        if (filteredHistory.isEmpty()) {
            binding.rvHistory.visibility = android.view.View.GONE
            binding.layoutEmptyState.visibility = android.view.View.VISIBLE
        } else {
            binding.rvHistory.visibility = android.view.View.VISIBLE
            binding.layoutEmptyState.visibility = android.view.View.GONE
        }
    }

    private fun setupBottomNavigation() {
        setActiveNav(binding.navRiwayat)

        binding.navBeranda.setOnClickListener {
            finish()
        }

        binding.navInformasi.setOnClickListener {
            startActivity(Intent(this, InformationActivity::class.java))
        }

        binding.fabDeteksi.setOnClickListener {
            startActivity(Intent(this, CameraScanActivity::class.java))
        }

        binding.navRiwayat.setOnClickListener {
            setActiveNav(binding.navRiwayat)
        }

        binding.navProfil.setOnClickListener {
            // startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun setActiveNav(activeNav: LinearLayout) {
        val allNavs = listOf(binding.navBeranda, binding.navInformasi, binding.navRiwayat, binding.navProfil)
        val activeColor = Color.parseColor("#4A90E2")
        val inactiveColor = Color.parseColor("#B0B0B0")

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

