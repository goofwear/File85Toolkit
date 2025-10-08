#!/data/data/com.termux/files/usr/bin/bash

SRC_DIR="app/src/main/res"
TARGET_NAME="file85_icon.png"

# Map densities to source mipmap dirs
declare -A DENSITIES=(
  ["ldpi"]="mipmap-ldpi"
  ["mdpi"]="mipmap-mdpi"
  ["hdpi"]="mipmap-hdpi"
  ["xhdpi"]="mipmap-xhdpi"
  ["xxhdpi"]="mipmap-xxhdpi"
  ["xxxhdpi"]="mipmap-xxxhdpi"
)

echo "📦 Copying icons into drawable-* folders..."
for dpi in "${!DENSITIES[@]}"; do
    src_file="$SRC_DIR/${DENSITIES[$dpi]}/ic_launcher_foreground.png"
    dest_dir="$SRC_DIR/drawable-$dpi"
    dest_file="$dest_dir/$TARGET_NAME"

    mkdir -p "$dest_dir"

    if [ -f "$src_file" ]; then
        cp "$src_file" "$dest_file"
        echo "✔ Copied $src_file → $dest_file"
    else
        echo "⚠ Missing $src_file, skipped $dpi"
    fi
done

echo "📝 Updating XML references..."
# Force launcher XMLs to use @drawable/file85_icon
sed -i 's|@mipmap/ic_launcher_foreground|@drawable/file85_icon|g' \
    "$SRC_DIR/mipmap-anydpi-v26/ic_launcher.xml" \
    "$SRC_DIR/mipmap-anydpi-v26/ic_launcher_round.xml"

echo "✅ Done!"
