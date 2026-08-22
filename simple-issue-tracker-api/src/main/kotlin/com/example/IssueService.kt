package com.example

class IssueService(
    private val repository: IssueRepository,
) {
    fun getAll(status: IssueStatus? = null, priority: IssuePriority? = null): List<Issue> =
        repository.findAll(status, priority)

    fun getById(id: Int): Issue? =
        repository.findById(id)

    fun create(request: CreateIssueRequest): Issue {
        validateText(request.title, "Title")
        validateText(request.description, "Description")
        return repository.create(request)
    }

    fun update(id: Int, request: UpdateIssueRequest): Issue? {
        validateText(request.title, "Title")
        validateText(request.description, "Description")
        return repository.update(id, request)
    }

    fun changeStatus(id: Int, status: IssueStatus): Issue? =
        repository.updateStatus(id, status)

    fun delete(id: Int): Boolean =
        repository.delete(id)

    private fun validateText(value: String, fieldName: String) {
        require(value.isNotBlank()) { "$fieldName must not be blank" }
    }
}
