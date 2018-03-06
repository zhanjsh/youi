package io.youi.task

import io.youi.Updates

trait TaskSupport extends Updates {
  protected def createInstance(task: Task): TaskInstance = new TaskInstance(task, this)

  def start(task: Task): TaskInstance  = {
    val instance = createInstance(task)
    instance.start()
    instance
  }

  def updateTasks(): Boolean = true
}
