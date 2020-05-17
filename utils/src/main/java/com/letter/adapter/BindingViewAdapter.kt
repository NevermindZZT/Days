package com.letter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.letter.presenter.ItemClickPresenter
import com.letter.presenter.ItemLongClickPresenter

/**
 * DataBind 列表适配器
 * @param T item类型
 * @property context Context context
 * @property layoutRes Int 布局资源id
 * @property list ObservableList<T> 数据列表
 * @property itemClickPresenter ItemClickPresenter? item点击处理
 * @property itemLongClickPresenter ItemLongClickPresenter? item长按处理
 * @constructor 构造一个适配器
 *
 * @author Letter(nevermindzzt@gmail.com)
 * @since 1.0.0
 */
class BindingViewAdapter<T>
constructor(private val context: Context,
            @LayoutRes private val layoutRes: Int,
            private val list: ObservableList<T>,
            private val br: Class<Any>,
            private val itemClickPresenter: ItemClickPresenter? = null,
            private val itemLongClickPresenter: ItemLongClickPresenter? = null)
    : RecyclerView.Adapter<BindingViewAdapter.BindingViewHolder<ViewDataBinding>>() {

    init {
        list.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<T>>() {
            override fun onChanged(sender: ObservableList<T>?) {
                notifyDataSetChanged()
            }

            override fun onItemRangeRemoved(
                sender: ObservableList<T>?,
                positionStart: Int,
                itemCount: Int
            ) {
                notifyItemRangeRemoved(positionStart, itemCount)
            }

            override fun onItemRangeMoved(
                sender: ObservableList<T>?,
                fromPosition: Int,
                toPosition: Int,
                itemCount: Int
            ) {
                notifyItemMoved(fromPosition, toPosition)
            }

            override fun onItemRangeInserted(
                sender: ObservableList<T>?,
                positionStart: Int,
                itemCount: Int
            ) {
                notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeChanged(
                sender: ObservableList<T>?,
                positionStart: Int,
                itemCount: Int
            ) {
                notifyItemRangeChanged(positionStart, itemCount)
            }

        })
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = BindingViewHolder(
        DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(context),
            layoutRes,
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holder: BindingViewHolder<ViewDataBinding>,
        position: Int
    ) {
        holder.binding.setVariable(br.getDeclaredField("adapter").getInt(br), this)
        holder.binding.setVariable(br.getDeclaredField("list").getInt(br), list)
        holder.binding.setVariable(br.getDeclaredField("position").getInt(br), position)
        if (itemClickPresenter != null) {
            holder.binding.setVariable(br.getDeclaredField("itemClickPresenter").getInt(br),
                itemClickPresenter)
        }
        if (itemLongClickPresenter != null) {
            holder.binding.setVariable(br.getDeclaredField("itemLongClickPresenter").getInt(br),
                itemLongClickPresenter)
        }
        holder.binding.executePendingBindings()
    }

    override fun getItemCount() = list.size

    /**
     * DataBinding 列表适配器ViewHolder
     * @param T : ViewDataBinding 数据绑定类型
     * @property binding T 数据绑定对象
     * @constructor 构造一个ViewHolder
     *
     * @author Letter(nevermindzzt@gmail.com)
     * @since 1.0.0
     */
    class BindingViewHolder<out T : ViewDataBinding>
    constructor(val binding : T)
        : RecyclerView.ViewHolder(binding.root)

//    /**
//     * DataBinding 列表适配器BR
//     *
//     * @author Letter(nevermindzzt@gmail.com)
//     * @since 1.0.0
//     */
//    data class BR(var adapter: Int = 0,
//                  var list: Int = 0,
//                  var position: Int = 0,
//                  var itemClickPresenter: Int = 0,
//                  var itemLongClickPresenter: Int = 0) {
//        companion object {
//            const val _all = 0
//        }
//    }
}