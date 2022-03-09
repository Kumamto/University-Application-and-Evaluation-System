package controllers

import dao.{Applicant, ApplicantDAO}
import dao.Index
import play.api.data.Forms._
import play.api.data._
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

import javax.inject._
import scala.concurrent.ExecutionContext
import play.api.data.format.Formats.doubleFormat

class ApplicationController @Inject()(
                                       applicantDao: ApplicantDAO,
                                       controllerComponents: ControllerComponents)(implicit executionContext: ExecutionContext) extends AbstractController(controllerComponents) with I18nSupport {

  /** Redirect to the evaluation home */
  val Home = Redirect(routes.HomeController.home())

  /** Describe the application form. */
  val applicantForm = Form(
    mapping(
      "appNumber" -> number,
      "name" -> nonEmptyText,
      "university" -> text,
      "ranking" -> text,
      "specialization" -> text,
      "cap" -> of(doubleFormat),
      "cap_scale" -> of(doubleFormat),
      "ielts_overall" -> optional(of(doubleFormat)),
      "toefl_total" -> optional(number),
      "priority" -> number
    )(Applicant.apply)(Applicant.unapply)
  )

  /** Describe the banding/priority index form. */
  val indexForm = Form(
    mapping(
      "number" -> number
    )
    (Index.apply)(Index.unapply)
  )

  // - Actions

  def index = Action {
    Home
  }

  /**
   * Direction to Admission Judgement Page.
   */
  def Judgement() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.bandhelper(indexForm))
  }


  /**
   * Display the list of applicants.
   */
  def all() = Action.async { implicit request =>
    applicantDao.all().map {
      case applicants => Ok(views.html.list(applicantForm, applicants))
    }
  }

  /**
   * Direct To Applicant's Detail Page
   * @param appNumber ApplicationNumber of the applicant
   * */
  def detail(appNumber: Int) = Action.async { implicit request: Request[AnyContent] =>
    val resultingApplicants = applicantDao.findById(appNumber)
    resultingApplicants.map(applicants => Ok(views.html.result(applicants)))

  }

  /**
   * Display the priority set page of an applicant.
   *
   * @param appNumber ApplicationNumber of the applicant
   */

  def edit(appNumber: Int) = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.priorityhelper(appNumber, indexForm))
  }

  /**
   * Handle the priority set submission
   *
   * @param id Id of the applicant
   */
  def update(appNumber:Int) = Action.async { implicit request: Request[AnyContent] =>
    val updateresult = indexForm.bindFromRequest().get
    applicantDao.update(appNumber, updateresult.number)
    applicantDao.all().map {
      case applicants => Ok(views.html.list(applicantForm, applicants))
    }
  }

  /**
   * Direct To Sorted Applicants By Ielts Score Page
   * */
  def result1() = Action.async { implicit request: Request[AnyContent] =>
    applicantDao.sortById().map {
      case applicants => Ok(views.html.list(applicantForm, applicants))
    }
  }

  /**
   * Direct To Sorted Applicants By Toefl Score Page
   * */
  def result2() = Action.async { implicit request: Request[AnyContent] =>
    applicantDao.sortByIelts().map {
      case applicants => Ok(views.html.list(applicantForm, applicants))
    }
  }

  /**
   * Direct To Sorted Applicants By Academic Band Page
   * */
  def result3() = Action.async { implicit request: Request[AnyContent] =>
    applicantDao.sortByToefl().map {
      case applicants => Ok(views.html.list(applicantForm, applicants))
    }
  }

  /**
   * Direct To Sorted Applicants By University Ranking Page
   * */
  def result4() = Action.async { implicit request: Request[AnyContent] =>
    applicantDao.sortByRank().map {
      case applicants => Ok(views.html.list(applicantForm, applicants))
    }
  }

  /**
   * Direct To Sorted Applicants By Process Priority Page
   * */
  def result5() = Action.async { implicit request: Request[AnyContent] =>
    applicantDao.sortByPriority().map {
      case applicants => Ok(views.html.list(applicantForm, applicants))
    }
  }

  /**
   * Direct To Sorted Applicants By Academic Band Page
   * */
  def judge1() = Action.async { implicit request: Request[AnyContent] =>
    applicantDao.sortByCap().map {
      case applicants => Ok(views.html.list(applicantForm, applicants))
    }
  }

  /**
   * Direct To Sorted Applicants By Professional Band Page
   * */
  def judge2() = Action.async { implicit request: Request[AnyContent] =>
    applicantDao.sortBySep().map {
      case applicants => Ok(views.html.list(applicantForm, applicants))
    }
  }

  /**
   * Direct To Sorted Applicants By DIY Band Page
   * */

  def diyJudge() = Action.async { implicit request: Request[AnyContent] =>
    val index = indexForm.bindFromRequest().get
    applicantDao.sortBy(index.number).map {
      case applicants => Ok(views.html.list(applicantForm, applicants))
    }
  }
}
