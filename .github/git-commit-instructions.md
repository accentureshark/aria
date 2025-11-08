# Git Commit Instructions - Aria Project

This project follows the [Conventional Commits](https://www.conventionalcommits.org/) specification for structured and readable commit messages.

## Commit Message Format

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

**All commit messages MUST be written in English.**

## Commit Types

- **feat**: A new feature
- **fix**: A bug fix
- **docs**: Documentation only changes
- **style**: Changes that do not affect the meaning of the code (formatting, semicolons, etc.)
- **refactor**: A code change that neither fixes a bug nor adds a feature
- **perf**: A code change that improves performance
- **test**: Adding missing tests or correcting existing tests
- **build**: Changes that affect the build system or external dependencies
- **ci**: Changes to CI/CD configuration files and scripts
- **chore**: Other changes that don't modify src or test files
- **revert**: Reverts a previous commit

## Scopes

Based on the hexagonal architecture:

- `tickets`: Ticket-related functionality
- `persons`: Person-related functionality
- `controller`: Changes in adapter layer (REST controllers)
- `service`: Changes in domain layer (business logic)
- `repository`: Changes in adapter layer (persistence)
- `dto`: Changes in DTOs
- `model`: Changes in domain entities
- `mapper`: Changes in mappers
- `config`: Application configuration
- `database`: Database schema or migrations

## Jira Integration

### Branch Naming Convention

Branches MUST include the Jira ticket ID:

```
feature/ARIA-123-add-ticket-filtering
bugfix/ARIA-456-fix-lazy-loading
hotfix/ARIA-789-critical-email-validation
```

### Commit Message with Jira Reference

Every commit MUST reference the Jira ticket from the branch name in the footer:

```
feat(tickets): add filtering by priority and status

Implement query parameters in GET /api/tickets endpoint to filter
tickets by priority (LOW, MEDIUM, HIGH, URGENT) and status
(OPEN, IN_PROGRESS, RESOLVED, CLOSED).

Refs: ARIA-123
```

### Automatic Jira Linking

Use these keywords in the footer to update Jira tickets automatically:

- **Refs**: Reference a ticket (no status change)
- **Closes**: Close a ticket when PR is merged
- **Fixes**: Mark a bug as fixed when PR is merged
- **Relates**: Link related tickets

```
fix(person): resolve lazy loading exception in relationships

PersonMapper now handles null objects safely when serializing
PersonResponseDTO to avoid LazyInitializationException.

Fixes: ARIA-456
```

## Examples

### Feature Implementation

```
feat(tickets): implement POST /api/tickets endpoint

- Add TicketController.create() method
- Add Jakarta validation in TicketRequestDTO
- Implement TicketService.createFromDTO() with relationship resolution
- Map reporterId and assigneeId to Person entities

Refs: ARIA-123
```

### Bug Fix

```
fix(mapper): handle null Person in TicketMapper.toResponse()

When a ticket has no assignee or reporter, the mapper threw NPE.
Added null checks in PersonMapper.toResponse().

Fixes: ARIA-456
```

### Refactoring

```
refactor(service): apply two-methods-per-operation pattern

- Split create() for internal use
- Add createFromDTO() for controller use
- Same pattern applied to update() and updateFromDTO()

Improves separation of concerns following hexagonal architecture.

Refs: ARIA-789
```

### Documentation

```
docs(readme): update API endpoint examples

Add examples for Person endpoints including filtering by department
and email lookup.

Refs: ARIA-234
```

### Breaking Changes

When introducing breaking changes, add `!` after type/scope and explain in footer:

```
feat(api)!: change TicketResponseDTO structure

BREAKING CHANGE: The 'reporter' field is now a PersonResponseDTO object
instead of a String. Clients must update their API consumers.

Before:
{
  "reporter": "john@example.com"
}

After:
{
  "reporter": {
    "id": 1,
    "email": "john@example.com",
    "fullName": "John Doe"
  }
}

Refs: ARIA-567
```

## Multi-Ticket Commits

If a commit relates to multiple tickets:

```
feat(service): implement person deactivation and audit log

Add deactivate() method in PersonService and audit logging for
person status changes.

Refs: ARIA-123, ARIA-124
```

## Commit Message Guidelines

### DO ✅

- Write in English
- Use imperative mood ("add" not "added" or "adds")
- Start description with lowercase
- No period (.) at the end of description
- Keep description under 72 characters
- Reference Jira ticket in footer
- Explain "why" in body, not "what" (code shows "what")

### DON'T ❌

- Write in Spanish or mixed languages
- Use past tense
- Capitalize first letter (except proper nouns)
- Add period at end
- Write vague descriptions ("fix bug", "update code")
- Forget Jira reference
- Leave body empty for complex changes

## Examples by Scenario

### Adding a new entity

```
feat(model): add Comment entity for ticket discussions

Create Comment entity with relationship to Ticket and Person.
Includes createdAt timestamp and content field with 5000 char limit.

Refs: ARIA-890
```

### Fixing validation

```
fix(dto): correct email validation in PersonRequestDTO

Email regex was rejecting valid emails with plus signs.
Updated to RFC 5322 compliant pattern.

Fixes: ARIA-234
```

### Adding tests

```
test(service): add unit tests for TicketService.createFromDTO()

Cover scenarios:
- Creating ticket without reporter/assignee
- Creating ticket with valid IDs
- Creating ticket with non-existent person IDs

Refs: ARIA-345
```

### Updating dependencies

```
build(pom): upgrade Spring Boot to 3.2.9

Includes security patches and performance improvements.

Refs: ARIA-678
```

### Database changes

```
feat(database): add index on Person.email for faster lookups

Improves performance of findByEmail() queries by 60%.

Refs: ARIA-456
```

## Tools

Recommended tools for enforcing Conventional Commits:

- **commitlint**: Validate commit messages in CI
- **Commitizen**: CLI tool for guided commit messages
- **Husky**: Git hooks for pre-commit validation

## Pre-commit Hook Example

Extract Jira ticket from branch name and validate format:

```bash
#!/bin/bash
# .git/hooks/commit-msg

COMMIT_MSG_FILE=$1
COMMIT_MSG=$(cat "$COMMIT_MSG_FILE")
BRANCH_NAME=$(git symbolic-ref --short HEAD)

# Extract Jira ticket from branch
JIRA_TICKET=$(echo "$BRANCH_NAME" | grep -o 'ARIA-[0-9]\+')

# Check if commit has Jira reference
if ! grep -q "Refs: ARIA-" "$COMMIT_MSG_FILE"; then
  if [ -n "$JIRA_TICKET" ]; then
    echo "" >> "$COMMIT_MSG_FILE"
    echo "Refs: $JIRA_TICKET" >> "$COMMIT_MSG_FILE"
  fi
fi
```

## References

- [Conventional Commits Specification](https://www.conventionalcommits.org/)
- [Atlassian Git Commit Best Practices](https://www.atlassian.com/git/tutorials/saving-changes/git-commit)
- [Jira Smart Commits](https://support.atlassian.com/jira-software-cloud/docs/process-issues-with-smart-commits/)
