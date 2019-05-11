package bon.jo.helloworld.juliasite.pers

import slick.dbio.Effect.Schema
import slick.jdbc.meta.MTable

import scala.concurrent.Await
import scala.concurrent.duration.Duration


trait Repository {
  this: RepositoryContext =>

  import profile.api._

  def allShema = allTableAsSeq.map(_.schema).reduce((a, b) => a ++ b)


  def __ouevreByTheme(tKey: Rep[Int]) = for {
    o <- ouvres
    ot <- oeuvresThemes if o.id === ot.idOeuvre && ot.idTheme === tKey
  } yield o.*

  def __imagesByOeuvres(imagesKey: Rep[Int]) = for {
    o <- images
    ot <- oeuvreImages if o.id === ot.idOeuvre && ot.idImage === imagesKey
  } yield o.*

  val _ouvresByTheme = Compiled(__ouevreByTheme _)
  val _imagesByOeuvres = Compiled(__ouevreByTheme _)


  def ouevreByTheme(i: Int) = _ouvresByTheme(i)


  def imagesByOeuvres(i: Int) = _imagesByOeuvres(i)


  def createMissing() = {
    val existing = db.run(MTable.getTables)
    val f = existing.flatMap(v => {
      val names = v.map(mt => mt.name.name)
      val createIfNotExist = allTableAsSeq.filter(table =>
        (!names.contains(table.baseTableRow.tableName))).map(_.schema.create)
      db.run(DBIO.sequence(createIfNotExist))
    })
    Await.result(f, Duration.Inf)
    ()
  }
}
