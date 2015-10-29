package chandu0101.scalajs.offline.util

import chandu0101.pouchdb.{ChangesEventEmitter, ChangesOptions, ReplicateOptions}
import chandu0101.scalajs.offline.services.BaseService
import japgolly.scalajs.react.extra.OnUnmount
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.JSON

/**
 * Created by chandrasekharkode on 2/26/15.
 */
abstract class PouchDBChangesListener[T](service: BaseService[T]) extends OnUnmount {

  var changes: ChangesEventEmitter = null

  var synChanges: ChangesEventEmitter = null

  /**
   * implement this method to update react component
   * @param data
   */
  def updateNewData(data: List[T])

  def startListening = {
    changes = service.store.changes(ChangesOptions.since("now").live(true).result).onChange((resp: js.Dynamic) => {
      getNewData
    }).onError((err: js.Dynamic) => println(s"Error occurred while performing db operations on ${service.store.name} : ${JSON.stringify(err)}"))
    getNewData // call explicitly for the first time
    if (service.retrySync) retrySync
    else if (service.sync) sync
  }


  def sync = {
    synChanges = service.store.sync(service.remoteStore, ReplicateOptions.live(true).result)
      .onError((err: js.Dynamic) => // oops network error
      println(s"Error occurred while syncing db's  : $err}"))
  }

  /**
   * Useful when user have on/off internet connectivity (example : mobile internet)
   */
  def retrySync: Unit = {
    var timeout = 10000 // 10secs
    var increment = 2
    synChanges = service.store.sync(service.remoteStore, ReplicateOptions.live(true).result)
      .onChange((resp: js.Dynamic) => timeout = 10000) // reset retry timer when user came back on
      .onError((err: js.Dynamic) =>
      dom.setTimeout(() => {
        timeout *= increment
        retrySync
      }, timeout)
      )
  }

  def getNewData = {
    service.getAll.onSuccess {
      case (data: List[T]) => {
        updateNewData(data)
      }
    }
  }


  /**
   * clean up
   */
  onUnmount(() => {
    if (changes != null) changes.cancel()
    if (synChanges != null) synChanges.cancel()
  })

}
