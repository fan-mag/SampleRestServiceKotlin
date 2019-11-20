package helpers

import model.ApiKey
import webservice.BaseService

class CredentialsHelper : DatabaseHelper() {
    fun getApiKey(login: String?, password: String?): String {
        val query = "SELECT * FROM credentials WHERE login = '$login' AND password = '$password'"
        val statement = conn.createStatement()
        val rs = statement.executeQuery(query)
        if (rs.next()) {
            return rs.getString("api_key")
        } else throw BaseService.Exception401()
    }

    fun validateApiKey(apiKey: String, privilegeLevel: Int) {
        val query = "SELECT privilege FROM credentials WHERE api_key = '$apiKey'"
        val rs = conn.createStatement().executeQuery(query)
        if (rs.next()) {
            if (rs.getInt("privilege") < privilegeLevel) throw BaseService.Exception403()
        } else throw BaseService.Exception401()
    }

    fun generateApiKey(login: String?, password: String?): String {
        val query = "SELECT COUNT(*) FROM credentials WHERE login = '$login' AND password = '$password'"
        val rs = conn.createStatement().executeQuery(query)
        rs.next()
        if (rs.getInt("count") == 0) throw BaseService.Exception401()
        else {
            val newRandomKey: Long = Math.round(900000000000L * Math.random() + 100000000000L)
            val queryKey = "UPDATE credentials SET api_key = $newRandomKey WHERE login = '$login'"
            conn.createStatement().execute(queryKey)
            return newRandomKey.toString()
        }
    }
}