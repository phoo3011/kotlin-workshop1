package com.example

class IssueRepository {
    private val issues = mutableListOf<Issue>()
    private var nextId = 1

    fun findAll(status: IssueStatus? = null, priority: IssuePriority? = null): List<Issue> =
        issues.filter { issue ->
            (status == null || issue.status == status) &&
                (priority == null || issue.priority == priority)
        }

    fun findById(id: Int): Issue? =
        issues.find { issue -> issue.id == id }

    fun create(request: CreateIssueRequest): Issue {
        val issue = Issue(
            id = nextId,
            title = request.title,
            description = request.description,
            status = request.status,
            priority = request.priority,
        )
        nextId += 1
        issues.add(issue)
        return issue
    }

    fun update(id: Int, request: UpdateIssueRequest): Issue? {
        val index = issues.indexOfFirst { issue -> issue.id == id }
        if (index == -1) return null

        val updatedIssue = Issue(
            id = id,
            title = request.title,
            description = request.description,
            status = request.status,
            priority = request.priority,
        )
        issues[index] = updatedIssue
        return updatedIssue
    }

    fun updateStatus(id: Int, status: IssueStatus): Issue? {
        val index = issues.indexOfFirst { issue -> issue.id == id }
        if (index == -1) return null

        val updatedIssue = issues[index].copy(status = status)
        issues[index] = updatedIssue
        return updatedIssue
    }

    fun delete(id: Int): Boolean =
        issues.removeIf { issue -> issue.id == id }
}
