package metadata.dtos

class MetaDimFact(
    val idFactTable: Long,
    val idDimTable: Long,
    val factAttrOrdinal: Int,
    val dimAttrOrdinal: Int
) {

    override fun toString(): String {
        return "MetaDimFact(" +
                "idFactTable=$idFactTable, " +
                "idDimTable=$idDimTable, " +
                "factAttrOrdinal=$factAttrOrdinal, " +
                "dimAttrOrdinal=$dimAttrOrdinal)"
    }
}