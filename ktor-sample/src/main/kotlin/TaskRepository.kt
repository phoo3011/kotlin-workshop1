package com.example

object TaskRepository {
    private val tasks = mutableListOf<Task>()
    private var nextId = 1

    fun getAll(): List<Task> = tasks

    fun getById(id: Int): Task? = tasks.find { it.id == id }

    fun add(taskRequest: TaskRequest): Task {
        val task = Task(
            id = nextId++,
            content = taskRequest.content,
            isDone = taskRequest.isDone,
        )
        tasks.add(task)
        return task
    }

    fun update(id: Int, updatedTask: Task): Task? {
        val index = tasks.indexOfFirst { it.id == id }
        if (index == -1) return null

        val task = updatedTask.copy(id = id)
        tasks[index] = task
        return task
    }

    fun delete(id: Int): Boolean = tasks.removeIf { it.id == id }
}
