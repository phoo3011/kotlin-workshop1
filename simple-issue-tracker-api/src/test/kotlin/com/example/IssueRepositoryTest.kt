package com.example

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class IssueRepositoryTest {
    @Test
    fun `create generates id and stores issue`() {
        val repository = IssueRepository()

        val issue = repository.create(
            CreateIssueRequest(
                title = "Login bug",
                description = "Login fails with correct password",
                priority = IssuePriority.HIGH,
            ),
        )

        assertEquals(1, issue.id)
        assertEquals(issue, repository.findById(1))
    }

    @Test
    fun `findAll filters issues by status and priority`() {
        val repository = IssueRepository()
        repository.create(CreateIssueRequest("A", "Open high", IssueStatus.OPEN, IssuePriority.HIGH))
        repository.create(CreateIssueRequest("B", "Closed high", IssueStatus.CLOSED, IssuePriority.HIGH))
        repository.create(CreateIssueRequest("C", "Open low", IssueStatus.OPEN, IssuePriority.LOW))

        val result = repository.findAll(status = IssueStatus.OPEN, priority = IssuePriority.HIGH)

        assertEquals(listOf("A"), result.map { issue -> issue.title })
    }

    @Test
    fun `update replaces issue fields but keeps same id`() {
        val repository = IssueRepository()
        repository.create(CreateIssueRequest("Old title", "Old description", priority = IssuePriority.LOW))

        val updated = repository.update(
            id = 1,
            request = UpdateIssueRequest(
                title = "New title",
                description = "New description",
                status = IssueStatus.IN_PROGRESS,
                priority = IssuePriority.HIGH,
            ),
        )

        assertEquals(1, updated?.id)
        assertEquals("New title", updated?.title)
        assertEquals(IssueStatus.IN_PROGRESS, updated?.status)
        assertEquals(IssuePriority.HIGH, updated?.priority)
    }

    @Test
    fun `updateStatus changes status without changing other fields`() {
        val repository = IssueRepository()
        repository.create(CreateIssueRequest("Crash", "App crashes", priority = IssuePriority.MEDIUM))

        val updated = repository.updateStatus(id = 1, status = IssueStatus.CLOSED)

        assertEquals("Crash", updated?.title)
        assertEquals("App crashes", updated?.description)
        assertEquals(IssueStatus.CLOSED, updated?.status)
        assertEquals(IssuePriority.MEDIUM, updated?.priority)
    }

    @Test
    fun `delete removes existing issue`() {
        val repository = IssueRepository()
        repository.create(CreateIssueRequest("Remove me", "Temporary issue", priority = IssuePriority.LOW))

        val deleted = repository.delete(1)

        assertEquals(true, deleted)
        assertNull(repository.findById(1))
    }

    @Test
    fun `delete returns false when issue does not exist`() {
        val repository = IssueRepository()

        assertFalse(repository.delete(999))
    }
}
