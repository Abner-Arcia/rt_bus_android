package arcia.abner.rt_bus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import arcia.abner.rt_bus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindingAndSetContentView()
        setBtnViewRoutesListener()
        setBtnSimulateBusListener()
    }

    private fun initBindingAndSetContentView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setBtnViewRoutesListener() {
        binding.btnViewRoutes.setOnClickListener {
            val intent = Intent(this, RoutesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setBtnSimulateBusListener() {
        binding.btnSimulateBus.setOnClickListener {
            val intent = Intent(this, SimulateBusActivity::class.java)
            startActivity(intent)
        }
    }
}