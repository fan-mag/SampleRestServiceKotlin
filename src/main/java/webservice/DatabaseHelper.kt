package webservice

import java.sql.Connection
import java.sql.DriverManager
import java.text.SimpleDateFormat
import java.util.*

class DatabaseHelper {
    private var conn: Connection

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
        } else throw BaseService.Exception401()
    }


    fun deletePerson(id: Long) : Boolean {
        val querySelect: String = java.lang.String.format("SELECT COUNT(*) FROM person WHERE id = %d", id)
        val rs = conn.createStatement().executeQuery(querySelect)
        rs.next()
        val isRecordExist : Boolean = (rs.getInt("count") == 1)
        if (isRecordExist) {
            val queryDelete: String = java.lang.String.format("DELETE FROM person WHERE id = %d", id)
            conn.createStatement().execute(queryDelete)
        }
        return isRecordExist
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
        } else throw BaseService.Success201()
    }

    fun createPerson(surname: String, name: String, lastname: String, birthDate: Date): Long {
        val birth: String = SimpleDateFormat("yyyy-MM-dd").format(birthDate)
        val query: String = java.lang.String.format("INSERT INTO person " +
                "(id, Фамилия, Имя, Отчество, Дата_рождения) " +
                "VALUES (DEFAULT, '%s', '%s', '%s', '%s') RETURNING id AS ID",
                surname, name, lastname, birth)
        val rs = conn.createStatement().executeQuery(query)
        rs.next()
        return rs.getLong("ID")
    }

    fun updatePerson(id: Long, surname: String, name: String, lastname: String, birthDate: Date) {
        val birth: String = SimpleDateFormat("yyyy-MM-dd").format(birthDate)
        val query: String = java.lang.String.format("UPDATE person " +
                "SET Фамилия = '%s', Имя = '%s', Отчество = '%s', Дата_рождения = '%s' " +
                "WHERE id = %d", surname, name, lastname, birth, id)
        conn.createStatement().execute(query)
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
        return array
    }

    fun validateApiKey(apiKey: String) {
        val query: String = java.lang.String.format("SELECT COUNT(*) FROM credentials WHERE api_key = '%s'"
                , apiKey)
        val rs = conn.createStatement().executeQuery(query)
        rs.next()
        if (rs.getInt("count") == 0) throw BaseService.Exception401()
    }

    fun getCount(): Long {
        val query = "SELECT hitcount FROM rest_stat"
        val rs = conn.createStatement().executeQuery(query)
        rs.next()
        return rs.getLong("hitcount")
    }

    fun incrementCount() {
        val query = "UPDATE rest_stat SET hitcount = hitcount + 1"
        conn.createStatement().execute(query)
    }


}