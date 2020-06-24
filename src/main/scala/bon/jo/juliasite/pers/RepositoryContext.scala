package bon.jo.juliasite.pers

import bon.jo.juliasite.model.Schema
import slick.dbio.Effect.Write
import slick.dbio.{DBIOAction, NoStream}
import slick.lifted.Query

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait RepositoryContext extends WithProfile with DB with Schema {


  def runAndWait[R](value: DBIOAction[R, NoStream, Nothing]): Unit = {
    Await.result(db.run(value), Duration.Inf)
  }

  def run[R](a: DBIOAction[R, NoStream, Nothing]): Future[R] = db.run(a)

  type UBase = profile.api.DBIOAction[Boolean, NoStream, Write]
  type QueryBaseType = Query[_, (Schema.Oeuvre, Option[(Int, Int)], (Int, Int, Int, Int)), Seq]
}
