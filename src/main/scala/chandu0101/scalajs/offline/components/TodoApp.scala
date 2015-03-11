package chandu0101.scalajs.offline.components

import chandu0101.scalajs.offline.model.TodoItem
import chandu0101.scalajs.offline.services.TodoService
import chandu0101.scalajs.offline.util.PouchDBChangesListener
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.raw.KeyboardEvent

/**
 * Created by chandrasekharkode on 2/25/15.
 */
object TodoApp {


  case class State(list: List[TodoItem] = List())

  class Backend(t: BackendScope[_, State]) extends PouchDBChangesListener[TodoItem](TodoService) {

    def handleItemUpdate(item: TodoItem) = TodoService.save(item)

    def handleItemDelete(item: TodoItem) = TodoService.remove(item)

    def handleItemSave(e: ReactEventI): Unit = {
      val text = e.target.value
      if (e.asInstanceOf[KeyboardEvent].keyCode == KeyCode.enter && text.nonEmpty) {
        e.target.value = ""
        TodoService.save(TodoItem(title = text))
      }
    }

    override def updateNewData(data: List[TodoItem]): Unit = t.modState(_.copy(list = data))


  }

  val todoList = ReactComponentB[(State, Backend)]("TodoList")
    .render(P => {
    val (s, b) = P
    ul(
      s.list.map(item => li(paddingBottom := "6px",
        input(tpe := "checkbox", checked := item.completed, onChange --> b.handleItemUpdate(item.copy(completed = !item.completed))),
        span(paddingLeft := "10px", item.title),
        button(marginLeft := "10px", onClick --> b.handleItemDelete(item))("X")
      ))
    )
  })
    .build


  val component = ReactComponentB[Unit]("TodoApp")
    .initialState(State())
    .backend(new Backend(_))
    .render((P, S, B) => {
    div(cls := "todoapp",
      input(marginLeft := "60px", tpe := "text", onKeyUp ==> B.handleItemSave,placeholder := "type a todo here .."),
      todoList((S, B))
    )
  })
    .componentDidMount(scope => scope.backend.startListening)
    .buildU

  def apply() = component()
}
