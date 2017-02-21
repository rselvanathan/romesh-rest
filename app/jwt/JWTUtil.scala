package jwt

import domain.User
import io.jsonwebtoken.{Jwts, SignatureAlgorithm}

import scala.collection.JavaConverters._

/**
  * @author Romesh Selvan
  */
class JWTUtil {

  val CLAIM_ROLE = "role"
  val CLAIM_USERNAME = "username"

  def getTokenUser(implicit token : String) = getTokenField(CLAIM_USERNAME)

  def getTokenRole(implicit token: String) = getTokenField(CLAIM_ROLE)

  def generateToken(user: User) = generateTokenWithClaims(Map(CLAIM_USERNAME -> user.username, CLAIM_ROLE -> user.role))

  private def generateTokenWithClaims(map : Map[String, AnyRef]) =
    Jwts.builder().setClaims(map.asJava).signWith(SignatureAlgorithm.HS512, "").compact()

  private def getTokenField(field : String)(implicit token : String) = {
    val claimsFromToken = getClaimsFromToken
    if(claimsFromToken.isEmpty) None
    else                        Some(claimsFromToken.get.get(field, classOf[String]))
  }

  private def getClaimsFromToken(implicit token : String) = {
    try {
      Some(Jwts.parser().setSigningKey("").parseClaimsJws(token).getBody)
    } catch {
      case e : Exception => None
    }
  }
}
