package ru.eababurin.pictureoftheday.view

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.FontsContract
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.provider.FontRequest
import androidx.core.provider.FontsContractCompat
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

    private val TAG = "PictureOfTheDayFragment"

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

    @RequiresApi(Build.VERSION_CODES.O)
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
                // Toast.makeText(requireActivity(), appState.error, Toast.LENGTH_SHORT).show()
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

        val request = FontRequest(
            "com.google.android.gms.fonts",
            "com.google.android.gms",
            "Orbitron",
            R.array.com_google_android_gms_fonts_certs
        )

        val callback = object : FontsContractCompat.FontRequestCallback() {
            override fun onTypefaceRetrieved(typeface: Typeface?) {
                super.onTypefaceRetrieved(typeface)
                requireView().findViewById<TextView>(R.id.bottom_sheet_description).typeface =
                    typeface
            }

            override fun onTypefaceRequestFailed(reason: Int) {
                super.onTypefaceRequestFailed(reason)
                when (reason) {
                    FAIL_REASON_FONT_LOAD_ERROR -> {
                        Log.d(TAG, "Ошибка загрузки шрифта")
                    }
                    FAIL_REASON_FONT_NOT_FOUND -> {
                        Log.d(TAG, "Шрифт не найден")
                    }
                    FAIL_REASON_FONT_UNAVAILABLE -> {
                        Log.d(TAG, "Шрифт недоступен")
                    }
                    FAIL_REASON_MALFORMED_QUERY -> {
                        Log.d(TAG, "Запрос не поддержан поставщиком шрифтов")
                    }
                    FAIL_REASON_PROVIDER_NOT_FOUND -> {
                        Log.d(TAG, "Поставщик шрифтов не найден")
                    }
                    FAIL_REASON_WRONG_CERTIFICATES -> {
                        Log.d(TAG, "Сертификат не соответсвует подписи")
                    }
                    else -> {
                        Log.d(TAG, "Неизвестная ошибка")
                    }
                }
            }
        }

        FontsContractCompat.requestFont(requireContext(), request, callback, Handler())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PictureOfTheDayFragment()
    }
}