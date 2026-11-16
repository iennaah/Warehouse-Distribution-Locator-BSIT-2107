enum class Province(val label: String) {
    CAVITE("Cavite"),
    LAGUNA("Laguna"),
    BATANGAS("Batangas"),
    RIZAL("Rizal"),
    QUEZON("Quezon")
}

enum class ParcelStatus { RECEIVED, SORTED, DISPATCHED, DELIVERED }

abstract class Location(
    val name: String,
    val province: Province,
    val latitude: Double,
    val longitude: Double
) {
    abstract fun describe(): String
}

class Parcel(
    val trackingId: String,
    val destinationMunicipality: String,
    val destinationProvince: Province
) {
    var status: ParcelStatus = ParcelStatus.RECEIVED
        private set

    fun advanceTo(newStatus: ParcelStatus) {
        if (newStatus.ordinal > status.ordinal) status = newStatus
    }
}
