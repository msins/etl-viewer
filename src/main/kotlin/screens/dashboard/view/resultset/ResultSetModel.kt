package screens.dashboard.view.resultset

import org.jooq.Record
import org.jooq.Result

sealed class ResultSetModel {

    class Error(val message: String): ResultSetModel()

    class Success(
        val headers: Array<String>,
        val data: Array<Array<String>>
    ): ResultSetModel() {

        companion object {

            fun fromJooqResult(result: Result<Record>): Success {
                val rowsCount: Int = result.size
                val columnCount: Int = result.fields().size

                val headers = result
                    .fields()
                    .map { it.name }
                    .toTypedArray()

                val data = Array(rowsCount) { Array(columnCount) { "?" } }

                for (rowIndex in 0 until rowsCount) {
                    val fields = result[rowIndex]
                    for (columnIndex in 0 until columnCount) {
                        data[rowIndex][columnIndex] = fields[columnIndex].toString()
                    }
                }

                return Success(headers, data)
            }
        }
    }
}