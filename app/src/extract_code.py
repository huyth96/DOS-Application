import os

# Đường dẫn gốc – để trỏ đúng vào thư mục src hiện tại
ROOT_DIR = os.path.abspath(os.getcwd())   # nếu chạy từ src, giữ nguyên
OUTPUT_FILE = "all_code_output.txt"

# Phần mở rộng muốn trích (có thể thêm bớt tùy ý)
VALID_EXTS = {".java", ".kt", ".xml", ".gradle", ".properties", ".json", ".txt"}

def should_include(filename: str) -> bool:
    # chỉ lấy những file có đuôi trong VALID_EXTS
    return os.path.splitext(filename)[1].lower() in VALID_EXTS

with open(OUTPUT_FILE, "w", encoding="utf-8") as out:
    for root, dirs, files in os.walk(ROOT_DIR):
        for f in files:
            if should_include(f):
                full_path = os.path.join(root, f)
                rel_path = os.path.relpath(full_path, ROOT_DIR)  # đường dẫn tương đối từ src
                out.write("=" * 30 + "\n")
                out.write(f"File: {rel_path}\n")
                out.write("=" * 30 + "\n")
                try:
                    with open(full_path, "r", encoding="utf-8", errors="ignore") as code:
                        out.write(code.read())
                except Exception as e:
                    out.write(f"[LỖI ĐỌC FILE: {e}]\n")
                out.write("\n\n")
print(f"✅ Đã trích xuất xong. Kiểm tra file: {OUTPUT_FILE}")
