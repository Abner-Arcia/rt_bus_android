package arcia.abner.rt_bus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import arcia.abner.rt_bus.R
import arcia.abner.rt_bus.databinding.ListItemBinding
import arcia.abner.rt_bus.model.Route

class RoutesAdapter(
    private val routes: List<Route>,
    private val clickCallback: (position: Int) -> Unit
) : RecyclerView.Adapter<RoutesAdapter.ListItemHolder>() {

    class ListItemHolder(
        val itemBinding: ListItemBinding
    ) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val itemBinding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val route = routes[position]

        holder.itemBinding.ivIcon.setImageResource(R.drawable.ic_baseline_location_on_24)
        holder.itemBinding.tvText.text = route.name
        holder.itemBinding.tvSecondaryText.text = route.description
        holder.itemBinding.clItem.setOnClickListener { clickCallback(position) }
    }

    override fun getItemCount() = routes.size

}