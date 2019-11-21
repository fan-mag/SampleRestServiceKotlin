package helpers

import model.Person
import java.text.SimpleDateFormat
import java.util.*

class PersonHelper : DatabaseHelper() {
    fun getPerson(id: Long): ArrayList<Person> {
        val query = "SELECT * FROM person WHERE id = $id"
        val rs = conn.createStatement().executeQuery(query)
        val array = ArrayList<Person>()
        if (rs.next()) {
            array.add(Person(rs.getLong("id"), rs.getString("Фамилия"),
                    rs.getString("Имя"), rs.getString("Отчество"),
                    rs.getDate("Дата_рождения")))
        }
        return array
    }

    fun createPerson(surname: String?, name: String?, lastname: String?, birthDate: Date): Long {
        val birth: String = SimpleDateFormat("yyyy-MM-dd").format(birthDate)
        val query: String = "INSERT INTO person " +
                "(id, Фамилия, Имя, Отчество, Дата_рождения) " +
                "VALUES (DEFAULT, '$surname', '$name', '$lastname', '$birth') RETURNING id AS ID"
        val rs = conn.createStatement().executeQuery(query)
        rs.next()
        return rs.getLong("ID")
    }

    fun updatePerson(id: Long?, surname: String?, name: String?, lastname: String?, birthDate: Date) {
        val birth: String = SimpleDateFormat("yyyy-MM-dd").format(birthDate)
        val query: String = "UPDATE person " +
                "SET Фамилия = '$surname', Имя = '$name', Отчество = '$lastname', Дата_рождения = '$birth' " +
                "WHERE id = $id"
        conn.createStatement().execute(query)
    }

    fun deletePerson(id: Long?) {
        val queryDelete = "DELETE FROM person WHERE id = $id"
        conn.createStatement().execute(queryDelete)
    }


    fun getPerson(surname: String, name: String, lastname: String): ArrayList<Person> {
        val query: String = "SELECT * FROM person WHERE Фамилия LIKE '%$surname' AND Имя LIKE '%$name' AND Отчество LIKE '%$lastname'" +
                ""
        val rs = conn.createStatement().executeQuery(query)
        val array = ArrayList<Person>()
        while (rs.next()) {
            array.add(Person(rs.getLong("id"), rs.getString("Фамилия"),
                    rs.getString("Имя"), rs.getString("Отчество"),
                    rs.getDate("Дата_рождения")))
        }
        return array
    }
}