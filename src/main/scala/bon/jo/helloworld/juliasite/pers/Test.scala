package bon.jo.helloworld.juliasite.pers

import java.io.{BufferedOutputStream, FileOutputStream}
import java.nio.file.{Files, Paths}
import java.util.concurrent.atomic.AtomicInteger

import slick.jdbc.{H2Profile, PostgresProfile}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.io.Source


object Test extends ApplicationPostgresProfile with App {

  import _import._

  //def allShema() = allTableAsSeq.map(_.schema).reduce((a,b)=> a ++ b)
  val ouvr = List((0, "La bell  ou", 1933), (0, "La bell  ou", 1955), (3, "La bell  ou", 1985))
  val theme = List((1, "le thzmz"), (1, "le thzmzqsqs"))




  val drop = allShema.drop
  val truncate = allShema.truncate
 val dropAll = allTableAsSeq.reverse.map( t => t.schema).reduce((s1,s2) => s1 ++ s2)
  Await.result(db.run( dropAll.drop), Duration.Inf)
//  createMissing()
//  val r =  Array[Byte](1)
//
//  r(0)= 1
//  r.foreach(println(_))
 val imgd = Files.readAllBytes(Paths.get("C:\\Users\\Jonathan\\Pictures\\carte_visite_julia\\fleur.jpg"));
//
//  println(imgd.size)
//  Await.result(db.run( images.schema.dropIfExists), Duration.Inf)
//  val a = new AtomicInteger(1)
  val img = (0,"Bien ou qoui",imgd)
  (images += img).statements.foreach(println(_))
  val f= db.run( images += img) flatMap  { e =>
    println("res insert image = "+e)
    db.run(  images.filter( e => e.name ==="Bien ou qoui" ).map(i => i.imgData).result)
  } map{
    e => {
       e.foreach(b => {
        // b.foreach(println(_))
         val w = new BufferedOutputStream(new FileOutputStream("youpi.jpg"))
         w.write(b)
         w.close()
       })
    }
  }
  Await.result(f, Duration.Inf)

  images.schema.createStatements.foreach(println(_))
  images.insertStatement.foreach(print(_))


  def oldTest = {


    val insert = (ouvres ++= ouvr) andThen (themes ++= theme)

    val select = ouvres.result
    createMissing()
    val f = db.run(insert andThen select) flatMap  { a => {
      println(a)
      for (e <- a) {
        if (e._1 < 3) {
          db.run(oeuvresThemes += (1, e._1))
        } else {
          db.run(oeuvresThemes += (2, e._1))
        }

      }
      println("o b ")

      db.run(ouevreByTheme(1).result)
    }
    } map (a => {a.foreach(println);a}) map { a => {
      a.map(x =>(x._2,x._3))
    } }map {println}
    Await.result(f, Duration.Inf)

    val aa = List(Array("aaa","aaa"),Array("aaa","aca","aaa"))

    def testFor( char: Char) = for{
      e <- aa
      ee <- e
      eee <- ee if(eee == 'c')
    }yield (ee)

    testFor('e').map(println(_))

    aa.flatMap((e)=> {
      //  val dup  = e.iterator.duplicate
      e.iterator ++ e.iterator
    } ).map(println(_))
  }

}
