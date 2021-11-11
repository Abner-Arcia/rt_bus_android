package arcia.abner.rt_bus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import arcia.abner.rt_bus.databinding.ActivityRouteMapBinding
import arcia.abner.rt_bus.model.Route
import com.azure.android.maps.control.AzureMap
import com.azure.android.maps.control.AzureMaps
import com.azure.android.maps.control.layer.LineLayer
import com.azure.android.maps.control.options.LineLayerOptions.strokeColor
import com.azure.android.maps.control.options.LineLayerOptions.strokeWidth
import com.azure.android.maps.control.source.DataSource
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point

class RouteMapActivity : AppCompatActivity() {

    companion object {
        init {
            AzureMaps.setSubscriptionKey(BuildConfig.AZURE_MAPS_SUBSCRIPTION_KEY)

            //Alternatively use Azure Active Directory authenticate.
            //AzureMaps.setAadProperties("<Your aad clientId>", "<Your aad AppId>", "<Your aad Tenant>")
        }
    }

    private lateinit var binding: ActivityRouteMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindingAndSetContentView()
        binding.mapControl.onCreate(savedInstanceState)

        //Wait until the map resources are ready.
        binding.mapControl.onReady { map -> drawRoute(map) }
    }

    private fun initBindingAndSetContentView() {
        binding = ActivityRouteMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun drawRoute(map: AzureMap) {
        val route = intent.getSerializableExtra("route") as Route

        //Create a data source and add it to the map.
        val source = DataSource()
        map.sources.add(source)

        //Create a list of points.
        val points = mutableListOf<Point>()
        route.points.forEach { point ->
            points.add(Point.fromLngLat(point.lng, point.lat))
        }

        //Create a LineString geometry and add it to the data source.
        source.add(LineString.fromLngLats(points))

        //Create a line layer and add it to the map.
        val layer = LineLayer(
            source,
            strokeColor("green"),
            strokeWidth(5f)
        )

        map.layers.add(layer)
    }

    public override fun onStart() {
        super.onStart()
        binding.mapControl.onStart()
    }

    public override fun onResume() {
        super.onResume()
        binding.mapControl.onResume()
    }

    public override fun onPause() {
        binding.mapControl.onPause()
        super.onPause()
    }

    public override fun onStop() {
        binding.mapControl.onStop()
        super.onStop()
    }

    override fun onLowMemory() {
        binding.mapControl.onLowMemory()
        super.onLowMemory()
    }

    override fun onDestroy() {
        binding.mapControl.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapControl.onSaveInstanceState(outState)
    }
}