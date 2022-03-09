package dao
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.MySQLProfile.api._
import scala.util.Try

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import org.slf4j.LoggerFactory
import slick.jdbc.JdbcProfile
import play.api.data.format.Formats._

import javax.xml.datatype.Duration


case class Applicant(appNumber: Int, name: String, university: String,
                     ranking : String, specialization: String,
                     cap: Double, cap_scale: Double,ielts_overall : Option[Double],
                     toefl_total : Option[Int], priority : Int)

case class Index(number:Int)


class ApplicantDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  class Applicants(tag: Tag) extends Table[Applicant](tag, "APPLICANTS") {

    def appNumber = column[Int]("APPNUMBER", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def university = column[String]("UNIVEERSITY")
    def ranking = column[String]("UNIVEERSITY_RANK")
    def specialization = column[String]("specialization")
    def cap = column[Double]("CAP")
    def cap_scale = column[Double]("CAP_SCALE")
    def ielts_overall = column[Option[Double]]("IELTS")
    def toefl_total = column[Option[Int]]("TOEFL")
    def priority = column[Int]("PRIORITY")
    def * = (appNumber, name, university, ranking, specialization, cap, cap_scale, ielts_overall, toefl_total, priority) <> (Applicant.tupled, Applicant.unapply _)
  }

  private val applicants = TableQuery[Applicants]

  /** Return Applicants */
  def all(): Future[Seq[Applicant]] = db.run {
    applicants.result
  }

  /** Retrieve an applicant from the id. */
  def findById(appNumber: Int): Future[Seq[Applicant]] = {
    dbConfig.db.run(applicants.filter(_.appNumber === appNumber).result)
  }

  /** Update an applicant.*/
  def update(appNumber: Int, priority:Int) = {
    val qlocate = applicants.filter(_.appNumber === appNumber).map(_.priority)
    qlocate.update(priority)
  }

  /** Return Sorted Applicants */
  def sortById(): Future[Seq[Applicant]] = db.run {
    applicants.sortBy(_.appNumber.desc).result
  }

  /** Return Sorted Applicants */
  def sortByIelts(): Future[Seq[Applicant]] = db.run {
    applicants.sortBy(_.ielts_overall.desc).result
  }

  /** Return Sorted Applicants */
  def sortByToefl(): Future[Seq[Applicant]] = db.run {
    applicants.sortBy(_.toefl_total.desc).result
  }

  /** Return Sorted Applicants */
  def sortByRank(): Future[Seq[Applicant]] = db.run {
    applicants.sortBy(_.ranking.desc).result
  }

  /** Return Sorted Applicants */
  def sortByPriority(): Future[Seq[Applicant]] = db.run {
    applicants.sortBy(_.priority.desc).result
  }

  /** Return Sorted Applicants By Academic Band*/
  def sortByCap(): Future[Seq[Applicant]] = db.run {
    applicants.sortBy(_.cap.desc).result
  }

  /** Return Sorted Applicants By Professional Index*/
  def sortBySep(): Future[Seq[Applicant]] = db.run {
    applicants.sortBy(_.specialization.desc).result
  }

  /** Return Sorted Applicants By DIY Band*/
  def sortBy(index:Int): Future[Seq[Applicant]] = db.run {
    applicants.sortBy(_.cap.desc).result
  }
}