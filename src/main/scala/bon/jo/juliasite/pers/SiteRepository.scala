package bon.jo.juliasite.pers

import bon.jo.juliasite.model.Schema
import bon.jo.juliasite.model.Schema.{Descri, SiteElement}
import slick.jdbc.meta.MTable
import slick.lifted.AppliedCompiledFunction

import scala.concurrent.{ExecutionContext, Future}


trait SiteRepository {
  this: RepositoryContext =>

  import profile.api._

  implicit   val ctx: ExecutionContext


  def allShema: profile.DDL = allTableAsSeq.map(_.schema).reduce((a, b) => a ++ b)



  def createMissing(): Future[List[Unit]] = {
    val existing = db.run(MTable.getTables)
    existing.flatMap(v => {
      val names = v.map(mt => mt.name.name)
      val createIfNotExist = allTableAsSeq.filter(table =>

        !names.contains(table.baseTableRow.tableName)).map(e => {
        println(e.baseTableRow.tableName); e
      }).map(_.schema.create)

      try {
        db.run(DBIO.sequence(createIfNotExist))
      } catch {
        case l: Exception => Future.failed(l)
      }
    })

  }

  def dropAll(): Seq[ DBIOAction[Unit, NoStream, _]] = {
    val f = allTableAsSeq.reverse.map(_.schema).map(e => {
      e.drop.statements.foreach(println);
      e
    })

    f.map(_.drop)

  }

  def deleteImage(id : Int): Future[Boolean] = {
    val deleteAction =  images.filter(_.id === id).delete
    db.run(deleteAction).map(e => e ==1)
  }




  def addImages(data: Array[Byte], contentType: String,name : String,base:String): Future[Option[(Int, String)]] = {
    db.run((images += (0, contentType, data, name,base)).flatMap {
      _ => images.sortBy(_.id.desc).map(e => (e.id, e.contentType)).result.headOption
    })

  }



  object Initilaizer {
    def createDropCreate(): Future[List[Unit]] = {
      createMissing()
    }
  }

}



