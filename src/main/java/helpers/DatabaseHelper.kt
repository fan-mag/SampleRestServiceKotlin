package helpers

import java.sql.Connection
import java.sql.DriverManager
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

}