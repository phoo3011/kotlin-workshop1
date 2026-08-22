package com.example

import kotlin.test.Test
import kotlin.test.assertFailsWith

class IssueServiceTest {
    @Test
    fun `create rejects blank title`() {
        val service = IssueService(IssueRepository())

        assertFailsWith<IllegalArgumentException> {
            service.create(
                CreateIssueRequest(
                    title = " ",
                    description = "Description is valid",
                    priority = IssuePriority.HIGH,
                ),
            )
        }
    }

    @Test
    fun `update rejects blank description`() {
        val service = IssueService(IssueRepository())

        assertFailsWith<IllegalArgumentException> {
            service.update(
                id = 1,
                request = UpdateIssueRequest(
                    title = "Valid title",
                    description = "",
                    status = IssueStatus.OPEN,
                    priority = IssuePriority.MEDIUM,
                ),
            )
        }
    }
}
