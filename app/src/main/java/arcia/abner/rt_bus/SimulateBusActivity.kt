package arcia.abner.rt_bus

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import arcia.abner.rt_bus.databinding.ActivitySimulateBusBinding
import arcia.abner.rt_bus.model.Bus
import arcia.abner.rt_bus.webservice.AppRetrofit
import com.azure.messaging.webpubsub.WebPubSubClientBuilder
import com.azure.messaging.webpubsub.models.GetAuthenticationTokenOptions
import com.google.android.gms.location.*
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URI


class SimulateBusActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySimulateBusBinding
    private lateinit var requestLocationPermissionLauncher: ActivityResultLauncher<String>
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var locationCallback: LocationCallback
    private lateinit var webSocketClient: WebSocketClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindingAndSetContentView()
        requestLocationPermissionLauncher = getRequestLocationPermissionLauncher()
        setBtnStartSendingLocationListener()
    }

    private fun initBindingAndSetContentView() {
        binding = ActivitySimulateBusBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    private fun getRequestLocationPermissionLauncher(): ActivityResultLauncher<String> {
        return registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                getLocation()
            }
        }
    }

    private fun setBtnStartSendingLocationListener() {
        binding.btnStartSendingLocation.setOnClickListener {
            if (hasLocationPermission()) {
                getLocation()
            } else {
                requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        hideBtnAndShowLatLng()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = createLocationCallback()
        fusedLocationClient!!.requestLocationUpdates(
            createLocationRequest(),
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun hideBtnAndShowLatLng() {
        binding.btnStartSendingLocation.visibility = View.GONE
        binding.tvLatLng.visibility = View.VISIBLE
    }

    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun createLocationCallback(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                val locations = locationResult.locations
                val location = locations[locations.size - 1]
                sendLocation(location.latitude, location.longitude)
                binding.tvLatLng.text = "Lat: ${location.latitude} Lng: ${location.longitude}"
            }
        }
    }

    private fun sendLocation(lat: Double, lng: Double) {
        val call = AppRetrofit.webservice.updateBusLocation(lat, lng)
        call.enqueue(object : Callback<Bus> {
            override fun onResponse(call: Call<Bus>, response: Response<Bus>) {}

            override fun onFailure(call: Call<Bus>, t: Throwable) {}

        })
    }

    private fun receiveLocation() {
        val client = WebPubSubClientBuilder()
            .connectionString(BuildConfig.WEB_PUBSUB_CONNECTION_STRING)
            .hub(BuildConfig.WEB_PUBSUB_HUB_NAME)
            .buildClient()

        val token = client.getAuthenticationToken(GetAuthenticationTokenOptions())

        webSocketClient = object : WebSocketClient(URI(token.url)) {
            override fun onMessage(message: String) {
                Toast.makeText(
                    applicationContext,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onClose(arg0: Int, arg1: String, arg2: Boolean) {}

            override fun onError(arg0: Exception) {}

            override fun onOpen(arg0: ServerHandshake) {}
        }

        webSocketClient.connect()
    }

}