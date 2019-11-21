package helpers

import errors.Exception401


class CredentialsHelper : DatabaseHelper() {
    fun getApiKey(login: String?, password: String?): String {
        val query = "SELECT * FROM credentials WHERE login = '$login' AND password = '$password'"
        val statement = conn.createStatement()
        val rs = statement.executeQuery(query)
        if (rs.next()) {
            return rs.getString("api_key")
        } else throw Exception401()
    }

    fun validateApiKey(apiKey: String?, privilegeLevel: Int) : Int {
        val query = "SELECT privilege FROM credentials WHERE api_key = '$apiKey'"
        val rs = conn.createStatement().executeQuery(query)
        if (rs.next()) {
            if (rs.getInt("privilege") < privilegeLevel) return 403
        } else return 401
        return 0
    }

    fun generateApiKey(login: String?, password: String?): String {
        val query = "SELECT COUNT(*) FROM credentials WHERE login = '$login' AND password = '$password'"
        val rs = conn.createStatement().executeQuery(query)
        rs.next()
        if (rs.getInt("count") == 0) throw Exception401()
        else {
            val newRandomKey: Long = Math.round(900000000000L * Math.random() + 100000000000L)
            val queryKey = "UPDATE credentials SET api_key = $newRandomKey WHERE login = '$login'"
            conn.createStatement().execute(queryKey)
            return newRandomKey.toString()
        }
    }
}