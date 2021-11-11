package arcia.abner.rt_bus

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import arcia.abner.rt_bus.adapter.RoutesAdapter
import arcia.abner.rt_bus.databinding.ActivityRoutesBinding
import arcia.abner.rt_bus.model.Route
import arcia.abner.rt_bus.viewmodel.RoutesViewModel
import arcia.abner.rt_bus.webservice.AppRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoutesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoutesBinding
    private val viewModel: RoutesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindingAndSetContentView()
        getRoutes()
    }

    private fun initBindingAndSetContentView() {
        binding = ActivityRoutesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun getRoutes() {
        val getRoutesCall = AppRetrofit.webservice.getRoutes()
        getRoutesCall.enqueue(object : Callback<List<Route>> {
            override fun onResponse(call: Call<List<Route>>, response: Response<List<Route>>) {
                if (response.isSuccessful) {
                    viewModel.routes = response.body()!!
                    initRecyclerViewRoutes()
                } else {
                    showErrorToast()
                }
            }

            override fun onFailure(call: Call<List<Route>>, t: Throwable) {
                showErrorToast()
            }
        })
    }

    private fun initRecyclerViewRoutes() {
        binding.progressIndicator.visibility = View.GONE
        val routesAdapter = RoutesAdapter(viewModel.routes, ::routeItemClick)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.rvRoutes.apply {
            adapter = routesAdapter
            layoutManager = linearLayoutManager
        }
    }

    private fun routeItemClick(position: Int) {
        val intent = Intent(this, RouteMapActivity::class.java).apply {
            putExtra("route", viewModel.routes[position])
        }
        startActivity(intent)
    }

    private fun showErrorToast() {
        binding.progressIndicator.visibility = View.GONE
        Toast.makeText(
            applicationContext,
            getString(R.string.an_error_ocurred),
            Toast.LENGTH_SHORT
        ).show()
    }
}