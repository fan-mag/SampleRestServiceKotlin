package helpers

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.util.*

open class DatabaseHelper : Runnable {
    companion object {
        @Volatile
        var keepAlive = false
    }


    var conn: Connection
    val url = "jdbc:postgresql://fan-mag.ddns.net:58091/passport_table"
    val props = Properties()

    init {
        Class.forName("org.postgresql.Driver")
        props.setProperty("user", "postgres")
        props.setProperty("password", "study")
        props.setProperty("tcpKeepAlive", "true")
        conn = DriverManager.getConnection(url, props)
        Thread(this).start()
    }

    override fun run() {
        if (!keepAlive) {
            keepAlive = true
            while (true) {
                keepAlive()
                Thread.sleep(30000)
            }
        }
    }

    private fun keepAlive() {
        incrementCount("KeepAlive")
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