package com.example

import kotlinx.serialization.Serializable

@Serializable
enum class IssueStatus {
    OPEN,
    IN_PROGRESS,
    CLOSED,
}

@Serializable
enum class IssuePriority {
    LOW,
    MEDIUM,
    HIGH,
}

@Serializable
data class Issue(
    val id: Int,
    val title: String,
    val description: String,
    val status: IssueStatus,
    val priority: IssuePriority,
)

@Serializable
data class CreateIssueRequest(
    val title: String,
    val description: String,
    val status: IssueStatus = IssueStatus.OPEN,
    val priority: IssuePriority,
)

@Serializable
data class UpdateIssueRequest(
    val title: String,
    val description: String,
    val status: IssueStatus,
    val priority: IssuePriority,
)

@Serializable
data class UpdateIssueStatusRequest(
    val status: IssueStatus,
)

@Serializable
data class ErrorResponse(
    val message: String,
)
