# Foodies
SDK tối thiểu: SDK 24 <br/>
Đề nghị: SDK 30, Android API 30 (Android 11)

# Spoonacular API
Spoonacular API phiên bản free, chỉ cho phép call API ~ 20 lần/ngày <br/>
Note: Spoonacular API free cho người dùng 150 points, mỗi lần request API tốn 1 point, mỗi kết quả trả về từ request tốn 0.1 points. <br/>
Sau khi hết 150 points, sẽ không thể call API và chạy app. Cần chờ 24h để reset số points.

#API Key
Đi đến đường dẫn: app/src/main/java/com/cozysatan1a/foodies/util/Constants.kt <br/>
Thay đổi trường const val API_KEY bằng: const val API_KEY = "c5f98105e9d34ddda9bda44153941062"
