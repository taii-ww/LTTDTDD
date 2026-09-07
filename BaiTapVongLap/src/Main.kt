//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    // Bài 1: In các số từ 1 đến 10
    println("===== Bài 1 =====")
    for (i in 1..10) {
        print("$i ")
    }
    println()

    // Bài 2: Tính tổng 1 + 2 + ... + 100
    println("\n===== Bài 2 =====")
    var tong = 0
    for (i in 1..100) {
        tong += i
    }
    println("Tổng từ 1 đến 100 là: $tong")

    // Bài 3: In các số chẵn từ 1 đến 20
    println("\n===== Bài 3 =====")
    var i = 1
    while (i <= 20) {
        if (i % 2 == 0) {
            print("$i ")
        }
        i++
    }
    println()
}