# Contributing

## Branch naming
- `feature/` — new features
- `fix/` — bug fixes
- `refactor/` — code cleanup without changing functionality
- `docs/` — documentation only

## Commit messages
- First line: short summary (50 chars max)
- Body: what changed and why

## Code style
- Implementations prefixed with the technology (e.g. `GDX_GraphicsManager`)
- Javadoc on all public interfaces and methods

## Module structure
- `api/` — shared interfaces, no external dependencies
- `gameEngine/` — rendering Implementations, depends on `api`
- `app/` — game logic Implementations, depends on `api` and `gameEngine` if the game has a graphic
