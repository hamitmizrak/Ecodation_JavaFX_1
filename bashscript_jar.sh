#!/bin/bash

# 1️⃣ Proje dizinine git
cd "$(dirname "$0")"

# 2️⃣ Maven ile projeyi temizleyip derle
echo "🚀 Maven ile proje derleniyor..."
mvn clean package || { echo "❌ Maven build başarısız!"; exit 1; }

# 3️⃣ Çıktı klasörü
OUTPUT_DIR="./output_exe"
mkdir -p "$OUTPUT_DIR"

# 4️⃣ Ana JAR dosyasını belirle
JAR_FILE=$(find target -name "*.jar" | head -n 1)

# 5️⃣ Ana sınıfı (Main Class) tanımla
MAIN_CLASS="com.example.Main"  # Kendi ana sınıfını gir!

# 6️⃣ jpackage komutu ile .exe oluştur
echo "📦 .exe dosyası oluşturuluyor..."
jpackage --input target/ \
    --name MyJavaFXApp \
    --main-jar "$(basename "$JAR_FILE")" \
    --main-class "$MAIN_CLASS" \
    --type exe \
    --java-options "--enable-preview" \
    --win-menu \
    --win-shortcut \
    --win-dir-chooser \
    --win-per-user-install \
    --win-menu-group "My JavaFX App" \
    --dest "$OUTPUT_DIR" || { echo "❌ jpackage işlemi başarısız!"; exit 1; }

echo "✅ .exe başarıyla oluşturuldu! Çıktı dizini: $OUTPUT_DIR"
