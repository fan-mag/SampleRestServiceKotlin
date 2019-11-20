package helpers

class StatisticHelper : DatabaseHelper() {
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