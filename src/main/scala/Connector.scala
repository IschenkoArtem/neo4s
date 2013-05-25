import java.net.{HttpURLConnection, URLConnection, URL}
import scala.io.Source

/**
 * @author artem.ischenko
 *         Date: 5/25/13
 *         Time: 1:22 PM
 */
class Connector {
  val host = "http://localhost:7474/"
  val db = "db"
  val charset = "utf-8"

  def node(id: Int, fullInfo: Boolean = false) = {
    val con: URLConnection = new URL(s"$host$db/data/node/$id").openConnection
    con.addRequestProperty("Accept", "application/json")
    println(con.getHeaderFields.toString)
    con.getInputStream
  }

  def query(query: String, params: Map[String, String]) = {
    System.setProperty("http.keepAlive", "false");
    val con: URLConnection = new URL(s"$host$db/data/cypher").openConnection
    con.setDoOutput(true)
    con.addRequestProperty("Accept", "application/json")
    con.addRequestProperty("Content-Type", "application/json")
    val joinedParams = params.map(p => "\"" + p._1 + "\":\"" + p._2 + "\"").mkString(",")
    val requestBody = s"""{ "query":"$query", "params":{$joinedParams} }"""
    val os = con.getOutputStream()
    os.write(requestBody.getBytes(charset))
    os.close
    println(requestBody)
    try {
      println(Source.fromInputStream(con.getInputStream).getLines().mkString("\n"))
    } catch {
      case e: java.io.IOException => println(Source.fromInputStream(con.asInstanceOf[HttpURLConnection].getErrorStream).getLines().mkString("\n"))
    }
  }
}
