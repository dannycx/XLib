package com.danny.demo.filepicker.view

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.danny.common.BaseResponse
import com.danny.demo.R
import com.danny.demo.databinding.ItemFileBinding
import com.danny.demo.filepicker.FilePickerConstants
import com.danny.demo.filepicker.bean.PickerFile
import com.danny.demo.filepicker.listener.FilePickerDurationCallback
import com.danny.xtool.UiTool
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.StringBuilder

class FilePickerAdapter(): RecyclerView.Adapter<FilePickerAdapter.XFilePickerHolder>() {
    private var items: ArrayList<PickerFile>? = null
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): XFilePickerHolder {
        context = parent.context
        val binding = ItemFileBinding.inflate(LayoutInflater.from(context), parent, false)
        return XFilePickerHolder(binding)
    }

    override fun getItemCount(): Int = items?.size ?: 0

    override fun onBindViewHolder(holder: XFilePickerHolder, position: Int) {
        holder.binding.xItemCheck.tag = position
        holder.binding.root.tag = position
        holder.binding.xItemCheck.setOnClickListener {
            val tag = holder.binding.root.tag as Int
            val var1 = items?.get(tag)
            var1?.let {
//                if () {// 超出大小不可选
//                    holder.binding.xItemCheck.isChecked = false
//                    return@setOnClickListener
//                }

                if (it.isHasOperated) {
                    holder.binding.xItemCheck.isChecked = true
                    return@setOnClickListener
                }

                val isChecked = holder.binding.xItemCheck.isChecked
                it.isSelect = isChecked
                items!![tag] = it

                val event = BaseResponse<PickerFile> ()
                event.code = FilePickerConstants.EVENT_CODE_CHECK
                if (isChecked) {
                    event.message = FilePickerConstants.EVENT_INCREMENT
                } else {
                    event.message = FilePickerConstants.EVENT_REDUCE
                }
                event.result = var1
                EventBus.getDefault().post(event)
            }
        }

        holder.binding.root.tag = position
        holder.binding.root.setOnClickListener {
            val tag = holder.binding.root.tag as Int
            val var1 = items?.get(tag)


            Toast.makeText(context, var1?.file?.canonicalPath ?: "qq", Toast.LENGTH_LONG).show()
            return@setOnClickListener

            var1?.run {
                if (FilePickerConstants.CATEGORY_IMAGE == category) {// 图片
                    val context = holder.binding.root.context
                    val intent = Intent(context, OpenImageActivity::class.java)
                    val view = holder.binding.xItemImg
                    intent.putExtra(FilePickerConstants.PREVIEW_IMAGE_KEY, file.absolutePath)
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context as Activity, view, view.transitionName).toBundle())
                } else {// 其他文件
                    val msg = BaseResponse<PickerFile>()
                    msg.code = FilePickerConstants.EVENT_CODE_ITEM_CLICK
                    msg.result = this
                }
            }
        }

        items?.apply {
            val pickerFile = this[position]
            holder.setData(pickerFile)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    fun setData(items: ArrayList<PickerFile>?) {
        this.items = items
        notifyDataSetChanged()
    }

    @Subscribe
    fun onAdapterEvent(event: BaseResponse<PickerFile>) {
        when (event.code) {
            FilePickerConstants.EVENT_CODE_DELETE -> {
                val pf = event.result
                pf?.run {
                    file?.run {
                        absolutePath.let { p ->
                            items?.let { it ->
                                for (i in it.indices) {
                                    val var1 = it[i]
                                    if (null == var1.file) {
                                        continue
                                    }
                                    if (p == var1.file.absolutePath) {
                                        var1.isSelect = false
                                        it[i] = var1
                                        notifyItemChanged(i)
                                        break
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun setRequest(request: Boolean) {
        if (request) {
            Glide.with(context!!).resumeRequests()
        } else {
            Glide.with(context!!).pauseRequests()
        }
    }

    inner class XFilePickerHolder(var binding: ItemFileBinding): RecyclerView.ViewHolder(binding.root) {

        fun setData(pickerFile: PickerFile) {
            binding.xItemFile.text = pickerFile.fileName
            binding.xItemSize.text = pickerFile.size
            binding.xItemTime.text = pickerFile.lastModify
            binding.xItemCheck.isChecked = pickerFile.isSelect
            val context = binding.root.context
            if (FilePickerConstants.EXTENSION_IMAGE.contains(pickerFile.extension)) {
                Glide.with(context)
                        .load(pickerFile.file)
//                        .placeholder()
//                        .error()
                        .thumbnail(0.1f)
                        .into(binding.xItemImg)
            } else if (FilePickerConstants.EXTENSION_VIDEO.contains(pickerFile.extension)) {
                binding.xItemImg.layoutParams.width =
                    UiTool.dimensionPixelSize(context, com.danny.common.R.dimen.dp_70)
                Glide.with(context)
                        .load(pickerFile.file)
                        .thumbnail(0.1f)
                        .into(binding.xItemImg)
                val builder = StringBuilder()
                binding.xItemDuration.tag = builder
                binding.xItemDuration.text = builder.toString()
                pickerFile.getDuration(object : FilePickerDurationCallback {
                    override fun onFailure(t: Throwable?) {
                        if (builder == binding.xItemDuration.tag) {
                            builder.append("00:00:00")
                            binding.xItemDuration.text = builder.toString()
                        }
                    }

                    override fun onResponse(duration: Int, durationStr: String?) {
                        if (builder == binding.xItemDuration.tag) {
                            builder.append(durationStr)
                            binding.xItemDuration.text = builder.toString()
                        }
                    }
                })

            } else {
                binding.xItemImg.layoutParams.width =
                    UiTool.dimensionPixelSize(context, com.danny.common.R.dimen.dp_48)
                Glide.with(context).load(R.mipmap.ic_launcher).thumbnail(0.1f).into(binding.xItemImg)
            }
        }
    }

}