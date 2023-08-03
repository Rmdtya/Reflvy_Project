import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reflvy.R
import com.example.reflvy.SliderModel

class SliderAdapter(var list: ArrayList<SliderModel>, var context: Context):RecyclerView.Adapter<SliderAdapter.viewHolder>(){
    class viewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun disableClick() {
            itemView.isClickable = false
            itemView.setOnClickListener(null)
        }

        val sliderImage:ImageView = itemView.findViewById(R.id.slider_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.slider_layout, parent, false)

        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        var currentItem= list[position]

        Glide.with(context.applicationContext)
            .load(currentItem.image)
            .into(holder.sliderImage)

        holder.itemView.setOnClickListener{

            Toast.makeText(context, currentItem.id, Toast.LENGTH_SHORT).show()
        }

        holder.disableClick()
    }

    override fun getItemCount(): Int {
        return  list.size
    }

}
