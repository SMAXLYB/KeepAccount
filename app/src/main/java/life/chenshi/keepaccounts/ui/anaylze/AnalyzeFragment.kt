package life.chenshi.keepaccounts.ui.anaylze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import life.chenshi.keepaccounts.R
import life.chenshi.keepaccounts.databinding.FragmentAnaylzeBinding

class AnalyzeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAnaylzeBinding>(
            inflater,
            R.layout.fragment_anaylze,
            container,
            false
        )

        return binding.root
    }
}