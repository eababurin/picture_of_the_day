package ru.eababurin.pictureoftheday.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.eababurin.pictureoftheday.R
import ru.eababurin.pictureoftheday.databinding.FragmentPictureBinding
import ru.eababurin.pictureoftheday.utils.BEFORE_YESTERDAY
import ru.eababurin.pictureoftheday.utils.TODAY
import ru.eababurin.pictureoftheday.utils.WIKI_SEARCH_URL
import ru.eababurin.pictureoftheday.utils.YESTERDAY
import ru.eababurin.pictureoftheday.viewmodel.AppState
import ru.eababurin.pictureoftheday.viewmodel.PictureOfTheDayViewModel
import java.text.SimpleDateFormat
import java.util.*

class PictureOfTheDayFragment : Fragment() {

    private var _binding: FragmentPictureBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this)[PictureOfTheDayViewModel::class.java]
    }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomAppBar: BottomAppBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPictureBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
        bottomAppBar = binding.bottomAppBar

        bottomAppBar.setNavigationOnClickListener {
            val bottomNavigationDrawerFragment = BottomNavigationDrawerFragment()
            bottomNavigationDrawerFragment.show(
                requireFragmentManager(),
                bottomNavigationDrawerFragment.tag
            )
        }

        bottomAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.app_bar_arrow -> {
                    if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        with(it) {
                            icon = resources.getDrawable(R.drawable.ic_baseline_arrow_up_24)
                            title = resources.getString(R.string.show_image_description)
                        }
                    } else {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        with(it) {
                            icon = resources.getDrawable(R.drawable.ic_baseline_arrow_down_24)
                            title = resources.getString(R.string.hide_image_description)
                        }
                    }
                    true
                }
                R.id.app_bar_fab -> {
                    Toast.makeText(
                        requireActivity(),
                        resources.getString(R.string.favourite),
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }
                R.id.app_bar_search -> {
                    Toast.makeText(
                        requireActivity(),
                        resources.getString(R.string.search),
                        Toast.LENGTH_SHORT
                    ).show()
                    true
                }
                R.id.app_bar_settings -> {
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .add(R.id.container, SettingsFragment.newInstance())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack("")
                        .commit()
                    true
                }
                else -> false
            }

        }

        viewModel.getLiveData().observe(viewLifecycleOwner) { appState -> renderData(appState) }

        viewModel.sendRequest(requireDate((0)))
        binding.chipToday.isChecked = true

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> { /*TODO("Not yet implemented")*/
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> { /*TODO("Not yet implemented")*/
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> { /*TODO("Not yet implemented")*/
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> { /*TODO("Not yet implemented")*/
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> { /*TODO("Not yet implemented")*/
                    }
                    BottomSheetBehavior.STATE_SETTLING -> { /*TODO("Not yet implemented")*/
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                /*TODO("Not yet implemented")*/
            }
        })

        binding.chipToday.setOnClickListener {
            binding.chipToday.isChecked = true
            viewModel.sendRequest(requireDate((TODAY)))
        }

        binding.chipYesterday.setOnClickListener {
            binding.chipYesterday.isChecked = true
            viewModel.sendRequest(requireDate((YESTERDAY)))
        }

        binding.chipBeforeYesterday.setOnClickListener {
            binding.chipBeforeYesterday.isChecked = true
            viewModel.sendRequest(requireDate((BEFORE_YESTERDAY)))
        }

        binding.chipHd.setOnClickListener {
            when (binding.chipGroup.checkedChipId) {
                binding.chipToday.id -> {
                    viewModel.sendRequest(requireDate(TODAY))
                }
                binding.chipYesterday.id -> {
                    viewModel.sendRequest(requireDate(YESTERDAY))
                }
                binding.chipBeforeYesterday.id -> {
                    viewModel.sendRequest(requireDate(BEFORE_YESTERDAY))
                }
            }
        }

        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(WIKI_SEARCH_URL + binding.input.text.toString())
            })
        }
    }

    private fun requireDate(offset: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, offset)
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.time).toString()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.bottom_app_bar_menu, menu)
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Error -> {
                binding.progressBar.visibility = View.INVISIBLE
                binding.imageView.visibility = View.VISIBLE
                binding.imageView.setImageResource(R.drawable.ic_no_image)
                Toast.makeText(
                    requireActivity(),
                    appState.error,
                    Toast.LENGTH_SHORT
                ).show()
            }
            is AppState.Success -> {
                binding.progressBar.visibility = View.INVISIBLE
                binding.imageView.visibility = View.VISIBLE

                if (!binding.chipHd.isChecked) {
                    binding.imageView.load(appState.pictureOfTheDayResponseData.url)
                } else {
                    binding.imageView.load(appState.pictureOfTheDayResponseData.hdurl)
                }

                requireView().findViewById<TextView>(R.id.bottom_sheet_description_header).text =
                    appState.pictureOfTheDayResponseData.title
                requireView().findViewById<TextView>(R.id.bottom_sheet_description).text =
                    appState.pictureOfTheDayResponseData.explanation
            }
            AppState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.imageView.visibility = View.INVISIBLE
            }
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
    }
}