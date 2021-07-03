package com.example.workmanagerapplication.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.example.workmanagerapplication.viewmodel.MainActivityViewModel
import com.example.workmanagerapplication.adapter.ImagesAdapter
import com.example.workmanagerapplication.databinding.ActivityMainBinding
import com.example.workmanagerapplication.service.DownloadImages


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainActivityViewModel
    private var imagesAdapter = ImagesAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = imagesAdapter
        binding.buttonCancel.setOnClickListener {
            WorkManager.getInstance(this).cancelAllWork()
        }
        binding.button.setOnClickListener {
            checkPermissions()
        }
        viewModel.getImagesFromDatabase(this)
        observerLiveData()
    }

    fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                1
            )
        } else {

            viewModel.getDataFromAPI(this)
        }
    }

    fun observerLiveData() {
        viewModel.imagesListLiveData.observe(this, Observer {
            it?.let {
                val data = Data.Builder().putStringArray("list", it.toTypedArray()).build()
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
                val myWorkRequest: WorkRequest =
                    OneTimeWorkRequestBuilder<DownloadImages>()
                        .setConstraints(constraints)
                        .setInputData(data)
                        .build()
                WorkManager.getInstance(this).enqueue(myWorkRequest)
                WorkManager.getInstance(this).getWorkInfoByIdLiveData(myWorkRequest.id)
                    .observe(this,
                        Observer {
                            if (it.state == WorkInfo.State.SUCCEEDED) {
                                binding.progressBar.visibility = View.GONE
                                binding.button.text = "Resimleri İndir"
                                binding.button.isEnabled = true
                                binding.buttonCancel.visibility = View.GONE
                                Toast.makeText(this, "İndirme Tamamlandı.", Toast.LENGTH_SHORT)
                                    .show()
                                viewModel.getImagesFromDatabase(this)

                            } else if (it.state == WorkInfo.State.FAILED) {
                                binding.progressBar.visibility = View.GONE
                                binding.button.text = "Resimleri İndir"
                                binding.button.isEnabled = true
                                binding.buttonCancel.visibility = View.GONE
                                Toast.makeText(this, "Hata oluştu.", Toast.LENGTH_SHORT).show()
                                viewModel.getImagesFromDatabase(this)
                                WorkManager.getInstance(this).cancelAllWork()

                            } else if (it.state == WorkInfo.State.RUNNING) {
                                binding.progressBar.visibility = View.VISIBLE
                                binding.button.text = "İndiriliyor..."
                                binding.button.isEnabled = false
                                binding.buttonCancel.visibility = View.GONE
                                Toast.makeText(this, "İndirme başladı.", Toast.LENGTH_SHORT).show()
                            }
                            else if (it.state == WorkInfo.State.ENQUEUED) {
                                binding.progressBar.visibility = View.GONE
                                binding.button.text = "Lütfen Bekleyin."
                                binding.button.isEnabled = false
                                binding.buttonCancel.visibility = View.VISIBLE
                                viewModel.getImagesFromDatabase(this)
                            }
                            else if (it.state == WorkInfo.State.CANCELLED) {
                                binding.progressBar.visibility = View.GONE
                                binding.button.text = "Resimleri İndir"
                                binding.button.isEnabled = true
                                binding.buttonCancel.visibility = View.GONE
                                viewModel.getImagesFromDatabase(this)
                            }
                        })
            }
        })
        viewModel.listOfImages.observe(this, Observer {
            it?.let {
                imagesAdapter.updateList(it)
            }
        })
    }

}