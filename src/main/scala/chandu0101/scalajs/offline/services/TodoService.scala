package chandu0101.scalajs.offline.services

import chandu0101.scalajs.facades.pouchdb.{AllDocsOptions, PouchDB}
import chandu0101.scalajs.offline.model.TodoItem

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

/**
 * Created by chandrasekharkode on 2/26/15.
 */
object TodoService extends BaseService[TodoItem] {

  val DB_NAME = "scalajs-todos"
  val store: PouchDB = PouchDB.create(DB_NAME)
  override val sync = false
  override val remoteStore = s"http://localhost:5984/$DB_NAME"

  def save(item: TodoItem) = store.put(item.toJson)

  def remove(item: TodoItem) = store.remove(item.toJson)

  def getAll: Future[List[TodoItem]] = {
    store.allDocs(AllDocsOptions(include_docs = true,descending = true)).map(resp => resp.rows.asInstanceOf[js.Array[js.Dynamic]]
        .map(item => TodoItem.fromJson(item.doc)
      ).toList
    )
  }

}
