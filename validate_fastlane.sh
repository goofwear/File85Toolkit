#!/bin/bash
# validate_fastlane.sh
# Validates Fastlane metadata structure for File85Toolkit

BASE_DIR="fastlane/metadata/android/en-US"
CHANGELOG_DIR="fastlane/metadata/android/changelogs"
ERRORS=0

echo "🔍 Checking Fastlane metadata structure..."

# Check title.txt
if [ ! -f "$BASE_DIR/title.txt" ]; then
  echo "❌ Missing: $BASE_DIR/title.txt"
  ERRORS=$((ERRORS+1))
else
  echo "✔ Found: $BASE_DIR/title.txt"
fi

# Check short_description.txt
if [ ! -f "$BASE_DIR/short_description.txt" ]; then
  echo "❌ Missing: $BASE_DIR/short_description.txt"
  ERRORS=$((ERRORS+1))
else
  echo "✔ Found: $BASE_DIR/short_description.txt"
fi

# Check full_description.txt
if [ ! -f "$BASE_DIR/full_description.txt" ]; then
  echo "❌ Missing: $BASE_DIR/full_description.txt"
  ERRORS=$((ERRORS+1))
else
  echo "✔ Found: $BASE_DIR/full_description.txt"
fi

# Check changelog file(s)
if [ ! -d "$CHANGELOG_DIR" ] || [ -z "$(ls -A $CHANGELOG_DIR 2>/dev/null)" ]; then
  echo "❌ Missing: changelog file(s) in $CHANGELOG_DIR"
  ERRORS=$((ERRORS+1))
else
  echo "✔ Found changelog(s) in $CHANGELOG_DIR"
fi

# Check screenshots (must be 7)
SCREENSHOT_DIR="$BASE_DIR/images/phoneScreenshots"
if [ ! -d "$SCREENSHOT_DIR" ]; then
  echo "❌ Missing: $SCREENSHOT_DIR folder"
  ERRORS=$((ERRORS+1))
else
  COUNT=$(ls -1 "$SCREENSHOT_DIR"/*.jpg 2>/dev/null | wc -l)
  if [ "$COUNT" -lt 7 ]; then
    echo "❌ Only $COUNT screenshots found in $SCREENSHOT_DIR (expected 7)"
    ERRORS=$((ERRORS+1))
  else
    echo "✔ Found $COUNT screenshots in $SCREENSHOT_DIR"
  fi
fi

# Summary
if [ "$ERRORS" -eq 0 ]; then
  echo "✅ All required Fastlane metadata files are in place!"
else
  echo "⚠️ Validation finished with $ERRORS issue(s)."
fi
