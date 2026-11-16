class InventoryZone(val destination: Province) {
    private val parcels = mutableListOf<Parcel>()

    fun add(parcel: Parcel) {
        require(parcel.destinationProvince == destination) {
            "Parcel ${parcel.trackingId} does not belong to the ${destination.label} zone"
        }
        parcel.advanceTo(ParcelStatus.SORTED)
        parcels.add(parcel)
    }

    fun count(): Int = parcels.size
    fun getParcels(): List<Parcel> = parcels.toList()
}

open class Warehouse(
    name: String,
    province: Province,
    latitude: Double,
    longitude: Double
) : Location(name, province, latitude, longitude) {

    private val zones: Map<Province, InventoryZone> =
        Province.values().associateWith { InventoryZone(it) }

    fun receiveAndSort(parcel: Parcel) {
        zones.getValue(parcel.destinationProvince).add(parcel)
    }

    fun countFor(destination: Province): Int = zones.getValue(destination).count()
    fun totalParcels(): Int = zones.values.sumOf { it.count() }

    override fun describe() = "Warehouse: $name (${province.label})"
}

class ProvincialHub(
    name: String,
    province: Province,
    latitude: Double,
    longitude: Double
) : Warehouse(name, province, latitude, longitude) {
    override fun describe() = "Provincial Hub: $name (${province.label})"
}

interface RoutingStrategy {
    fun buildRoute(origin: ProvincialHub, destination: ProvincialHub): List<ProvincialHub>
}

class SameProvinceRoute : RoutingStrategy {
    override fun buildRoute(origin: ProvincialHub, destination: ProvincialHub) =
        listOf(origin)
}

class InterProvinceRoute : RoutingStrategy {
    override fun buildRoute(origin: ProvincialHub, destination: ProvincialHub) =
        listOf(origin, destination)
}
