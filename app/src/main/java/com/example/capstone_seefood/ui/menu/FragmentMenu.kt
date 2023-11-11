import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import com.example.capstone_seefood.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.ChipGroup
import org.w3c.dom.Text

class FragmentMenu : Fragment() {

    private lateinit var scrollView: NestedScrollView
    private lateinit var chipGroup: ChipGroup


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

//        val menuList = listOf(
//            Menu("Mie Goreng", R.drawable.menumakan, "Rp 6000"),
//            Menu("Mie Goreng", R.drawable.menumakan, "Rp 6000"),
//            Menu("Mie Goreng", R.drawable.menumakan, "Rp 6000")
//        )
        scrollView = view.findViewById(R.id.scrollview)
        chipGroup = view.findViewById(R.id.chipGroup)

        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroup)
        val scrollView = view.findViewById<ScrollView>(R.id.scrollview)
        val cardViews = mutableListOf<MaterialCardView>()

        return TODO("Provide the return value")
    }
    }
    // Anda dapat menambahkan lebih banyak logika ke dalam metode ini sesuai kebutuhan Anda
