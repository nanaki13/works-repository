package bon.jo.juliasite.pers

import slick.jdbc.{H2Profile, PostgresProfile}

import scala.concurrent.ExecutionContext

case class RepoFactory()(implicit val ctxp: ExecutionContext) {

  object H2Repo extends RepositoryImpl(H2Profile) {
    override implicit val ctx: ExecutionContext = ctxp
  }


  object PostgresRepo extends RepositoryImpl(PostgresProfile) {
    override def profileName: String = "postgres"

    override implicit val ctx: ExecutionContext = ctxp
  }

}
