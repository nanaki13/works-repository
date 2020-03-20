package bon.jo.juliasite.pers

import bon.jo.juliasite.model.Schema
import slick.dbio.Effect.Write
import slick.dbio.{DBIOAction, NoStream}
import slick.lifted.Query

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

trait RepositoryContext extends WithProfile with DB with Schema {
  implicit def ctx = scala.concurrent.ExecutionContext.Implicits.global

  def runAndWait[R](value: DBIOAction[R, NoStream, Nothing]): Unit = {
    Await.result(db.run(value), Duration.Inf)
  }

  def run[R](a: DBIOAction[R, NoStream, Nothing]): Future[R] = db.run(a)

  type UBase = profile.api.DBIOAction[Boolean, NoStream, Write]
  type QueryBaseType = Query[(OeuvresTable, OeuvreImagesTable, ThemesOeuvresTable), (Schema.Oeuvre, (Int, Int), (Int, Int, Int, Int)), Seq]
}
