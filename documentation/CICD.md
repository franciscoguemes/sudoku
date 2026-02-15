CI/CD
====================================




● Here's what this workflow does and how to use it:

Two ways to trigger it:

1. Tag-based release (recommended for actual releases):
   git tag v1.0.0
   git push origin v1.0.0
1. This builds on all 3 platforms and automatically creates a GitHub Release with the .deb, .dmg, and .msi attached as downloadable assets.
2. Manual trigger — via the "Actions" tab in GitHub, click "Run workflow" and enter a version number. Installers are uploaded as workflow artifacts (downloadable from the Actions run page).

What happens on each platform:

┌────────────────┬─────────────────┬────────────────────────────────────┐
│     Runner     │ jpackage output │              Bundles               │
├────────────────┼─────────────────┼────────────────────────────────────┤
│ ubuntu-latest  │ .deb installer  │ JRE + app + Linux JavaFX natives   │
├────────────────┼─────────────────┼────────────────────────────────────┤
│ macos-latest   │ .dmg disk image │ JRE + app + macOS JavaFX natives   │
├────────────────┼─────────────────┼────────────────────────────────────┤
│ windows-latest │ .msi installer  │ JRE + app + Windows JavaFX natives │
└────────────────┴─────────────────┴────────────────────────────────────┘

End users don't need Java installed — jpackage bundles a JRE into each installer.

Note on macOS: The current macos-latest runner is ARM64 (Apple Silicon). If you also need Intel Mac support, you'd add a second macOS entry with os: macos-13 (the last x64 runner).