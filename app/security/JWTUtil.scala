package security

import defaults.SystemValues
import domain.User
import io.jsonwebtoken.{Jwts, SignatureAlgorithm}

import scala.collection.JavaConverters._

/**
  * @author Romesh Selvan
  */
object JWTUtil {

  private val CLAIM_ROLE = "role"
  private val CLAIM_USERNAME = "username"

  def getTokenUser(implicit token : String) = getTokenField(CLAIM_USERNAME)

  def getTokenRole(implicit token: String) = getTokenField(CLAIM_ROLE)

  def generateToken(user: User) = generateTokenWithClaims(Map(CLAIM_USERNAME -> user.username, CLAIM_ROLE -> user.role))

  private def generateTokenWithClaims(map : Map[String, AnyRef]) =
    Jwts.builder().setClaims(map.asJava).signWith(SignatureAlgorithm.HS512, SystemValues.JWTSECRET).compact()

  private def getTokenField(field : String)(implicit token : String) = {
    val claimsFromToken = getClaimsFromToken
    if(claimsFromToken.isEmpty) None
    else                        Some(claimsFromToken.get.get(field, classOf[String]))
  }

  private def getClaimsFromToken(implicit token : String) = {
    try {
      Some(Jwts.parser().setSigningKey(SystemValues.JWTSECRET).parseClaimsJws(token).getBody)
    } catch {
      case e : Exception => None
    }
  }
}
