package chandu0101.scalajs.offline.routes

import chandu0101.scalajs.offline.components.TodoApp
import chandu0101.scalajs.offline.pages.HomePage
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.{AbsUrl, BaseUrl, Redirect, RoutingRules}
import japgolly.scalajs.react.vdom.all._



/**
 * Created by chandrasekharkode on 2/25/15.
 */
object AppRouter {


  object AppPage extends RoutingRules {

    val root = register(rootLocation(HomePage()))

    register(removeTrailingSlashes)

    override protected val notFound = redirect(root, Redirect.Replace)


    override protected def interceptRender(i: InterceptionR): ReactElement =
        i.element

  }

  val baseUrl = BaseUrl.fromWindowOrigin / "scalajs-offline/"
//  val baseUrl = BaseUrl("https://chandu0101.github.io/scalajs-offline/")

  val C = AppPage.router(baseUrl)
}
