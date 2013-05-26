import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
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
    val joinedParams = params.map(p => "\"" + p._1 + "\":\"" + p._2 + "\"").mkString(",")
    val requestBody = s"""{ "query":"$query", "params":{$joinedParams} }"""
    val con = postConnection(s"$host$db/data/cypher",requestBody)
    read(con)
  }
  def statements(statements:Map[String,Map[String,String]]) = {
    val sts = statements.map(st=>{
      val query = st._1
      val params = st._2
      val joinedParams = params.map(p => "\"" + p._1 + "\":\"" + p._2 + "\"").mkString(",")
      s"""{ "statement":"$query", "parameters":{$joinedParams} }"""
    }).mkString(",")
    val requestBody = s"""{"statements" : [$sts]}"""
    val con = postConnection(s"$host$db/data/transaction/commit",requestBody)
    read(con)
  }
  def getSimpleResult(query: String, params: Map[String, String]) = {
    val result = Source.fromInputStream(this.query(query, params)).getLines().mkString("")
    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    val value = mapper.readValue(result, classOf[SResult])
    println(mapper.writeValueAsString(value))
    value.data.head.head
  }
  private def read(con:URLConnection) = {
    try {
      con.getInputStream
    } catch {
      case e: java.io.IOException => {
        throw new Neo4jException(Source.fromInputStream(con.asInstanceOf[HttpURLConnection].getErrorStream).getLines().mkString("\n"))
      }
    }
  }
  private def postConnection(url:String,body:String)={
    val con: URLConnection = new URL(url).openConnection
    con.setDoOutput(true)
    con.addRequestProperty("Accept", "application/json")
    con.addRequestProperty("Content-Type", "application/json")
    con.addRequestProperty("X-Stream", "true")
    val os = con.getOutputStream
    os.write(body.getBytes(charset))
    os.close()
    con
  }

}
