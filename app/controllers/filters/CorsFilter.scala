package controllers.filters

import com.google.inject.Inject
import play.api.http.DefaultHttpFilters
import play.filters.cors.CORSFilter

/**
  * @author Romesh Selvan
  */
class CorsFilter @Inject() (corsFilter: CORSFilter) extends DefaultHttpFilters(corsFilter)
