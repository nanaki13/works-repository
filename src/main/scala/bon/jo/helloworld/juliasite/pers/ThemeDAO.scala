//package bon.jo.helloworld.juliasite.pers
//
//import slick.dbio.{Effect, NoStream}
//import slick.jdbc.{H2Profile, JdbcProfile, PostgresProfile}
//
//trait Application extends RepositoryContext with Repository {
//
//}
//
//class ApplicationFrom(override val profile: JdbcProfile) extends Application {
//
//  override def profileName: String = "h2"
//  val _import = profile.api
//}
//
//object ApplicationInMemmoryH2 extends ApplicationFrom(H2Profile)
//
//class ApplicationPostgresProfile() extends ApplicationFrom(PostgresProfile){
//
//
//  override def profileName: String = "postgres"
//}