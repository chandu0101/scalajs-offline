package chandu0101.scalajs.offline.components


import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.util.{Failure, Success}

/**
 * Created by chandrasekharkode on 3/10/15.
 */
object ScalaJSRepos {

  case class State(repos: js.Array[js.Dynamic] = js.Array() ,err : String = "")

  class Backend(t: BackendScope[_, State]) {
    def getRepos(headers : Map[String,String] = Map.empty) = {
      Ajax.get("https://api.github.com/search/repositories?q=scalajs&sort=stars&order=desc",headers = headers)
        .onComplete {
        case Success(xhr) => {
          val result = JSON.parse(xhr.responseText)
          if(js.isUndefined(result) || js.isUndefined(result.total_count)) {
            if(headers.isEmpty && t.state.repos.isEmpty) t.modState(_.copy(err = "Network error!"))
          } else {
            val repos = result.items.asInstanceOf[js.Array[js.Dynamic]]
            if(repos.length != t.state.repos.length) t.modState(_.copy(repos = repos))
          }
        }
        case Failure(ex) => {
          println(s"failed getting repos ${ex.getMessage} and headers $headers")
          if(headers.isEmpty && t.state.repos.isEmpty) t.modState(_.copy(err = "Network error!"))
        }
      }

    }
  }

  val component = ReactComponentB[Unit]("ScalaJSRepos")
    .initialState(State())
    .backend(new Backend(_))
    .render((P, S, B) => {
      div( cls := "repos",
       if(S.err.nonEmpty) strong(S.err)
       else ul(
       S.repos.map(repo => li(key := repo.full_name.toString, a(target := "_blank", href := repo.html_url.toString , repo.full_name.toString),br(),span(repo.description.toString.concat(s"  Stars : ${repo.stargazers_count.toString}  "))))
       )
      
      )
  })
    .componentWillMount(scope => {
     if(!js.isUndefined(dom.navigator.serviceWorker) && dom.navigator.serviceWorker.controller != null) { // means service worker controlling this page
       scope.backend.getRepos(Map("Accept" -> "x-cache/only"))
     }
  })
    .componentDidMount(scope => { 
     scope.backend.getRepos()
  })
    .buildU

  def apply() = component()


}
