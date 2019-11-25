package helpers

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

open class DatabaseHelper {

    var conn: Connection
        get() {
            if (field.isClosed) {
                println("Connection was closed, setting new connection")
                field = DriverManager.getConnection(url)
            }
            return field
        }

    val url = "jdbc:postgresql://fan-mag.ddns.net:58091/passport_table?user=postgres&password=study"

    init {
        Class.forName("org.postgresql.Driver")
        conn = DriverManager.getConnection(url)
    }

    protected fun prepareStatement(query: String, vararg fields: Any?): PreparedStatement {
        val statement = conn.prepareStatement(query)
        var currentField = 1
        fields.forEach { field -> if (field != null) statement.setObject(currentField++, field) }
        return statement
    }

    fun incrementCount(rowName: String) {
        val query = "UPDATE rest_stat SET hitcount = hitcount + 1 WHERE Name = '$rowName'"
        conn.createStatement().execute(query)
    }

}