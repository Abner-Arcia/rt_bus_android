package arcia.abner.rt_bus.viewmodel

import androidx.lifecycle.ViewModel
import arcia.abner.rt_bus.model.Route

class RoutesViewModel : ViewModel() {

    lateinit var routes: List<Route>
}