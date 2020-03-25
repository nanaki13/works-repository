package bon.jo.juliasite.model

import java.time.LocalDate

import bon.jo.juliasite.model.Schema._
import bon.jo.juliasite.pers.WithProfile
import slick.lifted.ProvenShape
import slick.sql.SqlProfile.ColumnOption.SqlType






object Schema{
  type Themes = (Int, String,Option[Int],Int,Int,Boolean)
  type ThemesImages = (Int,Int )
  type OeuvresImages = (Int,Int )
  type OeuvresThemes = (Int, Int,Int,Int)
  type Images =(  Int, String , Array[Byte],   String,String)
  type ImagesWithoutData =(  Int, String ,  String)
  case class Oeuvre(id:Int, title:String, description:String, dimensionX:Float, dimensionY:Float, creation: Int)
  case class SiteElement( id:Int, imageKey: Option[Int],  descriminator : Int)
  object Descri{
    val IMAGE_MENU = 0
  }

}
trait Schema {
  this :   WithProfile =>

  import profile.api._

  class SiteElementTable(tag: Tag) extends Table[SiteElement](tag, "SiteElement") {
    // This is the primary key column:
    def id: Rep[Int] = column[Int]("id", O.PrimaryKey)

    def imageKey: Rep[Option[Int]] = column[Option[Int]]("imageKey")
    def descriminator: Rep[Int] = column[Int]("descriminator")

    def imageKeyFk = foreignKey("imageKeyFk", imageKey,images)(se => se.id.?)
    // Every table needs a * projection with the same type as the table's type parameter
    def * : ProvenShape[SiteElement] =
      (id, imageKey,descriminator) <> (SiteElement.tupled, SiteElement.unapply)
  }

  lazy val siteElement = TableQuery[SiteElementTable]

 // type Oeuvres = (Int, String,String,Float,Float,Int )

  class OeuvresTable(tag: Tag) extends Table[Oeuvre](tag, "Oeuvre") {
    // This is the primary key column:
    def id: Rep[Int] = column[Int]("id", O.PrimaryKey)

    def title: Rep[String] = column[String]("title")

    def description: Rep[String] = column[String]("description")

    def dimensionX: Rep[Float] = column[Float]("dimension_x")
    def dimensionY: Rep[Float] = column[Float]("dimension_y")

    def creation: Rep[Int] = column[Int]("date")



    // Every table needs a * projection with the same type as the table's type parameter
    def *  =
      (id, title,description,dimensionX,dimensionY, creation)  <> (Oeuvre.tupled, Oeuvre.unapply)
  }
  lazy val oeuvres = TableQuery[OeuvresTable]





  class ThemesTable(tag: Tag) extends Table[Themes](tag, "Theme") {
    // This is the primary key column:
    def id: Rep[Int] = column[Int]("id", O.PrimaryKey)

    def name: Rep[String] = column[String]("title")
    def idThemeParent: Rep[Option[Int]] = column[Option[Int]]("id_theme_parent")
    def x: Rep[Int] = column[Int]("x")

    def y: Rep[Int] = column[Int]("y")
    def final_theme: Rep[Boolean] = column[Boolean]("final")

    def fk_theme_parent = foreignKey("th_to_pth", idThemeParent,themes)(th => th.id.?)
    // Every table needs a * projection with the same type as the table's type parameter
    def * : ProvenShape[Themes] =
      (id, name,idThemeParent,x,y, final_theme)
  }

  lazy val themes = TableQuery[ThemesTable]



  class ThemesOeuvresTable(tag: Tag) extends Table[OeuvresThemes](tag, "ThemeOeuvres") {
    // This is the primary key column:
    def idOeuvre: Rep[Int] = column[Int]("id_oeuvre")

    def idTheme: Rep[Int] = column[Int]("id_theme")
    def xInTheme: Rep[Int] = column[Int]("x_in_theme")

    def yInTheme: Rep[Int] = column[Int]("y_in_theme")



    def pk = primaryKey("pkto", (idOeuvre, idTheme))

    def fkO = foreignKey("t_o_to_o", idOeuvre, oeuvres)(ou => ou.id)

    def fkt = foreignKey("t_o_to_th", idTheme, themes)(th => th.id)

    // Every table needs a * projection with the same type as the table's type parameter
    def * : ProvenShape[OeuvresThemes] =
      (idTheme , idOeuvre,xInTheme,yInTheme)
  }

  lazy val themesOeuvres = TableQuery[ThemesOeuvresTable]



  class  ImagesTable(tag: Tag) extends Table[Images](tag, "Image"){
    // This is the primary key column:
    def id: Rep[Int] = column[Int]("id", O.PrimaryKey)

    def contentType: Rep[String] = column[String]("contentType")

    def name: Rep[String] = column[String]("name")

    def base: Rep[String] = column[String]("base")

    def imgData: Rep[Array[Byte]] = column[Array[Byte]]("imgData")
    // Every table needs a * projection with the same type as the table's type parameter
    def * : ProvenShape[Images] =
      (id, contentType,imgData,name,base )
  }

  class ImagesTableReadWrite(tag: Tag) extends ImagesTable(tag =tag){

    override def imgData: Rep[Array[Byte]] = column[Array[Byte]]("imgData", SqlType("java.sql.Blob"))

  }

  lazy val images = TableQuery[ImagesTable]

  class OeuvreImagesTable(tag: Tag) extends Table[OeuvresImages](tag, "OeuvreImages") {
    // This is the primary key column:
    def idOeuvre: Rep[Int] = column[Int]("id_oeuvre")

    def idImage: Rep[Int] = column[Int]("id_image")

    def pk = primaryKey("pkoi", (idOeuvre, idImage))

    def fkO = foreignKey("fk_oi_to_o", idOeuvre, oeuvres)(ou => ou.id)

    def fki = foreignKey("fk_oi_to_i", idImage, images)(th => th.id)

    // Every table needs a * projection with the same type as the table's type parameter
    def * : ProvenShape[OeuvresImages] =
      (idOeuvre, idImage)
  }

  lazy val oeuvreImages = TableQuery[OeuvreImagesTable]



  class ThemesImagesTable(tag: Tag) extends Table[ThemesImages](tag, "ThemesImages") {
    // This is the primary key column:
    def idTheme: Rep[Int] = column[Int]("id_theme")

    def idImage: Rep[Int] = column[Int]("id_image")


    def pk = primaryKey("pkot", (idTheme, idImage))

    def fkt = foreignKey("fk_ti_to_t", idTheme, themes)(th => th.id)

    def fki = foreignKey("fk_ti_to_i", idImage, images)(i => i.id)

    // Every table needs a * projection with the same type as the table's type parameter
    def * : ProvenShape[ThemesImages] =
      (idTheme, idImage)
  }

  lazy val themeImages = TableQuery[ThemesImagesTable]



  def imageWithoutDataProjection(t : ImagesTable): (Rep[Int], Rep[String], Rep[String]) = (t.id,t.contentType,t.name)
   val allTableAsSeq = List(oeuvres,themes,images,themesOeuvres,oeuvreImages,siteElement,themeImages)

}