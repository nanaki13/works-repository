package bon.jo.juliasite.pers

trait DB extends WithProfile {

  val db = {
    val api = profile.api
    import api._
    Database.forConfig(profileName)
  }
}
