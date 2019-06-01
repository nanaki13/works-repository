package bon.jo.helloworld.juliasite.pers

import bon.jo.helloworld.juliasite.model.Schema
import slick.dbio.{DBIOAction, NoStream}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait RepositoryContext extends WithProfile with DB with Schema{
  implicit def ctx = scala.concurrent.ExecutionContext.Implicits.global

  def runAndWait[R](value:DBIOAction[R, NoStream, Nothing]): Unit ={
      Await.result(db.run(value),Duration.Inf)
  }
}
