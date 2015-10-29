package chandu0101.scalajs.offline

import chandu0101.scalajs.offline.routes.AppRouter
import japgolly.scalajs.react.React
import org.scalajs.dom
import org.scalajs.dom.raw.ServiceWorkerRegistration

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g, literal => json}
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.{Date, JSApp, JSON}

/**
 * Created by chandrasekharkode on 2/25/15.
 */
object ReactApp extends JSApp {
  @JSExport
  override def main(): Unit = {
    React.render(AppRouter.C(), dom.document.getElementById("container"))
    if (!js.isUndefined(dom.navigator.serviceWorker)) {
      // check if serviceWorker supported or not
      dom.navigator.serviceWorker.register("/scalajs-offline/offline.js").andThen((resp: ServiceWorkerRegistration) => {
        println(s" ServiceWorker registered ${new Date()} successfully : ${JSON.stringify(resp)}  ")
      }
      ).recover((err: Any) => println(s"service worker failed ${err}"))
    } else {
      println("ServiceWorker not there yet!")

    }
  }

}


