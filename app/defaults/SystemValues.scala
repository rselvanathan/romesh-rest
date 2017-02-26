package defaults

/**
  * @author Romesh Selvan
  */
object SystemValues {
  val AWS_ACCESS_KEY =  sys.props.getOrElse("AWS_ACCESS_KEY_ID",      sys.env.getOrElse("AWS_ACCESS_KEY_ID",      throw new NullPointerException("AWS_ACCESS_KEY not defined")))
  val AWS_SECRET_KEY =  sys.props.getOrElse("AWS_SECRET_ACCESS_KEY",  sys.env.getOrElse("AWS_SECRET_ACCESS_KEY",  throw new NullPointerException("AWS_SECRET_ACCESS_KEY not defined")))
  val AWS_SNS_TOPIC =   sys.props.getOrElse("AWS_EMAIL_SNS_TOPIC",    sys.env.getOrElse("AWS_EMAIL_SNS_TOPIC",    throw new NullPointerException("AWS_EMAIL_SNS_TOPIC not defined")))
  val APP_TYPE =        sys.props.getOrElse("APP_TYPE",               sys.env.getOrElse("APP_TYPE",               throw new NullPointerException("APP_TYPE not defined")))
  val JWTSECRET =       sys.props.getOrElse("JWTSECRET",              sys.env.getOrElse("JWTSECRET",              throw new NullPointerException("JWTSECRET not defined")))
}
