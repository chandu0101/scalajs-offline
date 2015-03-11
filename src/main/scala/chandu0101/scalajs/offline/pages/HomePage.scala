package chandu0101.scalajs.offline.pages

import chandu0101.scalajs.offline.components.{ScalaJSRepos, TodoApp}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._


/**
 * Created by chandrasekharkode on 2/25/15.
 */
object HomePage {

  val component = ReactComponentB[Unit]("HomePage")
    .render(P => {
    div(cls := "homePage")(
      div(cls := "todoWrapper",
        h3("Todo App"),
        TodoApp()),
      div(cls := "reposWrapper", 
        h3("ScalaJS Top Repos!"),
        ScalaJSRepos())
    )
  }).buildU

  def apply() = component()

}
