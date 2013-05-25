import org.scalatest.{Matchers, FlatSpec}
import scala.io.Source

/**
 * @author artem.ischenko
 *         Date: 5/25/13
 *         Time: 7:51 PM
 */
class AppSpec extends FlatSpec with Matchers{
  val host = "http://localhost:7474/db/data/"
  val root =s"""{
                    |    "extensions": {},
                    |    "outgoing_relationships": "${host}node/0/relationships/out",
                    |    "labels": "${host}node/0/labels",
                    |    "all_typed_relationships": "${host}node/0/relationships/all/{-list|&|types}",
                    |    "traverse": "${host}node/0/traverse/{returnType}",
                    |    "self": "${host}node/0",
                    |    "property": "${host}node/0/properties/{key}",
                    |    "properties": "${host}node/0/properties",
                    |    "outgoing_typed_relationships": "${host}node/0/relationships/out/{-list|&|types}",
                    |    "incoming_relationships": "${host}node/0/relationships/in",
                    |    "create_relationship": "${host}node/0/relationships",
                    |    "paged_traverse": "${host}node/0/paged/traverse/{returnType}{?pageSize,leaseTime}",
                    |    "all_relationships": "${host}node/0/relationships/all",
                    |    "incoming_typed_relationships": "${host}node/0/relationships/in/{-list|&|types}",
                    |    "data": {}
                    |}""".stripMargin.replaceAll("\\s+","")

  "Connector.node(0)" should "return information about root node" in{
    Source.fromInputStream(new Connector().node(0)).getLines().mkString("").replaceAll("\\s+","") should equal(root)
  }
  "Connector.query(\"START a = node(0) RETURN a.name\", Map())" should "return information about root node" in{
    Source.fromInputStream(new Connector().node(0)).getLines().mkString("").replaceAll("\\s+","") should equal(root)
  }
}
