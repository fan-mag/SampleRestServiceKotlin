package helpers

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.util.*

open class DatabaseHelper {
    var conn: Connection
    val url = "jdbc:postgresql://fan-mag.ddns.net:58091/passport_table"
    val props = Properties()

    init {
        Class.forName("org.postgresql.Driver")
        props.setProperty("user", "postgres")
        props.setProperty("password", "study")
        conn = DriverManager.getConnection(url, props)

    }

    protected fun prepareStatement(query: String, vararg fields: Any?): PreparedStatement {
        val statement = conn.prepareStatement(query)
        var currentField = 1
        fields.forEach { field -> if (field != null) statement.setObject(currentField++, field) }
        return statement
    }

}