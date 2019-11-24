package helpers

import model.Passport


class PassportHelper : DatabaseHelper() {

    fun getPassport(passport: String): List<Passport> {
        val seria = passport.split("-")[0]
        val number = passport.split("-")[1]
        val query = "SELECT * FROM passport WHERE Серия = ? AND Номер = ?"
        val statement = prepareStatement(query, seria.toInt(), number.toInt())
        val rs = statement.executeQuery()
        val passports = ArrayList<Passport>()
        while (rs.next()) {
            val pasp = Passport(
                    rs.getInt("id"), rs.getInt("person_id"), rs.getInt("Серия"), rs.getInt("Номер")
            )
            passports.add(pasp)
        }
        return passports
    }

    fun addPassport(passport: String, personId: Int?) {
        val seria = passport.split("-")[0]
        val number = passport.split("-")[1]
        val query = "INSERT INTO passport (id, person_id, Серия, Номер) " +
                "VALUES (DEFAULT, ?, ?, ?)"
        val statement = prepareStatement(query, personId, seria.toInt(), number.toInt())
        statement.execute()
    }

}