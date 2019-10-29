package bon.jo.helloworld.juliasite.pers

import bon.jo.helloworld.juliasite.model.{Descri, Images, Oeuvres, SiteElement}
import slick.jdbc.meta.MTable
import slick.lifted.AppliedCompiledFunction

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


trait SiteRepository {
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


  def createMissing(): Future[List[Unit]] = {
    val existing = db.run(MTable.getTables)
    existing.flatMap(v => {
      val names = v.map(mt => mt.name.name)
      val createIfNotExist = allTableAsSeq.filter(table =>

        !names.contains(table.baseTableRow.tableName)).map(e=>{println(e.baseTableRow.tableName);e}).map(_.schema.create)

      try {
        db.run(DBIO.sequence(createIfNotExist))
      } catch {
        case l: Exception => Future.failed(l)
      }
    })

  }

  def dropAll(): Future[List[Unit]] = {
    val f = allTableAsSeq.reverse.map(_.schema).map(e => {
      e.drop.statements.foreach(println);
      e
    })
    Future.sequence(f.map(st => {
      try {
        db.run(st.drop)
      } catch {
        case l: Exception => Future.failed(l)
      }
    }))

  }

  def imagesMenuLnk(): Future[Seq[(Int, String)]] = {
    db.run((for {
      se <- siteElement if se.descriminator === Descri.IMAGE_MENU
      im <- images if im.id === se.imageKey
    } yield (im.id, im.contentType)).result)
  }

  def addImages(data: Array[Byte], contentType: String): Future[Option[(Int, String)]] = {
    db.run((images += Images(0, contentType, data)).flatMap {
      _ => images.sortBy(_.id.desc).map(e => (e.id, e.contentType)).result.headOption
    })

  }

  def addSiteElement(imageKey: Option[Int], desc: Int): Future[Option[Int]] = {
    val se = SiteElement(0, imageKey, desc)
    val add = (siteElement += se).flatMap(
      _ => siteElement.sortBy(_.id.desc).map(e => e.id).result.headOption
    )
    db.run(add.map {
      case Some(1) => imageKey
      case _ => None
    })
  }

  def addImagesMenu(data: Array[Byte], contentType: String): Future[Option[(Int, String)]] = {
    addImages(data, contentType) flatMap {
      case Some((id, ct)) => {
        addSiteElement(Some(id), Descri.IMAGE_MENU) map {
          case Some(id) => Some((id, ct))
          case _ => None
        }
      }
      case _ => Future.successful(None)
    }

  }

  object Initilaizer {
    def createDropCreate(): Future[List[Unit]] = {
      createMissing().flatMap { _ =>
        dropAll().flatMap { _ =>
          createMissing()
        }
      }
    }
  }

}


