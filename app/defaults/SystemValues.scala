package defaults

/**
  * @author Romesh Selvan
  */
object SystemValues {
  val AWS_ACCESS_KEY : String = sys.props.getOrElse("AWS_ACCESS_KEY_ID", sys.env.getOrElse("AWS_ACCESS_KEY_ID", throw new NullPointerException("AWS_ACCESS_KEY not defined")))
  val AWS_SECRET_KEY: String = sys.props.getOrElse("AWS_SECRET_ACCESS_KEY", sys.env.getOrElse("AWS_SECRET_ACCESS_KEY", throw new NullPointerException("AWS_SECRET_ACCESS_KEY not defined")))
}
