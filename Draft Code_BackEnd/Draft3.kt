class SortingCenter(hubs: List<ProvincialHub>) {
    private val hubByProvince: Map<Province, ProvincialHub> = hubs.associateBy { it.province }

    fun getHub(province: Province): ProvincialHub = hubByProvince.getValue(province)
    fun getAllHubs(): List<ProvincialHub> = hubByProvince.values.toList()

    fun processParcel(parcel: Parcel, originProvince: Province): List<ProvincialHub> {
        val origin = getHub(originProvince)
        val destination = getHub(parcel.destinationProvince)

        val strategy: RoutingStrategy =
            if (originProvince == parcel.destinationProvince) SameProvinceRoute()
            else InterProvinceRoute()

        val route = strategy.buildRoute(origin, destination)
        origin.receiveAndSort(parcel)
        return route
    }

    fun itemsPerProvince(): Map<Province, Int> =
        Province.values().associateWith { province ->
            getAllHubs().sumOf { it.countFor(province) }
        }
}
