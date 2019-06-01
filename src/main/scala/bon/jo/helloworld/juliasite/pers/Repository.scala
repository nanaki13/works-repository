package bon.jo.helloworld.juliasite.pers

import bon.jo.helloworld.juliasite.model.{Descri, Images, Oeuvres, SiteElement}
import bon.jo.helloworld.juliasite.pers.Util.$
import slick.dbio.Effect.Schema
import slick.jdbc.meta.MTable
import slick.lifted.AppliedCompiledFunction

import scala.concurrent.{Await, Awaitable}
import scala.concurrent.duration.Duration


trait Repository {
  this: RepositoryContext =>

  import profile.api._

  type image = bon.jo.helloworld.juliasite.model.Images

  def allShema: profile.DDL = allTableAsSeq.map(_.schema).reduce((a, b) => a ++ b)



  def __ouevreByTheme(tKey: Rep[Int]): Query[OeuvresTable, Oeuvres, Seq] = for {
    o <- ouvres
    ot <- oeuvresThemes if o.id === ot.idOeuvre && ot.idTheme === tKey
  } yield o

  def __imagesByOeuvres(imagesKey: Rep[Int]): Query[ImagesTable, image, Seq] = for {
    o <- images
    ot <- oeuvreImages if o.id === ot.idOeuvre && ot.idImage === imagesKey
  } yield o

  val _ouvresByTheme = Compiled(__ouevreByTheme _)
  val _imagesByOeuvres = Compiled(__ouevreByTheme _)


  def ouevreByTheme(i: Int): AppliedCompiledFunction[Int, Query[OeuvresTable, Oeuvres, Seq], Seq[Oeuvres]] = _ouvresByTheme(i)


  def imagesByOeuvres(i: Int): AppliedCompiledFunction[Int, Query[OeuvresTable, Oeuvres, Seq], Seq[Oeuvres]] = _imagesByOeuvres(i)


  def createMissing():Unit = {
    val existing = db.run(MTable.getTables)
    val f = existing.flatMap(v => {
      val names = v.map(mt => mt.name.name)
      val createIfNotExist = allTableAsSeq.filter(table =>
        !names.contains(table.baseTableRow.tableName)).map(_.schema.create)
      db.run(DBIO.sequence(createIfNotExist))
    })
    Await.result(f, Duration.Inf)
    ()
  }

  def dropAll() = {
    val f = allTableAsSeq.reverse.map(_.schema).map(e => {
      e.drop.statements.foreach(println); e
    })
    f.foreach(st => {
      try {

        Await.result(db.run(st.drop), Duration.Inf)
      } catch {
        case l: Any => println(st + " : " + l)
      }
    })

  }

  def getImagesMenuLnk():Seq[(Int,String)] = {
    Await.result(db.run((for {
      se <- siteElement if se.descriminator === Descri.IMAGE_MENU
      im <- images if im.id === se.imageKey
    } yield (im.id,im.contentType)).result), Duration.Inf)
  }

  def addImages(data : Array[Byte],contentType: String):Option[(Int,String)] = {
    $$[Option[(Int,String)]]((images += Images(0,contentType,data)).flatMap(
     _ =>  images.sortBy(_.id.desc).map(e => (e.id,e.contentType)).result.headOption
    ))
  }

  def addSiteElement(imageKey : Option[Int],desc : Int):Option[Int] = {
    val se = SiteElement(0,imageKey, desc)
    val add = (siteElement += se).flatMap(
      _ =>  siteElement.sortBy(_.id.desc).map(e => e.id).result.headOption
    )
    $$(add)
    imageKey
  }

  def addImagesMenu(data : Array[Byte],contentType: String):Option[(Int,String)] ={
    addImages(data,contentType) match {
      case  Some((id,ct)) =>   {
        addSiteElement(Some(id),Descri.IMAGE_MENU) match {
          case Some(id) =>  Some((id,ct))
          case _ => None
        }
      }
      case _ => None
    }

  }



  def $$[R](add: DBIOAction[R, NoStream, Nothing]):R = $(db.run(add))

}
object Util{
  def  $[T](aw : Awaitable[T]) = Await.result(aw,Duration.Inf)


}

