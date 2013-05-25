import java.net.{URLConnection, URL}

/**
 * @author artem.ischenko
 *         Date: 5/25/13
 *         Time: 1:22 PM
 */
class Connector {
  val host = "http://localhost:7474/"
  val db = "db"

  def node(id: Int, fullInfo: Boolean = false) {
    val con: URLConnection = new URL(s"$host$db/data/node/$id").openConnection
    con.addRequestProperty("Accept", "application/json")
    println(con.getHeaderFields.toString)
    con.getInputStream
  }

  def query(query: String) {

  }
}
