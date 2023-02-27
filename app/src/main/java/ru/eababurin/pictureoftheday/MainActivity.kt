package ru.eababurin.pictureoftheday

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.eababurin.pictureoftheday.databinding.ActivityMainBinding
import ru.eababurin.pictureoftheday.utils.THEME
import ru.eababurin.pictureoftheday.view.PictureOfTheDayFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(getPreferences(MODE_PRIVATE).getInt(THEME, R.style.Theme_PictureOfTheDay))

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commit()
        }
    }
}