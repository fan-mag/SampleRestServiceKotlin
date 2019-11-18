package webservice

import java.sql.Connection
import java.sql.DriverManager
import java.util.*

class DatabaseHelper() {
    lateinit var conn: Connection

    init {
        Class.forName("org.postgresql.Driver")
        val url = "jdbc:postgresql://fan-mag.ddns.net:58091/passport_table"
        val props = Properties()
        props.setProperty("user", "postgres")
        props.setProperty("password", "study")
        conn = DriverManager.getConnection(url, props)
    }

    fun getApiKey(login: String, password: String): String {
        val query: String = java.lang.String.format("SELECT * FROM credentials WHERE login = '%s' AND password = '%s'"
                , login, password)
        val statement = conn.createStatement()
        val rs = statement.executeQuery(query)
        if (rs.next()) {
            return rs.getString("api_key")
        } else throw WebService.InvalidCredentialException()
    }

    fun getPerson(id: Int): ArrayList<WebService.Person> {
        val query: String = java.lang.String.format("SELECT * FROM person WHERE id = %d", id)
        val rs = conn.createStatement().executeQuery(query)
        val array = ArrayList<WebService.Person>()
        if (rs.next()) {
            array.add(WebService.Person(rs.getLong("id"), rs.getString("Фамилия"),
                    rs.getString("Имя"), rs.getString("Отчество"),
                    rs.getDate("Дата_рождения")))
            return array
        } else throw WebService.NoContentError()
    }

    fun getPerson(surname: String, name: String, lastname: String): ArrayList<WebService.Person> {
        val query: String = java.lang.String.format("SELECT * FROM person WHERE Фамилия LIKE '%%%s' AND Имя LIKE '%%%s' AND Отчество LIKE '%%%s'"
                , surname, name, lastname)
        val rs = conn.createStatement().executeQuery(query)
        val array = ArrayList<WebService.Person>()
        while (rs.next()) {
            array.add(WebService.Person(rs.getLong("id"), rs.getString("Фамилия"),
                    rs.getString("Имя"), rs.getString("Отчество"),
                    rs.getDate("Дата_рождения")))
        }
        if (array.size == 0) throw WebService.NoContentError()
        else return array
    }

    fun validateApiKey(apiKey: String) {
        val query: String = java.lang.String.format("SELECT COUNT(*) FROM credentials WHERE api_key = '%s'"
                , apiKey)
        val rs = conn.createStatement().executeQuery(query)
        rs.next()
        if (rs.getInt("count") == 0) throw WebService.NoApiKeyProvidedException()
    }


    fun stop() {
        conn.close()
    }


}