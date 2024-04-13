import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.classroomapp.ClassDataClass
import com.example.classroomapp.R

class ClassAdapter(
    private var classList: List<ClassDataClass>,
    private val classClickListener: ClassClickListener
) : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    inner class ClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val batch: TextView = itemView.findViewById(R.id.batch)
        val subject: TextView = itemView.findViewById(R.id.subject)
        val teacherName: TextView = itemView.findViewById(R.id.teacherName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.room_facedesign, parent, false)
        return ClassViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val currentClass = classList[position]
        holder.batch.text = currentClass.batch
        holder.subject.text = currentClass.subject
        holder.teacherName.text = currentClass.teacherName

        holder.itemView.setOnClickListener {
            classClickListener.onClassClicked(currentClass)
        }
    }

    override fun getItemCount() = classList.size

    fun updateData(newList: List<ClassDataClass>) {
        classList = newList
        notifyDataSetChanged()
    }

    interface ClassClickListener {
        fun onClassClicked(classData: ClassDataClass)
    }
}
