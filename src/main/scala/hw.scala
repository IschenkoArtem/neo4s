
;

object Hi {
  def main(args: Array[String]) = {
    new Connector().query("START a = node(0) RETURN a.name", Map())
  }
}
