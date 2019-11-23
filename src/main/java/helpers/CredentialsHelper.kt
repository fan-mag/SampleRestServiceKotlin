package helpers

import responses.Exception401
import java.sql.PreparedStatement


class CredentialsHelper : DatabaseHelper() {
    fun getApiKey(login: String?, password: String?): String {
        val query = "SELECT * FROM credentials WHERE login = ? AND password = ?"
        val statement: PreparedStatement = conn.prepareStatement(query)
        statement.setString(1, login)
        statement.setString(2, password)
        val rs = statement.executeQuery()
        if (rs.next()) {
            return rs.getString("api_key")
        } else throw Exception401()
    }

    fun validateApiKey(apiKey: String?, privilegeLevel: Int): Int {
        val query = "SELECT privilege FROM credentials WHERE api_key = ?"
        val statement: PreparedStatement = conn.prepareStatement(query)
        apiKey?.toLongOrNull()?.let { statement.setLong(1, it) }
        val rs = statement.executeQuery()
        if (rs.next()) {
            if (rs.getInt("privilege") < privilegeLevel) return 403
        } else return 401
        return 0
    }

    fun generateApiKey(login: String?, password: String?): String {
        val query = "SELECT COUNT(*) FROM credentials WHERE login = ? AND password = ?"
        val statement: PreparedStatement = conn.prepareStatement(query)
        statement.setString(1, login)
        statement.setString(2, password)
        val rs = statement.executeQuery()
        rs.next()
        if (rs.getInt("count") == 0) throw Exception401()
        else {
            val newRandomKey: Long = Math.round(900000000000L * Math.random() + 100000000000L)
            val queryKey = "UPDATE credentials SET api_key = ? WHERE login = ?"
            val statement: PreparedStatement = conn.prepareStatement(queryKey)
            statement.setLong(1, newRandomKey)
            statement.setString(2, login)
            println(statement.executeUpdate())
            return newRandomKey.toString()
        }
    }
}