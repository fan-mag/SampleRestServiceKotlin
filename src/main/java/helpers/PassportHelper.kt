package helpers

import model.Passport
import model.Person
import webservice.BaseService

class PassportHelper : DatabaseHelper() {
    fun getPassport(userId: Int, seria: Int, number: Int): List<Passport> {
        val query = StringBuilder("SELECT * FROM passport ")
        if (userId != 0)
            query.append("WHERE user_id = $userId")
        else {
            if (seria != 0) {
                query.append("WHERE Серия = $seria ")
                if (number != 0)
                    query.append("AND Номер = $number")
            } else
                if (number != 0)
                    query.append("WHERE Номер = $number")
        }
        val passports = ArrayList<Passport>()
        val rs = conn.createStatement().executeQuery(query.toString())
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
        if (passports.isEmpty()) throw BaseService.Success201()
        else return passports
    }

}