package helpers

import model.Person
import responses.Exception500
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.text.SimpleDateFormat

class PersonHelper : DatabaseHelper() {

    fun getPersons(): List<Person> {
        val query: String = getQueryBuilder(0)
        val statement = prepareStatement(query)
        val rs = statement.executeQuery()
        return readPersonsFromResultSet(rs)
    }

    fun getPersons(personId: Int?): List<Person> {
        val query: String = getQueryBuilder(1)
        val statement = prepareStatement(query, personId)
        val rs = statement.executeQuery()
        return readPersonsFromResultSet(rs)
    }

    fun getPersons(passport: String?): List<Person> {
        val query: String = getQueryBuilder(2)
        val seria = passport?.split("-")?.get(0)?.toInt()
        val number = passport?.split("-")?.get(1)?.toInt()
        val statement = prepareStatement(query, seria, number)
        val rs = statement.executeQuery()
        return readPersonsFromResultSet(rs)
    }

    fun getPersons(surname: String?, name: String?, lastname: String?): List<Person> {
        var caseQuery = 3
        if (lastname != null) caseQuery += 1
        if (name != null) caseQuery += 2
        if (surname != null) caseQuery += 4
        val query: String = getQueryBuilder(caseQuery)
        val statement = prepareStatement(query, surname, name, lastname)
        val rs = statement.executeQuery()
        return readPersonsFromResultSet(rs)
    }

    fun deletePerson(person: Person) {
        if (person.passport != null)
            deletePassport(person.passport)
        val query = "DELETE FROM person WHERE id = ?"
        val statement = prepareStatement(query, person.person_id)
        statement.execute()
    }

    private fun deletePassport(passport: String) {
        val seria = passport.split("-")[0]
        val number = passport.split("-")[1]
        val query = "DELETE FROM passport WHERE Серия = ? AND Номер = ?"
        val statement = prepareStatement(query, seria.toInt(), number.toInt())
        statement.execute()
    }

    private fun getQueryBuilder(caseQuery: Int): String {
        val baseQuery = "SELECT person.id, Фамилия, Имя, Отчество, Дата_рождения, Серия, Номер " +
                "FROM person LEFT JOIN passport ON person.id = passport.person_id "
        return when (caseQuery) {
            0 -> baseQuery
            1 -> baseQuery + "WHERE person_id = ?"
            2 -> baseQuery + "WHERE Серия = ? AND Номер = ?"
            4 -> baseQuery + "WHERE Отчество = ?"
            5 -> baseQuery + "WHERE Имя = ?"
            6 -> baseQuery + "WHERE Имя = ? AND Отчество = ?"
            7 -> baseQuery + "WHERE Фамилия = ?"
            8 -> baseQuery + "WHERE Фамилия = ? AND Отчество = ?"
            9 -> baseQuery + "WHERE Фамилия = ? AND Имя = ?"
            10 -> baseQuery + "WHERE Фамилия = ? AND Имя = ? AND Отчество = ?"
            else -> throw Exception500.PersonDatabaseNoImpl()
        }
    }

    private fun prepareStatement(query: String, vararg fields: Any?): PreparedStatement {
        val statement = conn.prepareStatement(query)
        var currentField = 1
        fields.forEach { field -> if (field != null) statement.setObject(currentField++, field) }
        return statement
    }

    private fun readPersonsFromResultSet(rs: ResultSet): List<Person> {
        val persons = ArrayList<Person>()
        while (rs.next()) {
            val passport: String? =
                    if (rs.getString("Серия") == null || rs.getString("Номер") == null) null
                    else rs.getString("Серия") + "-" + rs.getString("Номер")
            val person = Person(
                    rs.getInt("id"),
                    rs.getString("Фамилия"),
                    rs.getString("Имя"),
                    rs.getString("Отчество"),
                    SimpleDateFormat("dd.MM.yyyy").format(rs.getDate("Дата_рождения")),
                    passport
            )
            persons.add(person)
        }
        return persons
    }

}


