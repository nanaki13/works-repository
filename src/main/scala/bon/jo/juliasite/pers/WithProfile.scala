package bon.jo.juliasite.pers

import slick.jdbc.JdbcProfile

trait WithProfile {
  val profile: JdbcProfile
  def profileName : String
}
