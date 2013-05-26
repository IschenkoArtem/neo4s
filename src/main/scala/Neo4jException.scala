/**
 * @author artem.ischenko
 *         Date: 5/26/13
 *         Time: 2:28 PM
 */
case class Neo4jException(errorMsg:String)  extends Exception(errorMsg){}
