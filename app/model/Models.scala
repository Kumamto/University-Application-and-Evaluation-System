package model

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.SQLiteProfile.api._
import scala.util.Try

case class Applicant(appNumber: String, name: String, university: String,
                     ranking : String, specialization: String,
                     cap: Double, cap_scale: Double,ielts_overall : Option[Double],
                     toefl_total : Option[Int], priority: Option[Int])



