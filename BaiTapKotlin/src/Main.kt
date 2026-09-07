//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import kotlin.math.max

fun main() {
    print("Nhập tên sinh viên: ")
    val ten = readLine()!!

    print("Nhập điểm Math: ")
    val math = readLine()!!.toDouble()

    print("Nhập điểm Programming: ")
    val programming = readLine()!!.toDouble()

    print("Nhập điểm Database: ")
    val database = readLine()!!.toDouble()

    val tongDiem = math + programming + database
    val diemTrungBinh = tongDiem / 3
    val diemCaoNhat = max(math, max(programming, database))
    val datKhong = diemTrungBinh >= 5.0

    println("\n===== KẾT QUẢ =====")
    println("Sinh viên: $ten")
    println("Điểm Math: $math")
    println("Điểm Programming: $programming")
    println("Điểm Database: $database")
    println("Tổng điểm: $tongDiem")
    println("Điểm trung bình: $diemTrungBinh")
    println("Điểm cao nhất: $diemCaoNhat")
    println("Sinh viên có đạt không (GPA >= 5.0)? ${if (datKhong) "Đạt" else "Không đạt"}")
}