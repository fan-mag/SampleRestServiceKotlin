package helpers

import webservice.BaseService

class CredentialsHelper : DatabaseHelper() {
    fun getApiKey(login: String, password: String): String {
        val query: String = java.lang.String.format("SELECT * FROM credentials WHERE login = '%s' AND password = '%s'"
                , login, password)
        val statement = conn.createStatement()
        val rs = statement.executeQuery(query)
        if (rs.next()) {
            return rs.getString("api_key")
        } else throw BaseService.Exception401()
    }

    fun validateApiKey(apiKey: String) {
        val query: String = java.lang.String.format("SELECT COUNT(*) FROM credentials WHERE api_key = '%s'"
                , apiKey)
        val rs = conn.createStatement().executeQuery(query)
        rs.next()
        if (rs.getInt("count") == 0) throw BaseService.Exception401()
    }
}