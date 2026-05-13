package hu.bme.aut.android.tictactoe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.android.tictactoe.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
