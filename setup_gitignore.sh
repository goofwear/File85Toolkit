#!/data/data/com.termux/files/usr/bin/bash
# Overwrite .gitignore with a safe version for F-Droid/GitHub

cat > .gitignore <<'EOF'
# Gradle
.gradle/
build/
*/build/

# Android Studio / IntelliJ
.idea/
*.iml
*.ipr
*.iws

# Local properties
local.properties

# Keystore files (never commit these!)
*.jks
*.keystore
*.keystore.properties
*.pem
gradle.properties

# Logs
*.log

# Fastlane generated outputs (keep metadata/screenshots for F-Droid)
fastlane/report.xml
fastlane/Preview.html
fastlane/test_output

# APKs / Bundles
*.apk
*.ap_
*.aab

# Cache
.cache/
.cxx/
EOF

echo "✅ .gitignore has been updated safely."
