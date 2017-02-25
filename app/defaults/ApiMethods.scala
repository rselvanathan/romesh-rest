package defaults

/**
  * @author Romesh Selvan
  */
object ApiMethods {

  trait ApiMethod
  // Family APIs
  object SAVE_FAMILY extends ApiMethod
  object GET_FAMILY extends ApiMethod
  // User APIs
  object SAVE_USER extends ApiMethod
  object AUTH_USER extends ApiMethod
  // Project APIs
  object GET_PROJECT extends ApiMethod
  object GET_ALL_PROJECTS extends ApiMethod
  object SAVE_PROJECT extends ApiMethod
}
