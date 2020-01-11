package bon.jo.juliasite.model

import bon.jo.juliasite.pers.WithProfile
import slick.lifted.ProvenShape
import slick.sql.SqlProfile.ColumnOption.SqlType




case class Images( id : Int, contentType:String , imgData:Array[Byte])
case class Oeuvre(id:Int, title:String, description:String, dimensionX:Float, dimensionY:Float, creation: Int)
case class SiteElement( id:Int, imageKey: Option[Int],  descriminator : Int)
object Descri{
  val IMAGE_MENU = 0
}
trait Schema {
  this :   WithProfile =>

  import profile.api._

  class SiteElementTable(tag: Tag) extends Table[SiteElement](tag, "SiteElement") {
    // This is the primary key column:
    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

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
    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def title: Rep[String] = column[String]("title")

    def description: Rep[String] = column[String]("description")

    def dimensionX: Rep[Float] = column[Float]("dimension_x")
    def dimensionY: Rep[Float] = column[Float]("dimension_y")

    def creation: Rep[Int] = column[Int]("date")



    // Every table needs a * projection with the same type as the table's type parameter
    def *  =
      (id, title,description,dimensionX,dimensionY, creation)  <> (Oeuvre.tupled, Oeuvre.unapply)
  }
  lazy val ouvres = TableQuery[OeuvresTable]


  type Themes = (Int, String,Option[Int],Int,Int)


  class ThemesTable(tag: Tag) extends Table[Themes](tag, "Theme") {
    // This is the primary key column:
    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name: Rep[String] = column[String]("title")
    def idThemeParent: Rep[Option[Int]] = column[Option[Int]]("id_theme_parent")
    def x: Rep[Int] = column[Int]("x")

    def y: Rep[Int] = column[Int]("y")

    def fk_theme_parent = foreignKey("aaaaa", idThemeParent,themes)(th => th.id.?)
    // Every table needs a * projection with the same type as the table's type parameter
    def * : ProvenShape[Themes] =
      (id, name,idThemeParent,x,y)
  }

  lazy val themes = TableQuery[ThemesTable]

  type OeuvresThemes = (Int, Int,Int,Int)

  class ThemesOeuvresTable(tag: Tag) extends Table[OeuvresThemes](tag, "ThemeOeuvres") {
    // This is the primary key column:
    def idOeuvre: Rep[Int] = column[Int]("id_oeuvre")

    def idTheme: Rep[Int] = column[Int]("id_theme")
    def xInTheme: Rep[Int] = column[Int]("x_in_theme")

    def yInTheme: Rep[Int] = column[Int]("y_in_theme")

    def pk = primaryKey("pkto", (idOeuvre, idTheme))

    def fkO = foreignKey("t_o_to_o", idOeuvre, ouvres)(ou => ou.id)

    def fkt = foreignKey("t_o_to_th", idTheme, themes)(th => th.id)

    // Every table needs a * projection with the same type as the table's type parameter
    def * : ProvenShape[OeuvresThemes] =
      (idTheme , idOeuvre,xInTheme,yInTheme)
  }

  lazy val oeuvresThemes = TableQuery[ThemesOeuvresTable]



  class  ImagesTable(tag: Tag) extends Table[Images](tag, "Image"){
    // This is the primary key column:
    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def contentType: Rep[String] = column[String]("contentType")

    def imgData: Rep[Array[Byte]] = column[Array[Byte]]("imgData")
    // Every table needs a * projection with the same type as the table's type parameter
    def * : ProvenShape[Images] =
      (id, contentType,imgData ) <> (Images.tupled, Images.unapply)
  }

  class ImagesTableReadWrite(tag: Tag) extends ImagesTable(tag =tag){

    override def imgData: Rep[Array[Byte]] = column[Array[Byte]]("imgData", SqlType("java.sql.Blob"))

  }

  lazy val images = TableQuery[ImagesTable]
  type OeuvresImages = (Int,Int )
  class OeuvreImagesTable(tag: Tag) extends Table[OeuvresImages](tag, "OeuvreImages") {
    // This is the primary key column:
    def idOeuvre: Rep[Int] = column[Int]("id_oeuvre")

    def idImage: Rep[Int] = column[Int]("id_image")

    def pk = primaryKey("pkoi", (idOeuvre, idImage))

    def fkO = foreignKey("fk_oi_to_o", idOeuvre, ouvres)(ou => ou.id)

    def fki = foreignKey("fk_oi_to_i", idImage, images)(th => th.id)

    // Every table needs a * projection with the same type as the table's type parameter
    def * : ProvenShape[OeuvresImages] =
      (idOeuvre, idImage)
  }

  lazy val oeuvreImages = TableQuery[OeuvreImagesTable]


  type ThemesImages = (Int,Int )
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



   val allTableAsSeq = List(ouvres,themes,images,oeuvresThemes,oeuvreImages,siteElement,themeImages)


}

//
//import scala.concurrent.{Future, Await}
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.duration.Duration
//import slick.basic.DatabasePublisher
//import slick.jdbc.H2Profile.api._
//
//// The main application
//object HelloSlick extends App {
//  val db = Database.forConfig("h2mem1")
//  try {
//
//    // The query interface for the Suppliers table
//    val suppliers: TableQuery[Suppliers] = TableQuery[Suppliers]
//
//    // the query interface for the Coffees table
//    val coffees: TableQuery[Coffees] = TableQuery[Coffees]
//
//    val setupAction: DBIO[Unit] = DBIO.seq(
//      // Create the schema by combining the DDLs for the Suppliers and Coffees
//      // tables using the query interfaces
//      (suppliers.schema ++ coffees.schema).create,
//
//      // Insert some suppliers
//      suppliers += (101, "Acme, Inc.", "99 Market Street", "Groundsville", "CA", "95199"),
//      suppliers += ( 49, "Superior Coffee", "1 Party Place", "Mendocino", "CA", "95460"),
//      suppliers += (150, "The High Ground", "100 Coffee Lane", "Meadows", "CA", "93966")
//    )
//
//    val setupFuture: Future[Unit] = db.run(setupAction)
//    val f = setupFuture.flatMap { _ =>
//
//      //#insertAction
//      // Insert some coffees (using JDBC's batch insert feature)
//      val insertAction: DBIO[Option[Int]] = coffees ++= Seq (
//        ("Colombian",         101, 7.99, 0, 0),
//        ("French_Roast",       49, 8.99, 0, 0),
//        ("Espresso",          150, 9.99, 0, 0),
//        ("Colombian_Decaf",   101, 8.99, 0, 0),
//        ("French_Roast_Decaf", 49, 9.99, 0, 0)
//      )
//
//      val insertAndPrintAction: DBIO[Unit] = insertAction.map { coffeesInsertResult =>
//        // Print the number of rows inserted
//        coffeesInsertResult foreach { numRows =>
//          println(s"Inserted $numRows rows into the Coffees table")
//        }
//      }
//      //#insertAction
//
//      val allSuppliersAction: DBIO[Seq[(Int, String, String, String, String, String)]] =
//        suppliers.result
//
//      val combinedAction: DBIO[Seq[(Int, String, String, String, String, String)]] =
//        insertAndPrintAction andThen allSuppliersAction
//
//      val combinedFuture: Future[Seq[(Int, String, String, String, String, String)]] =
//        db.run(combinedAction)
//
//      combinedFuture.map { allSuppliers =>
//        allSuppliers.foreach(println)
//      }
//
//    }.flatMap { _ =>
//
//      /* Streaming */
//
//      val coffeeNamesAction: StreamingDBIO[Seq[String], String] =
//        coffees.map(_.name).result
//
//      val coffeeNamesPublisher: DatabasePublisher[String] =
//        db.stream(coffeeNamesAction)
//
//      coffeeNamesPublisher.foreach(println)
//
//    }.flatMap { _ =>
//
//      /* Filtering / Where */
//
//      // Construct a query where the price of Coffees is > 9.0
//      val filterQuery: Query[Coffees, (String, Int, Double, Int, Int), Seq] =
//        coffees.filter(_.price > 9.0)
//
//      // Print the SQL for the filter query
//      println("Generated SQL for filter query:\n" + filterQuery.result.statements)
//
//      // Execute the query and print the Seq of results
//      db.run(filterQuery.result.map(println))
//
//    }.flatMap { _ =>
//
//      /* Update */
//
//      // Construct an update query with the sales column being the one to update
//      val updateQuery: Query[Rep[Int], Int, Seq] = coffees.map(_.sales)
//
//      val updateAction: DBIO[Int] = updateQuery.update(1)
//
//      // Print the SQL for the Coffees update query
//      println("Generated SQL for Coffees update:\n" + updateQuery.updateStatement)
//
//      // Perform the update
//      db.run(updateAction.map { numUpdatedRows =>
//        println(s"Updated $numUpdatedRows rows")
//      })
//
//    }.flatMap { _ =>
//
//      /* Delete */
//
//      // Construct a delete query that deletes coffees with a price less than 8.0
//      val deleteQuery: Query[Coffees,(String, Int, Double, Int, Int), Seq] =
//        coffees.filter(_.price < 8.0)
//
//      val deleteAction = deleteQuery.delete
//
//      // Print the SQL for the Coffees delete query
//      println("Generated SQL for Coffees delete:\n" + deleteAction.statements)
//
//      // Perform the delete
//      db.run(deleteAction).map { numDeletedRows =>
//        println(s"Deleted $numDeletedRows rows")
//      }
//
//    }.flatMap { _ =>
//
//      /* Sorting / Order By */
//
//      val sortByPriceQuery: Query[Coffees, (String, Int, Double, Int, Int), Seq] =
//        coffees.sortBy(_.price)
//
//      println("Generated SQL for query sorted by price:\n" +
//        sortByPriceQuery.result.statements)
//
//      // Execute the query
//      db.run(sortByPriceQuery.result).map(println)
//
//    }.flatMap { _ =>
//
//      /* Query Composition */
//
//      val composedQuery: Query[Rep[String], String, Seq] =
//        coffees.sortBy(_.name).take(3).filter(_.price > 9.0).map(_.name)
//
//      println("Generated SQL for composed query:\n" +
//        composedQuery.result.statements)
//
//      // Execute the composed query
//      db.run(composedQuery.result).map(println)
//
//    }.flatMap { _ =>
//
//      /* Joins */
//
//      // Join the tables using the relationship defined in the Coffees table
//      val joinQuery: Query[(Rep[String], Rep[String]), (String, String), Seq] = for {
//        c <- coffees if c.price > 9.0
//        s <- c.supplier
//      } yield (c.name, s.name)
//
//      println("Generated SQL for the join query:\n" + joinQuery.result.statements)
//
//      // Print the rows which contain the coffee name and the supplier name
//      db.run(joinQuery.result).map(println)
//
//    }.flatMap { _ =>
//
//      /* Computed Values */
//
//      //#maxPrice
//      // Create a new scalar value that calculates the maximum price
//      val maxPrice: Rep[Option[Double]] = coffees.map(_.price).max
//      //#maxPrice
//
//      println("Generated SQL for max price column:\n" + maxPrice.result.statements)
//
//      // Execute the computed value query
//      db.run(maxPrice.result).map(println)
//
//    }.flatMap { _ =>
//
//      //#plainSql
//      /* Manual SQL / String Interpolation */
//
//      // A value to insert into the statement
//      val state = "CA"
//
//      // Construct a SQL statement manually with an interpolated value
//      val plainQuery = sql"select SUP_NAME from SUPPLIERS where STATE = $state".as[String]
//      //#plainSql
//
//      println("Generated SQL for plain query:\n" + plainQuery.statements)
//
//      // Execute the query
//      db.run(plainQuery).map(println)
//
//    }
//    Await.result(f, Duration.Inf)
//
//  } finally db.close
//}