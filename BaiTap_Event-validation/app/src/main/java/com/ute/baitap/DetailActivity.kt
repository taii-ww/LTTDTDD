package com.ute.baitap

import android.content.Intent
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.ute.baitap.databinding.ActivityDetailBinding
import com.ute.baitap.databinding.ItemInfoBinding

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_PHONE = "extra_phone"
        const val EXTRA_MSV = "extra_msv"
        const val EXTRA_CLASS = "extra_class"
        private const val EMPTY_TEXT = "Chưa có thông tin"
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(EXTRA_NAME)
        val email = intent.getStringExtra(EXTRA_EMAIL)
        val phone = intent.getStringExtra(EXTRA_PHONE)
        val msv = intent.getStringExtra(EXTRA_MSV)
        val className = intent.getStringExtra(EXTRA_CLASS)

        bindHeader(name)

        bindRow(binding.rowName, R.drawable.ic_person, "Họ và tên", name)
        bindRow(binding.rowEmail, R.drawable.ic_email, "Email", email)
        bindRow(binding.rowPhone, R.drawable.ic_phone, "Số điện thoại", phone)
        bindRow(binding.rowMsv, R.drawable.ic_id, "Mã sinh viên", msv)
        bindRow(binding.rowClass, R.drawable.ic_class, "Lớp", className)

        binding.btnBack.setOnClickListener {
            // Quay về màn Đăng kí bằng Intent, giữ nguyên dữ liệu đã nhập
            val intent = Intent(this, RegisterActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            startActivity(intent)
        }
    }

    private fun bindHeader(name: String?) {
        if (name.isNullOrBlank()) {
            binding.tvAvatar.text = "?"
            binding.tvHeaderName.text = "Chưa có dữ liệu"
            binding.tvStatus.text = "Hãy quay lại và đăng kí trước nhé"
        } else {
            // Chữ cái đầu của tên (từ cuối cùng trong họ tên)
            binding.tvAvatar.text = name.trim().split(" ").last().first().uppercase()
            binding.tvHeaderName.text = name
            binding.tvStatus.text = "Đăng kí thành công"
        }
    }

    private fun bindRow(
        row: ItemInfoBinding,
        @DrawableRes icon: Int,
        label: String,
        value: String?
    ) {
        row.ivIcon.setImageResource(icon)
        row.tvLabel.text = label
        row.tvValue.text = if (value.isNullOrBlank()) EMPTY_TEXT else value
    }
}