package com.example.applauncher

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applauncherlist.AppInfo
import com.example.applauncherlist.InstallationListener
import com.example.applauncherlist.LauncherInstance

class MainActivity : AppCompatActivity(), AppListAdapter.Listener {
    lateinit var contextData: MainActivity
    lateinit var recylcerView: RecyclerView
    lateinit var progress: RelativeLayout
    lateinit var search: EditText
    lateinit var adapter: AppListAdapter
    val appList = ArrayList<AppInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contextData = this
        recylcerView = findViewById<RecyclerView>(R.id.app_list)
        progress = findViewById<RelativeLayout>(R.id.progress)
        search = findViewById<EditText>(R.id.search)
        adapter = AppListAdapter(this)

        recylcerView.layoutManager = LinearLayoutManager(this)
        recylcerView.adapter = adapter

        setAppList()

        search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchData(p0.toString())
            }
        })

        LauncherInstance.setInstallationReceiver(
            this,
            object : InstallationListener {
                override fun installApp(packageName: String) {
                    Toast.makeText(
                        contextData,
                        "installed $packageName",
                        Toast.LENGTH_SHORT
                    ).show()

                    val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(contextData)
                    alertDialogBuilder.setMessage("Recently Installed App : $packageName")
                    alertDialogBuilder.setCancelable(true)

                    alertDialogBuilder.setPositiveButton(
                        getString(android.R.string.ok)
                    ) { dialog, _ ->
                        dialog.dismiss()
                        setAppList()
                    }

                    val alertDialog: AlertDialog = alertDialogBuilder.create()
                    alertDialogBuilder.setCancelable(false)
                    alertDialog.show()
                }

                override fun unInstallApp(packageName: String) {
                    val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(contextData)
                    alertDialogBuilder.setMessage("Recently Uninstalled App : $packageName")
                    alertDialogBuilder.setCancelable(true)

                    alertDialogBuilder.setPositiveButton(
                        getString(android.R.string.ok)
                    ) { dialog, _ ->
                        dialog.dismiss()
                        setAppList()
                    }

                    val alertDialog: AlertDialog = alertDialogBuilder.create()
                    alertDialogBuilder.setCancelable(false)
                    alertDialog.show()
                }
            })
    }

    private fun setAppList() {
        showProgress(true)
        val r = Runnable {
            appList.clear()
            appList.addAll(LauncherInstance.getApplications(packageManager))
            runOnUiThread {
                adapter.setDataList(appList)
                showProgress(false)
            }
        }

        Handler().postDelayed(r, 100)
    }

    private fun searchData(searchInput: String) {
        val searchedList = ArrayList<AppInfo>()
        for (data in appList) {
            if (data.appName.toLowerCase().contains(searchInput.toLowerCase()))
                searchedList.add(data)
        }

        adapter.setDataList(searchedList)
    }

    override fun launchApp(intent: Intent?) {
        startActivity(intent)
    }

    private fun showProgress(show: Boolean) {
        progress.visibility = if (show) View.VISIBLE else View.GONE
    }
}