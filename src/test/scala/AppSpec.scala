import org.scalatest.{Matchers, FlatSpec}
import scala.io.Source

/**
 * @author artem.ischenko
 *         Date: 5/25/13
 *         Time: 7:51 PM
 */
class AppSpec extends FlatSpec with Matchers {

  "Connector.node(0)" should "return information about root node" in {
    Source.fromInputStream(new Connector().node(0)).getLines().mkString("").replaceAll("\\s+", "") should include ("\"data\":{}")
  }
  "Connector.query(\"START a = node(0) RETURN a\", Map())" should "return information about root node" in {
    Source.fromInputStream(new Connector().query("START a = node(0) RETURN a", Map())).getLines().mkString("") should include ("\"data\":{}")
  }
  "Connector.statements()" should "return information about root node" in {
    val sts:Map[String,Map[String,String]] = Map("START n = node(0) RETURN n" -> Map())
    val response = Source.fromInputStream(new Connector().statements(sts)).getLines().mkString("")

    response should include ("\"data\":[[{}]]")
  }

  "Connector.getSimpleResult()" should "return number of root nodes(wish is 1)" in{
    new Connector().getSimpleResult("START a = node(0) RETURN count(a)", Map()) should equal("1")
  }

}
