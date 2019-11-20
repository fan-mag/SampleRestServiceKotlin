package helpers

import model.Person
import webservice.BaseService
import java.text.SimpleDateFormat
import java.util.*

class PersonHelper : DatabaseHelper(){
    fun getPerson(id: Int): ArrayList<Person> {
        val query: String = java.lang.String.format("SELECT * FROM person WHERE id = %d", id)
        val rs = conn.createStatement().executeQuery(query)
        val array = ArrayList<Person>()
        if (rs.next()) {
            array.add(Person(rs.getLong("id"), rs.getString("Фамилия"),
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

    fun getPerson(surname: String, name: String, lastname: String): ArrayList<Person> {
        val query: String = java.lang.String.format("SELECT * FROM person WHERE Фамилия LIKE '%%%s' AND Имя LIKE '%%%s' AND Отчество LIKE '%%%s'"
                , surname, name, lastname)
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