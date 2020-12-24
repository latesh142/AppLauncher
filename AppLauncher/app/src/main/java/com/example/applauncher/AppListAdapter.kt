package com.example.applauncher

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.applauncherlist.AppInfo
import kotlinx.android.synthetic.main.item_app_view.view.*

class AppListAdapter(listener: Listener) : RecyclerView.Adapter<AppListAdapter.ItemHolder>() {

    val appList = ArrayList<AppInfo>()

    var listener = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_app_view, parent, false)
        return ItemHolder(view)
    }

    fun setDataList(appList: ArrayList<AppInfo>) {
        this.appList.clear()
        this.appList.addAll(appList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return appList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = appList[position]
        holder.appName.text = data.appName
        holder.packageName.text = "Package Name : " + data.packageName
        holder.versionName.text = "Version Name : " + data.versionName
        holder.versionCode.text = "Version Code : " + data.versionCode

        if (data.icon != null)
            holder.icon.setImageDrawable(data.icon)

        if (data.launchIntent != null) {
            holder.launcher.visibility = View.VISIBLE
        } else {
            holder.launcher.visibility = View.GONE
        }



        holder.launcher.setOnClickListener { listener.launchApp(data.launchIntent) }
    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appName = view.app_name
        val launcher = view.launch
        val icon = view.icon
        val packageName = view.package_name
        val versionName = view.version_name
        val versionCode = view.version_code
    }

    interface Listener {
        fun launchApp(intent: Intent?)
    }
}