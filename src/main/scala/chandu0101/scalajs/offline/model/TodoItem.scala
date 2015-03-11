package chandu0101.scalajs.offline.model

import scala.scalajs.js
import scala.scalajs.js.Date
import scala.scalajs.js.Dynamic.{literal => json}

/**
 * Created by chandrasekharkode on 2/25/15.
 */
case class TodoItem(_id : String = new Date().toISOString(),_rev : String = "",title : String,completed : Boolean = false) {

  def toJson = json("_id" -> _id ,"_rev" -> _rev,"title" -> title ,"completed" -> completed)
}

object TodoItem {
  
  def fromJson(obj : js.Dynamic) : TodoItem = TodoItem(obj._id.toString,
    obj._rev.toString,
    obj.title.toString,
    obj.completed.asInstanceOf[Boolean])

}