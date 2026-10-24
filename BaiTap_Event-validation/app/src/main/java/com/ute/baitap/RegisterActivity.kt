package com.ute.baitap

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.ute.baitap.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

data class Student(
    val name: String,
    val email: String,
    val phone: String,
    val msv: String,
    val className: String
)

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var fields: List<Field>
    private var student: Student? = null

    /** Một ô nhập + layout chứa nó + luật kiểm tra (trả về null nếu hợp lệ, ngược lại trả về thông báo lỗi). */
    private class Field(
        val til: TextInputLayout,
        val edt: TextInputEditText,
        val rule: (String) -> String?
    )

    private companion object {
        val NAME_REGEX = Regex("^\\p{L}+(\\s+\\p{L}+)+$")            // >= 2 từ, chỉ chữ cái
        val PHONE_REGEX = Regex("^(0|\\+84)(3|5|7|8|9)\\d{8}$")        // SĐT Việt Nam
        val MSV_REGEX = Regex("^\\d{8,12}$")                           // 8-12 chữ số
        val CLASS_REGEX = Regex("^[A-Za-z0-9._-]{3,15}$")              // VD: 22T_DT1, 23CNTT1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFields()
        setupActions()
    }

    private fun setupFields() {
        fields = listOf(
            Field(binding.tilName, binding.edtName) { v ->
                when {
                    v.isEmpty() -> "Vui lòng nhập họ và tên"
                    !NAME_REGEX.matches(v) -> "Họ tên cần ít nhất 2 từ và chỉ gồm chữ cái"
                    else -> null
                }
            },
            Field(binding.tilEmail, binding.edtEmail) { v ->
                when {
                    v.isEmpty() -> "Vui lòng nhập email"
                    !Patterns.EMAIL_ADDRESS.matcher(v).matches() -> "Email không hợp lệ (VD: ten@gmail.com)"
                    else -> null
                }
            },
            Field(binding.tilPhone, binding.edtPhone) { v ->
                when {
                    v.isEmpty() -> "Vui lòng nhập số điện thoại"
                    !PHONE_REGEX.matches(v) -> "SĐT không hợp lệ (VD: 0912345678)"
                    else -> null
                }
            },
            Field(binding.tilMsv, binding.edtMsv) { v ->
                when {
                    v.isEmpty() -> "Vui lòng nhập mã sinh viên"
                    !MSV_REGEX.matches(v) -> "Mã sinh viên gồm 8-12 chữ số"
                    else -> null
                }
            },
            Field(binding.tilClass, binding.edtClass) { v ->
                when {
                    v.isEmpty() -> "Vui lòng nhập lớp"
                    !CLASS_REGEX.matches(v) -> "Lớp gồm 3-15 ký tự (chữ, số, - _ .)"
                    else -> null
                }
            }
        )

        fields.forEach { f ->
            // Đang hiện lỗi mà người dùng sửa -> kiểm tra lại ngay để lỗi biến mất khi hợp lệ
            f.edt.doOnTextChanged { _, _, _, _ ->
                if (f.til.error != null) f.validate()
            }
            // Rời khỏi ô có nội dung -> kiểm tra luôn
            f.edt.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus && f.edt.text.toString().isNotBlank()) f.validate()
            }
        }
    }

    private fun setupActions() {
        binding.btnRegister.setOnClickListener { onRegisterClicked() }

        // Nhấn Done trên bàn phím cũng đăng kí
        binding.edtClass.setOnEditorActionListener { _, _, _ ->
            onRegisterClicked()
            true
        }

        binding.btnDetail.setOnClickListener { openDetail() }
    }

    private fun Field.validate(): Boolean {
        val error = rule(edt.text.toString().trim())
        til.error = error
        return error == null
    }

    private fun onRegisterClicked() {
        hideKeyboard()

        // Kiểm tra TẤT CẢ các ô (không dừng ở ô lỗi đầu tiên) để hiện đủ lỗi
        val results = fields.map { it.validate() }

        if (results.all { it }) {
            student = Student(
                name = binding.edtName.text.toString().trim(),
                email = binding.edtEmail.text.toString().trim(),
                phone = binding.edtPhone.text.toString().trim(),
                msv = binding.edtMsv.text.toString().trim(),
                className = binding.edtClass.text.toString().trim()
            )
            showSuccessSnackbar()
        } else {
            fields.first { it.til.error != null }.edt.requestFocus()
            showErrorSnackbar()
        }
    }

    private fun showSuccessSnackbar() {
        Snackbar.make(binding.root, "Đăng kí thành công!", Snackbar.LENGTH_LONG)
            .setDuration(8000)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.success))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .setActionTextColor(ContextCompat.getColor(this, R.color.white))
            .setAction("XEM CHI TIẾT") { openDetail() }
            .show()
    }

    private fun showErrorSnackbar() {
        Snackbar.make(binding.root, "Vui lòng kiểm tra lại thông tin", Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.error_red))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()
    }

    private fun openDetail() {
        val intent = Intent(this, DetailActivity::class.java)
        student?.let {
            intent.putExtra(DetailActivity.EXTRA_NAME, it.name)
            intent.putExtra(DetailActivity.EXTRA_EMAIL, it.email)
            intent.putExtra(DetailActivity.EXTRA_PHONE, it.phone)
            intent.putExtra(DetailActivity.EXTRA_MSV, it.msv)
            intent.putExtra(DetailActivity.EXTRA_CLASS, it.className)
        }
        startActivity(intent)
    }

    private fun hideKeyboard() {
        currentFocus?.let {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}