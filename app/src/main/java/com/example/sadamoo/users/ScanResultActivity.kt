package com.example.sadamoo.users

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sadamoo.R
import com.example.sadamoo.databinding.ActivityScanResultBinding
import androidx.core.content.res.ResourcesCompat


class ScanResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from intent
        val cattleType = intent.getStringExtra("cattle_type") ?: "Sapi Madura"
        val diseaseDetected = intent.getStringExtra("disease_detected") ?: "Cacingan"
        val confidenceScore = intent.getFloatExtra("confidence_score", 87.5f)
        val severity = intent.getStringExtra("severity") ?: "Sedang"
        val isHealthy = intent.getBooleanExtra("is_healthy", false)

        // Setup UI with scan results
        setupScanResults(cattleType, diseaseDetected, severity)
        setupBottomNavigation()
    }

    private fun setupScanResults(cattleType: String, disease: String, severity: String) {
        // Set cattle type
        binding.tvCattleType.text = cattleType

        // Set disease description based on detected disease
        when (disease.lowercase()) {
            "cacingan" -> {
                binding.tvDiseaseDescription.text = "Cacingan pada sapi adalah penyakit yang disebabkan oleh infeksi cacing parasit. Penyakit ini dapat mengerang sapi induk, anakan, atau pedet."
                setupCacinganSymptoms()
                setupCacinganTreatment()
                setupCacinganLosses()
            }
            "lumpy skin disease", "lsd" -> {
                binding.tvDiseaseDescription.text = "Lumpy Skin Disease (LSD) adalah penyakit virus yang menyerang sapi dan kerbau, ditandai dengan benjolan-benjolan pada kulit."
                setupLSDSymptoms()
                setupLSDTreatment()
                setupLSDLosses()
            }
            "penyakit mulut dan kuku", "pmk" -> {
                binding.tvDiseaseDescription.text = "Penyakit Mulut dan Kuku (PMK) adalah penyakit virus akut yang sangat menular pada hewan berkuku belah."
                setupPMKSymptoms()
                setupPMKTreatment()
                setupPMKLosses()
            }
            else -> {
                binding.tvDiseaseDescription.text = "Penyakit terdeteksi pada sapi. Segera konsultasikan dengan dokter hewan untuk penanganan yang tepat."
                setupGeneralSymptoms()
                setupGeneralTreatment()
                setupGeneralLosses()
            }
        }

        // Set scanned image (in real app, this would be the actual captured image)
        when (cattleType.lowercase()) {
            "sapi madura" -> binding.ivScannedImage.setImageResource(R.drawable.sapi_madura)
            "sapi brahman" -> binding.ivScannedImage.setImageResource(R.drawable.sapi_brahmana)
            "sapi limosin" -> binding.ivScannedImage.setImageResource(R.drawable.sapi_limosin)
            else -> binding.ivScannedImage.setImageResource(R.drawable.sapi_madura)
        }
    }

    private fun setupCacinganSymptoms() {
        val symptoms = listOf(
            "Tubuh sapi kurus dan lemah",
            "Nafsu makan dan minum berkurang",
            "Sering mencret",
            "Mulut dan hidung kering",
            "Telinga terkukul",
            "Mata terlihat suram, cekung, dan selalu mengantuk"
        )

        binding.layoutSymptoms.removeAllViews()
        symptoms.forEach { symptom ->
            val textView = createBulletTextView("• $symptom")
            binding.layoutSymptoms.addView(textView)
        }
    }

    private fun setupCacinganTreatment() {
        val treatments = listOf(
            "🔹 Beri Obat Cacing - Gunakan anthelmintik (obat cacing) sesuai dosis, seperti:",
            "   💊 Albendazole",
            "   💊 Ivermectin",
            "   💊 Levamisole",
            "🔹 Perbaiki nutrisi dengan pakan berkualitas",
            "🔹 Berikan vitamin dan mineral tambahan",
            "🔹 Isolasi sapi yang terinfeksi",
            "🔹 Konsultasi dengan dokter hewan"
        )

        binding.layoutTreatment.removeAllViews()
        treatments.forEach { treatment ->
            val textView = createBulletTextView(treatment)
            binding.layoutTreatment.addView(textView)
        }
    }

    private fun setupCacinganLosses() {
        val losses = listOf(
            "📉 Penurunan berat badan hingga 20-30%",
            "🥛 Penurunan produksi susu hingga 40%",
            "⚰️ Kematian pada kasus yang parah",
            "💸 Biaya pengobatan yang mahal",
            "🦠 Penularan ke sapi lain dalam kandang",
            "📊 Penurunan nilai jual sapi"
        )

        binding.layoutLosses.removeAllViews()
        losses.forEach { loss ->
            val textView = createBulletTextView(loss)
            binding.layoutLosses.addView(textView)
        }

        binding.tvFinancialLoss.text = "💰 Estimasi Kerugian: Rp 2.500.000 - Rp 5.000.000"
    }

    private fun setupLSDSymptoms() {
        val symptoms = listOf(
            "Benjolan keras (nodul) pada kulit berdiameter 2-5 cm",
            "Demam tinggi hingga 41°C",
            "Pembengkakan kelenjar getah bening",
            "Nafsu makan menurun drastis",
            "Penurunan produksi susu hingga 50%",
            "Edema pada kaki, skrotum, atau ambing"
        )

        binding.layoutSymptoms.removeAllViews()
        symptoms.forEach { symptom ->
            val textView = createBulletTextView("• $symptom")
            binding.layoutSymptoms.addView(textView)
        }
    }

    private fun setupLSDTreatment() {
        val treatments = listOf(
            "🔹 Tidak ada pengobatan spesifik untuk virus LSD",
            "🔹 Perawatan suportif dan simptomatik",
            "🔹 Antibiotik untuk mencegah infeksi sekunder",
            "🔹 Anti-inflamasi untuk mengurangi peradangan",
            "🔹 Perawatan luka dengan antiseptik",
            "🔹 Isolasi hewan terinfeksi",
            "🔹 Vaksinasi pencegahan"
        )

        binding.layoutTreatment.removeAllViews()
        treatments.forEach { treatment ->
            val textView = createBulletTextView(treatment)
            binding.layoutTreatment.addView(textView)
        }
    }

    private fun setupLSDLosses() {
        val losses = listOf(
            "📉 Penurunan produksi susu hingga 50%",
            "🥩 Kerusakan kulit menurunkan nilai jual",
            "⚰️ Tingkat kematian 1-5%",
            "🦠 Penyebaran cepat ke ternak lain",
            "💸 Biaya pengobatan dan vaksinasi tinggi",
            "🚫 Pembatasan perdagangan ternak"
        )

        binding.layoutLosses.removeAllViews()
        losses.forEach { loss ->
            val textView = createBulletTextView(loss)
            binding.layoutLosses.addView(textView)
        }

        binding.tvFinancialLoss.text = "💰 Estimasi Kerugian: Rp 5.000.000 - Rp 15.000.000"
    }

    private fun setupPMKSymptoms() {
        val symptoms = listOf(
            "Lepuh berisi cairan pada lidah, gusi, dan hidung",
            "Luka pada kuku dan sela-sela kuku",
            "Hewan pincang dan sulit berjalan",
            "Air liur berlebihan (hipersalivasi)",
            "Demam tinggi (40-41°C)",
            "Nafsu makan hilang",
            "Penurunan produksi susu drastis"
        )

        binding.layoutSymptoms.removeAllViews()
        symptoms.forEach { symptom ->
            val textView = createBulletTextView("• $symptom")
            binding.layoutSymptoms.addView(textView)
        }
    }

    private fun setupPMKTreatment() {
        val treatments = listOf(
            "🔹 Tidak ada pengobatan spesifik untuk virus PMK",
            "🔹 Perawatan suportif untuk mencegah infeksi sekunder",
            "🔹 Pemberian antibiotik untuk mencegah infeksi bakteri",
            "🔹 Perawatan luka dengan antiseptik",
            "🔹 Isolasi ketat hewan terinfeksi",
            "🔹 Pelaporan wajib ke Dinas Peternakan",
            "🔹 Vaksinasi pencegahan"
        )

        binding.layoutTreatment.removeAllViews()
        treatments.forEach { treatment ->
            val textView = createBulletTextView(treatment)
            binding.layoutTreatment.addView(textView)
        }
    }

    private fun setupPMKLosses() {
        val losses = listOf(
            "📉 Penurunan produksi susu hingga 70%",
            "⚰️ Tingkat kematian hingga 50% pada anak sapi",
            "🚫 Embargo perdagangan internasional",
            "💸 Kerugian ekonomi nasional miliaran rupiah",
            "🦠 Penyebaran sangat cepat dan luas",
            "🏭 Penutupan pasar dan pemotongan massal"
        )

        binding.layoutLosses.removeAllViews()
        losses.forEach { loss ->
            val textView = createBulletTextView(loss)
            binding.layoutLosses.addView(textView)
        }

        binding.tvFinancialLoss.text = "💰 Estimasi Kerugian: Rp 10.000.000 - Rp 50.000.000"
    }

    private fun setupGeneralSymptoms() {
        val symptoms = listOf(
            "Perubahan perilaku pada sapi",
            "Nafsu makan menurun",
            "Kondisi fisik yang tidak normal",
            "Gejala klinis yang mencurigakan"
        )

        binding.layoutSymptoms.removeAllViews()
        symptoms.forEach { symptom ->
            val textView = createBulletTextView("• $symptom")
            binding.layoutSymptoms.addView(textView)
        }
    }

    private fun setupGeneralTreatment() {
        val treatments = listOf(
            "🔹 Konsultasi segera dengan dokter hewan",
            "🔹 Isolasi sapi dari ternak lain",
            "🔹 Observasi gejala lebih lanjut",
            "🔹 Berikan perawatan suportif"
        )

        binding.layoutTreatment.removeAllViews()
        treatments.forEach { treatment ->
            val textView = createBulletTextView(treatment)
            binding.layoutTreatment.addView(textView)
        }
    }

    private fun setupGeneralLosses() {
        val losses = listOf(
            "📉 Penurunan produktivitas",
            "💸 Biaya pengobatan",
            "🦠 Risiko penularan",
            "📊 Penurunan nilai ekonomi"
        )

        binding.layoutLosses.removeAllViews()
        losses.forEach { loss ->
            val textView = createBulletTextView(loss)
            binding.layoutLosses.addView(textView)
        }

        binding.tvFinancialLoss.text = "💰 Estimasi Kerugian: Konsultasi dokter hewan"
    }

    private fun createBulletTextView(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = 14f
            setTextColor(getColor(android.R.color.black))
            setPadding(0, 4, 0, 4)
            typeface = ResourcesCompat.getFont(this@ScanResultActivity, R.font.quicksand)
        }
    }

    private fun setupBottomNavigation() {
        binding.navBack.setOnClickListener {
            finish()
        }

        binding.navSave.setOnClickListener {
            Toast.makeText(this, "Hasil scan disimpan ke riwayat", Toast.LENGTH_SHORT).show()
            // TODO: Save to database/history
        }

        binding.navConsultation.setOnClickListener {
            // Check subscription status
            val intent = Intent(this, ChatConsultationActivity::class.java).apply {
                putExtra("doctor_name", "Dr. Ahmad Veteriner")
                putExtra("consultation_id", "new_consultation")
            }
            startActivity(intent)
        }

    }
}
