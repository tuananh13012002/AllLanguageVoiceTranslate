package com.all.language.translate.voice.translator.ui.permission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.all.language.translate.voice.translator.activities.MainActivity
import com.all.language.translate.voice.translator.base.activities.BaseActivity
import com.all.language.translate.voice.translator.databinding.ActivityPermissionBinding
import com.all.language.translate.voice.translator.ui.utils.DeviceUtil

@Suppress("CAST_NEVER_SUCCEEDS", "DEPRECATION")
class PermissionAct : BaseActivity() {
    private lateinit var binding: ActivityPermissionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnGo.setOnClickListener {
            if (binding.switch1.isChecked) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        binding.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            checkStorePermission()
        }
        binding.switch2.setOnCheckedChangeListener { buttonView, isChecked ->
            checkCameraPermission()
        }

        maxPermission()
    }

    private fun checkStorePermission() {
        try {
            if (DeviceUtil.hasStoragePermission(this)) {
                binding.switch1.isChecked = true
                binding.switch1.isEnabled = false
            } else {
                binding.switch1.isEnabled = true
                binding.switch1.isChecked = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                        REQUEST_CODE_STORAGE_PERMISSION
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        REQUEST_CODE_STORAGE_PERMISSION
                    )
                }

            }
        } catch (e: Exception) {
            Log.i("Tuananh", e.toString())
        }
    }

    private fun maxPermission() {
        if (!binding.switch1.isChecked) {
            binding.switch1.setOnClickListener {
                indexClickPermissionStore++
                if (indexClickPermissionStore > 2) {
                    startSettingApp()
                }
            }
        }

        if (!binding.switch2.isChecked) {
            binding.switch2.setOnClickListener {
                indexClickPermissionCamera++
                if (indexClickPermissionCamera > 2) {
                    startSettingApp()
                }
            }
        }
    }

    private fun startSettingApp() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        requestAppSettingsLauncher.launch(intent)
    }

    private val requestAppSettingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        }

    private fun checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            binding.switch2.isChecked = true
            binding.switch2.isEnabled = false
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
            binding.switch2.isEnabled = true
            binding.switch2.isChecked = false
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (indexClickPermissionStore > 2) {
            checkStorePermission()
        }
        if (indexClickPermissionCamera > 2) {
            checkCameraPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_STORAGE_PERMISSION -> {
                binding.switch1.isChecked =
                    grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            }

            CAMERA_PERMISSION_CODE -> {
                binding.switch2.isChecked =
                    grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (binding.switch1.isChecked ) {
            binding.btnGo.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        binding.switch1.isChecked = DeviceUtil.hasStoragePermission(this)
    }

    companion object {
        const val REQUEST_CODE_STORAGE_PERMISSION = 101
        const val CAMERA_PERMISSION_CODE = 1001
        var indexClickPermissionStore = 0
        var indexClickPermissionCamera = 0
    }
}