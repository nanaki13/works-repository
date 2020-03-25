//package bon.jo.juliasite.pers
//
//import java.nio.file.{Files, Paths}
//
//
//
//import scala.concurrent.Await
//import scala.concurrent.duration.Duration
//
//import bon.jo.juliasite.pers.H2Repo._
//import bon.jo.juliasite.pers.H2Repo.profile.api._
//object Test  extends App {
//
//
//  //def allShema() = allTableAsSeq.map(_.schema).reduce((a,b)=> a ++ b)
//  val ouvr = List()
//  val theme = List((1, "le thzmz",None), (1, "le thzmzqsqs",Some(1)))
//
//
//
//
//  val drop = allShema.drop
//  val truncate = allShema.truncate
//   dropAll()
//  createMissing()
//
////  createMissing()
//
//
//  def testImageWriteAndRead = {
//    val r =  Array[Byte](1)
//    //
//    //  r(0)= 1
//    //  r.foreach(println(_))
//    val imgd = Files.readAllBytes(Paths.get("C:\\Users\\Jonathan\\Pictures\\carte_visite_julia\\fleur.jpg"));
//    //
//    println(imgd.size)
//    //  Await.result(db.run( images.schema.dropIfExists), Duration.Inf)
//    //  val a = new AtomicInteger(1)
//    val img = (0,"Bien ou qoui",imgd,"test")
//    (images += img).statements.foreach(println(_))
//    val f= db.run( images += img) flatMap  { e =>
//      println("res insert image = "+e)
//      db.run(  images.filter( e => e.contentType ==="Bien ou qoui" ).map(i => i.imgData).result)
//    } map{
//      e => {
//        e.foreach(b => {
//          println(b.size)
//          //         val w = new BufferedOutputStream(new FileOutputStream("youpi.jpg"))
//          //         w.write(b)
//          //         w.close()
//        })
//      }
//    }
//    Await.result(f, Duration.Inf)
//
//    images.schema.createStatements.foreach(println(_))
//    images.insertStatement.foreach(print(_))
//  }
//
//
//
//  def oldTest = {
//
////
////    val insert = (ouvres ++= ouvr) andThen (themes ++= theme)
////
////    val select = ouvres.result
////    createMissing()
////    val f = db.run(insert andThen select) flatMap  { a => {
////      println(a)
////      for (e <- a) {
////        if (e.id < 3) {
////          db.run(oeuvresThemes += (1, e.id))
////        } else {
////          db.run(oeuvresThemes += (2, e.id))
////        }
////
////      }
////      println("o b ")
////
////      db.run(ouevreByTheme(1).result)
////    }
////    } map (a => {a.foreach(println);a}) map { a => {
////      a.map(x =>(x.id,x.id))
////    } }map {println}
////    Await.result(f, Duration.Inf)
////
////    val aa = List(Array("aaa","aaa"),Array("aaa","aca","aaa"))
////
////    def testFor( char: Char) = for{
////      e <- aa
////      ee <- e
////      eee <- ee if(eee == 'c')
////    }yield (ee)
////
////    testFor('e').map(println(_))
////
////    aa.flatMap((e)=> {
////      //  val dup  = e.iterator.duplicate
////      e.iterator ++ e.iterator
////    } ).map(println(_))
//  }
//
//}
