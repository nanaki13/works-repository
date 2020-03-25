package bon.jo.juliasite.pers

import slick.jdbc.{H2Profile, JdbcProfile, PostgresProfile}

import scala.concurrent.ExecutionContext


abstract class RepositoryImpl(override val profile: JdbcProfile) extends RepositoryContext with SiteRepository {
  override def profileName: String = "h2"

  val _import = profile.api
}



