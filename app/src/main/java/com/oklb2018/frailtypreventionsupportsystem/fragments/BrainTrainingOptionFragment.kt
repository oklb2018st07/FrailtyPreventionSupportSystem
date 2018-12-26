package com.oklb2018.frailtypreventionsupportsystem.fragments

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.oklb2018.frailtypreventionsupportsystem.R
import kotlinx.android.synthetic.main.sub_activity_main_brain_training_option.*

class BrainTrainingOptionFragment : Fragment() {
    var height: Int = 0
    var width: Int = 0

    public lateinit var onReadyListener: OnReadyListener

    private var skinId = 0
    private var difficulty = 5

    private val menus =
        List(ImageSkin.allImageList.size) { i -> OptionListData(ImageSkin.titles[i], ImageSkin.allImageList[i][0]) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.sub_activity_main_brain_training_option, container, false)
        root.post {
            height = root.measuredHeight
            width = root.measuredWidth
            initialize()
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ImageSkin.setSkinId(skinId)
        brainTrainingOptionButton01.setOnClickListener {
            onReadyListener.onReady(skinId, difficulty - 1)
        }
        val optionListAdapter = OptionListAdapter(this.context!!, menus)
        optionListView1.adapter = optionListAdapter

        optionListView1.setOnItemClickListener { parent, view, position, id ->
            for (v in parent.touchables) v.setBackgroundResource(R.drawable.colored_rectangler_00)
            parent.getChildAt(position).setBackgroundResource(R.drawable.colored_rectangler_01)
            skinId = id.toInt()
            ImageSkin.setSkinId(skinId)
            setComponents()
        }
        optionMinusButton.setOnClickListener {
            if (difficulty > 1) difficulty--
            difficultyTextView.text = "" + difficulty
        }
        optionPlusButton.setOnClickListener {
            if (difficulty < 9) difficulty++
            difficultyTextView.text = "" + difficulty
        }
        difficultyTextView.text = "" + difficulty
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        onReadyListener = context as OnReadyListener
    }

    private fun initialize() {
        val scale = previewLayout01.width
        Log.d("debug", "scale = $scale")
        previewImageView01.layoutParams.height = scale / 2
        previewImageView01.layoutParams.width = scale / 2
        previewImageView02.layoutParams.height = scale / 2
        previewImageView02.layoutParams.width = scale / 2
        previewImageView03.layoutParams.height = scale / 2
        previewImageView03.layoutParams.width = scale / 2
        previewImageView04.layoutParams.height = scale / 2
        previewImageView04.layoutParams.width = scale / 2
        setComponents()
    }

    private fun setComponents() {

        previewImageView01.setImageResource(ImageSkin.images[0])
        previewImageView02.setImageResource(ImageSkin.images[1])
        previewImageView03.setImageResource(ImageSkin.images[2])
        previewImageView04.setImageResource(ImageSkin.images[3])
    }
}

public interface OnReadyListener {
    public fun onReady(skinId: Int, difficulty: Int)
}

data class OptionListData(val title: String, val imageId: Int)

data class ViewHolder(val optionTextView: TextView, val optionImageView: ImageView)

class OptionListAdapter(context: Context, menus: List<OptionListData>) :
    ArrayAdapter<OptionListData>(context, 0, menus) {
    private val layoutInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var viewHolder: ViewHolder?
        var view = convertView
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_items_2, parent, false)
            viewHolder = ViewHolder(view.findViewById(R.id.optionTextView), view.findViewById(R.id.optionImageView))
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }
        val listItem = getItem(position)
        viewHolder!!.optionTextView.text = listItem!!.title
        viewHolder!!.optionImageView.setImageBitmap(BitmapFactory.decodeResource(context.resources, listItem.imageId))
        return view!!
    }
}