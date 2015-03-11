package chandu0101.scalajs.offline.services

import chandu0101.scalajs.facades.pouchdb.PouchDB

import scala.concurrent.Future

/**
 * Created by chandrasekharkode on 2/26/15.
 */
trait BaseService[T] {

  val store : PouchDB
  
  val sync = false
  
  val retrySync = false
  
  val remoteStore = ""

  def save(item : T)

  def remove(item : T)

  def getAll : Future[List[T]]
}
