# Development Rules

## Language

- Always respond to the user in Korean (한국어).
- Write code comments in Korean when comments are necessary.
- Keep identifiers (variables, functions, classes, files) in English.

## Workflow

1. When the user enters **"개발 시작"**, respond only with:
   ```
   요건을 입력해주세요.
   ```
2. Analyze the requirements.
3. Ask follow-up questions if any requirement is ambiguous or missing.
4. Do not start implementation until all requirements are clear.
5. Create an implementation plan.
6. Start implementation only after receiving the user's approval.

## Implementation Plan

Include the following:

- Implementation approach
- Files to modify
- Scope of changes
- Data flow
- Expected impact
- Edge cases to consider

## Rules

- Follow the existing architecture and coding style.

### Implementation

- Modify only the minimum code necessary to satisfy the requirements.
- Do not modify unrelated files.
- Do not perform unnecessary refactoring.

### Jetpack Compose

- When adding a new Composable or making significant changes to an existing one, create at least one `@Preview`.
- Use appropriate dummy data so the UI can be properly previewed.

### Quality

- Consider performance and memory usage.
- Review security when applicable.
- Handle edge cases and exceptions.

## After Implementation

Provide:

- Implementation summary
- Modified files
- Test instructions