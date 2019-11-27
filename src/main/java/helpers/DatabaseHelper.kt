package helpers

import org.postgresql.util.PSQLException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

open class DatabaseHelper {

    protected fun prepareStatement(query: String, vararg fields: Any?): PreparedStatement {
        val statement = conn.prepareStatement(query)
        var currentField = 1
        fields.forEach { field -> if (field != null) statement.setObject(currentField++, field) }
        return statement
    }

    fun incrementCount(rowName: String) {
        val query = "UPDATE rest_stat SET hitcount = hitcount + 1 WHERE Name = '$rowName'"
        try {
            conn.createStatement().execute(query)
        } catch (exception: PSQLException) {
            conn.createStatement().execute(query)
        }
    }

    companion object {
        val url = "jdbc:postgresql://fan-mag.ddns.net:58091/passport_table?user=postgres&password=study"
        var conn: Connection = connection()
            get() {
                if (field.isClosed) {
                    println("Connection was closed, setting new connection")
                    field = connection()
                }
                return field
            }

        @JvmStatic
        fun connection(): Connection {
            Class.forName("org.postgresql.Driver")
            return DriverManager.getConnection(url)
        }

    }

}