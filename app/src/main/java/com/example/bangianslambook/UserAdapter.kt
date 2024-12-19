
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.bangianslambook.UserData
import com.example.bangianslambook.UserDetailActivity
import com.example.bangianslambook.databinding.ItemUserBinding

class UserAdapter(
    private var userList: MutableList<UserData>,
    private val deleteListener: OnDeleteListener
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>(), Filterable {

    private var userListFull: MutableList<UserData> = userList.toMutableList() // Full list for filtering

    // Define an interface for the delete listener
    interface OnDeleteListener {
        fun onDelete(userToDelete: UserData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)

        // Set up item click listener to view user details
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, UserDetailActivity::class.java)

            // Pass the user data to the new activity
            intent.putExtra("Name", user.name)
            intent.putExtra("DateOfBirth", user.dob)
            intent.putExtra("Gender", user.gender)
            intent.putExtra("Address", user.address)
            intent.putExtra("ContactNumber", user.contactNumber)
            intent.putStringArrayListExtra("Hobbies", ArrayList(user.hobbies))
            intent.putExtra("Nickname", user.nickname)
            intent.putExtra("ChildhoodHero", user.childhoodHero)
            intent.putExtra("QuoteSaying", user.quoteSaying)
            intent.putExtra("FavoriteSong", user.favoriteSong)
            intent.putExtra("SchoolMemory", user.schoolMemory)

            // Start the activity
            context.startActivity(intent)
        }

        // Set up delete button click listener
        holder.binding.btnDelete.setOnClickListener {
            // Trigger the delete listener
            deleteListener.onDelete(user)
        }
    }

    override fun getItemCount(): Int = userList.size

    class UserViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserData) {
            binding.Title.text = user.name
            // Removed the Glide code for displaying photo
        }
    }

    // Filter logic
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<UserData>()
                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(userListFull)  // Show all when no search query
                } else {
                    val filterPattern = constraint.toString().lowercase().trim()
                    for (item in userListFull) {
                        if (item.name.lowercase().contains(filterPattern) ||
                            item.nickname.lowercase().contains(filterPattern) ||
                            item.address.lowercase().contains(filterPattern)) {
                            filteredList.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                userList.clear()
                if (results?.values is List<*>) {
                    @Suppress("UNCHECKED_CAST")
                    userList.addAll(results.values as List<UserData>)
                }
                notifyDataSetChanged()
            }
        }
    }
}


