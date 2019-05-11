package bon.jo.helloworld.juliasite.pers

import bon.jo.helloworld.juliasite.model.Schema

trait RepositoryContext extends WithProfile with DB with Schema{
  implicit def ctx = scala.concurrent.ExecutionContext.Implicits.global
}
