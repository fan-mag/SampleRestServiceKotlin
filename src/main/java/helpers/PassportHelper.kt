package helpers

import model.Passport
import model.Person
import webservice.BaseService

class PassportHelper : DatabaseHelper() {
    fun getPassport(userId: Int, seria: Int, number: Int): List<Passport> {
        val query = selectQueryBuilder(userId, seria, number)
        val passports = ArrayList<Passport>()
        val rs = conn.createStatement().executeQuery(query)
        while (rs.next()) {
            var personId: Int? = rs.getInt("user_id")
            if (rs.wasNull()) personId = null
            val person: Person?
            if (personId != null)
                person = PersonHelper().getPerson(rs.getInt("user_id"))[0]
            else person = null
            val passport = Passport(rs.getInt("id"), person, rs.getInt("Серия"), rs.getInt("Номер"))
            passports.add(passport)
        }
        return passports
    }

    fun deletePassport(id: Int?, seria: Int?, number: Int?): Boolean {
        val query = StringBuilder("DELETE FROM passport WHERE ")
        if (id == null) {
            if (seria != null) {
                query.append("Серия = $seria")
                if (number != null) {
                    query.append(" AND Номер = $number")
                }
            } else {
                if (number != null) {
                    query.append(" Номер = $number")
                }
            }
        } else {
            query.append("id = $id")
        }

        return true
    }

    fun selectQueryBuilder(id: Int, seria: Int, number: Int): String {
        if (id != 0)
            return "SELECT * FROM passport WHERE user_id = $id"
        if (seria != 0) {
            if (number != 0)
                return "SELECT * FROM passport WHERE Серия = $seria AND Номер = $number"
            return "SELECT * FROM passport WHERE Серия = $seria"
        }
        if (number != 0)
            return "SELECT * FROM passport WHERE Номер = $number"
        return "SELECT * FROM passport"
    }

    fun deleteQueryBuilder(id: Int?, seria: Int?, number: Int?): String {
        return ""
    }
}