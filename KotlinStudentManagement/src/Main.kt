import kotlin.math.min

/**
 Ho va ten: Tran Duc Tai
 Msv: 2415053122241
 */

data class Student(
    val id: String,
    var name: String,
    var age: Int,
    var major: String,
    var gpa: Double
)


val students = mutableListOf(
    Student("SV001", "Nguyen Van An", 20, "Cong nghe thong tin", 8.5),
    Student("SV002", "Tran Thi Bich", 21, "Ke toan", 6.8),
    Student("SV003", "Le Hoang Cuong", 19, "Cong nghe thong tin", 4.7),
    Student("SV004", "Pham Thi Dung", 22, "Quan tri kinh doanh", 7.9),
    Student("SV005", "Hoang Van Em", 23, "Cong nghe thong tin", 9.2)
)

fun main() {
    var running = true
    while (running) {
        printMainMenu()
        when (readLine()?.trim()) {
            "1" -> addStudent()
            "2" -> displayAllStudents(students)
            "3" -> searchStudentMenu()
            "4" -> calculateAverageGpaMenu()
            "5" -> findStudentWithHighestGpa()
            "6" -> removeStudent()
            "7" -> printAnalysisMenu()
            "0" -> {
                println("Tam biet!")
                running = false
            }
            else -> println("Lua chon khong hop le. Vui long thu lai.\n")
        }
    }
}



fun printMainMenu() {
    println(
        """
        |========== STUDENT MANAGEMENT ==========
        |1. Add student
        |2. Display all students
        |3. Search student
        |4. Calculate average GPA
        |5. Find student with highest GPA
        |6. Remove student
        |7. Analysis functions (yeu cau 1-12)
        |0. Exit
        |=========================================
        |Choose:
        """.trimMargin()
    )
}

fun printAnalysisMenu() {
    var back = false
    while (!back) {
        println(
            """
            |------- ANALYSIS FUNCTIONS -------
            |1. Dem so sinh vien co GPA >= 8.0
            |2. Dem so sinh vien co GPA < 5.0
            |3. Tinh GPA trung binh theo nganh
            |4. Tim sinh vien co GPA cao nhat
            |5. Tim sinh vien lon tuoi nhat
            |6. Tim sinh vien co GPA trong khoang 7.0 - 8.5
            |7. Tim tat ca sinh vien thuoc mot nganh
            |8. Tim sinh vien theo mot phan ten
            |9. Sap xep sinh vien theo GPA giam dan
            |10. Hien thi 3 sinh vien co GPA cao nhat
            |11. Sap xep sinh vien theo tuoi
            |12. Sap xep sinh vien theo ten
            |0. Quay lai menu chinh
            |-----------------------------------
            |Choose:
            """.trimMargin()
        )
        when (readLine()?.trim()) {
            "1" -> countGpaAtLeast(8.0)
            "2" -> countGpaBelow(5.0)
            "3" -> averageGpaByMajorMenu()
            "4" -> findStudentWithHighestGpa()
            "5" -> findOldestStudent()
            "6" -> findGpaInRange(7.0, 8.5)
            "7" -> findStudentsByMajorMenu()
            "8" -> findStudentsByNamePartMenu()
            "9" -> sortByGpaDescending()
            "10" -> topNByGpa(3)
            "11" -> sortByAge()
            "12" -> sortByName()
            "0" -> back = true
            else -> println("Lua chon khong hop le. Vui long thu lai.\n")
        }
    }
}


fun addStudent() {
    print("Nhap Student ID: ")
    val id = readLine()?.trim().orEmpty()

    if (id.isEmpty()) {
        println("Student ID khong duoc de trong.\n")
        return
    }
    if (students.any { it.id.equals(id, ignoreCase = true) }) {
        println("Student ID '$id' da ton tai.\n")
        return
    }

    print("Nhap Full Name: ")
    val name = readLine()?.trim().orEmpty()

    print("Nhap Age: ")
    val age = readLine()?.trim()?.toIntOrNull()
    if (age == null || age <= 0) {
        println("Age khong hop le.\n")
        return
    }

    print("Nhap Major: ")
    val major = readLine()?.trim().orEmpty()

    print("Nhap GPA (0.0 - 10.0): ")
    val gpa = readLine()?.trim()?.toDoubleOrNull()
    if (gpa == null || gpa < 0.0 || gpa > 10.0) {
        println("GPA khong hop le.\n")
        return
    }

    students.add(Student(id, name, age, major, gpa))
    println("Da them sinh vien '$name' thanh cong.\n")
}


fun displayAllStudents(list: List<Student>) {
    if (list.isEmpty()) {
        println("Danh sach sinh vien dang trong.\n")
        return
    }
    println(
        "%-8s %-22s %-5s %-22s %-6s".format("ID", "Full Name", "Age", "Major", "GPA")
    )
    println("-".repeat(70))
    for (s in list) {
        println(
            "%-8s %-22s %-5d %-22s %-6.2f".format(s.id, s.name, s.age, s.major, s.gpa)
        )
    }
    println()
}


fun searchStudentMenu() {
    print("Nhap Student ID hoac Full Name can tim: ")
    val keyword = readLine()?.trim().orEmpty()
    if (keyword.isEmpty()) {
        println("Tu khoa khong duoc de trong.\n")
        return
    }
    val results = students.filter {
        it.id.equals(keyword, ignoreCase = true) ||
                it.name.contains(keyword, ignoreCase = true)
    }
    if (results.isEmpty()) {
        println("Khong tim thay sinh vien nao khop voi '$keyword'.\n")
    } else {
        println("Ket qua tim kiem:")
        displayAllStudents(results)
    }
}



fun calculateAverageGpaMenu() {
    if (students.isEmpty()) {
        println("Danh sach rong, khong the tinh GPA trung binh.\n")
        return
    }
    val avg = students.map { it.gpa }.average()
    println("GPA trung binh cua toan bo sinh vien: %.2f\n".format(avg))
}



fun findStudentWithHighestGpa() {
    val top = students.maxByOrNull { it.gpa }
    if (top == null) {
        println("Danh sach rong.\n")
    } else {
        println("Sinh vien co GPA cao nhat:")
        displayAllStudents(listOf(top))
    }
}


fun removeStudent() {
    print("Nhap Student ID can xoa: ")
    val id = readLine()?.trim().orEmpty()
    val removed = students.removeIf { it.id.equals(id, ignoreCase = true) }
    if (removed) {
        println("Da xoa sinh vien co ID '$id'.\n")
    } else {
        println("Khong tim thay sinh vien co ID '$id'.\n")
    }
}



fun countGpaAtLeast(threshold: Double) {
    val count = students.count { it.gpa >= threshold }
    println("So sinh vien co GPA >= $threshold: $count\n")
}


fun countGpaBelow(threshold: Double) {
    val count = students.count { it.gpa < threshold }
    println("So sinh vien co GPA < $threshold: $count\n")
}



fun averageGpaByMajorMenu() {
    print("Nhap ten nganh: ")
    val major = readLine()?.trim().orEmpty()
    val filtered = students.filter { it.major.equals(major, ignoreCase = true) }
    if (filtered.isEmpty()) {
        println("Khong co sinh vien nao thuoc nganh '$major'.\n")
    } else {
        val avg = filtered.map { it.gpa }.average()
        println("GPA trung binh cua nganh '$major': %.2f (%d sinh vien)\n".format(avg, filtered.size))
    }
}



fun findOldestStudent() {
    val oldest = students.maxByOrNull { it.age }
    if (oldest == null) {
        println("Danh sach rong.\n")
    } else {
        println("Sinh vien lon tuoi nhat:")
        displayAllStudents(listOf(oldest))
    }
}


fun findGpaInRange(min: Double, max: Double) {
    val results = students.filter { it.gpa in min..max }
    if (results.isEmpty()) {
        println("Khong co sinh vien nao co GPA trong khoang $min - $max.\n")
    } else {
        println("Sinh vien co GPA trong khoang $min - $max:")
        displayAllStudents(results)
    }
}


fun findStudentsByMajorMenu() {
    print("Nhap ten nganh can tim: ")
    val major = readLine()?.trim().orEmpty()
    val results = students.filter { it.major.equals(major, ignoreCase = true) }
    if (results.isEmpty()) {
        println("Khong co sinh vien nao thuoc nganh '$major'.\n")
    } else {
        println("Danh sach sinh vien nganh '$major':")
        displayAllStudents(results)
    }
}



fun findStudentsByNamePartMenu() {
    print("Nhap mot phan ten can tim: ")
    val part = readLine()?.trim().orEmpty()
    val results = students.filter { it.name.contains(part, ignoreCase = true) }
    if (results.isEmpty()) {
        println("Khong tim thay sinh vien nao co ten chua '$part'.\n")
    } else {
        println("Ket qua tim kiem theo ten:")
        displayAllStudents(results)
    }
}



fun sortByGpaDescending() {
    val sorted = students.sortedByDescending { it.gpa }
    println("Danh sach sinh vien sap xep theo GPA giam dan:")
    displayAllStudents(sorted)
}


fun topNByGpa(n: Int) {
    if (students.isEmpty()) {
        println("Danh sach rong.\n")
        return
    }
    val top = students.sortedByDescending { it.gpa }.take(min(n, students.size))
    println("Top ${top.size} sinh vien co GPA cao nhat:")
    displayAllStudents(top)
}



fun sortByAge() {
    val sorted = students.sortedBy { it.age }
    println("Danh sach sinh vien sap xep theo tuoi (tang dan):")
    displayAllStudents(sorted)
}



fun sortByName() {
    val sorted = students.sortedBy { it.name.lowercase() }
    println("Danh sach sinh vien sap xep theo ten (A-Z):")
    displayAllStudents(sorted)
}