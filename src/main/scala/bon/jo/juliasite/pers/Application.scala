package bon.jo.juliasite.pers

import slick.jdbc.{H2Profile, JdbcProfile, PostgresProfile}

import scala.concurrent.Await
import scala.concurrent.duration.Duration




class RepositoryImpl(override val profile: JdbcProfile)  extends RepositoryContext with SiteRepository {
  override def profileName: String = "h2"
  val _import = profile.api
}


object H2Repo extends RepositoryImpl(H2Profile)


object PostgresRepo extends RepositoryImpl(PostgresProfile){
  override def profileName: String = "postgres"
}

