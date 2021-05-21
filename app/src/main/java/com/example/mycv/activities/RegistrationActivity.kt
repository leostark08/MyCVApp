  package com.example.mycv.activities

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mycv.R
import com.example.mycv.models.UserModel
import com.example.mycv.services.RestAPIService
import kotlinx.android.synthetic.main.activity_registration.*
import java.text.SimpleDateFormat
import java.util.*

  class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        validateSignUpForm()
//        sign up button event
        btnSignUp.setOnClickListener {
            SignUp()
        }
//        back to login
        btnBackToLogin.setOnClickListener {
            onBackPressed()
        }
    }


    private fun SignUp() {
//        starting progress
        progressCircular.visibility = View.VISIBLE
//        initialize dialog to show notification
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Sign up")
//        assign RestAPIService variable
        val apiService = RestAPIService()

//        initialize newUser variable to storage user information from sign up form
        val newUser = UserModel(
            _id = "",
            userName = etFullname.text.toString(),
            gender = rGrGender.checkedRadioButtonId,
            dateOfBirth = etDoB.text.toString(),
            cellphone = etCellphone.text.toString(),
            email = etSignUpEmail.text.toString(),
            password = etConfirmSignUpPassword.text.toString(),
            address = "No address yet!",
            specialization = "No specialization yet!",
            description = "No description yet!")

//        call createUserAdcount function to create user account into database
        apiService.createUserAccount(newUser) {
            if (it?._id != null) {
                alertDialog.setMessage("Your account has been created successfully!")
                alertDialog.setIcon(R.drawable.ic_done)
                alertDialog.setPositiveButton("Login now") { dialogInterface, which ->
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            } else {
                alertDialog.setMessage("An error occurred. Please try again later!")
                alertDialog.setIcon(R.drawable.ic_report)
                alertDialog.setPositiveButton("Try again") { dialogInterface, which ->
                    dialogInterface.dismiss()
                }
            }
//            dismiss progress
            progressCircular.visibility = View.GONE
//            show dialog
            alertDialog.show()
        }
    }

    fun validateSignUpForm() {
//        initialize edittext regular expression variables
        val regexFullName = "(([A-Z][a-z]+) ?){2,}".toRegex()
        val regexDoB = "^\\d{2}[./-]\\d{2}[./-]\\d{4}\$".toRegex()
        val regexCellphone = "\\(?([0-9]{3})\\)?([ .-]?)([0-9]{3})\\2([0-9]{4})".toRegex()
        val regexEmail =
            "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))\$".toRegex()
        val regexPassword = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,}\$".toRegex()
//        validate full name edit text value
        etFullname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (etFullname.text.toString() == "") {
                    etLayoutFullname.error = "Please enter your full name"
                } else if (!regexFullName.matches(etFullname.text.toString())) {
                    etLayoutFullname.error = "Please enter your correct full name"
                } else etLayoutFullname.isErrorEnabled = false
            }
        })
//        validate date of birth edit text value
        etDoB.setOnClickListener {
//            hide keyboard
            val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
//            pick date of birth dialog
            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, month, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    val simpleDateFormat = SimpleDateFormat("dd MMMM, YYYY", Locale.US)
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val formatedDate = simpleDateFormat.format(selectedDate.time)
                    etDoB.setText(formatedDate)
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
//        validate cellphone edit text value
        etCellphone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (etCellphone.text.toString() == "") {
                    etLayoutCellphone.error = "Please enter your cellphone"
                } else if (!regexCellphone.matches(etCellphone.text.toString())) {
                    etLayoutCellphone.error = "10 digits"
                } else etLayoutCellphone.isErrorEnabled = false
            }
        })
//        validate email edit text value
        etSignUpEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (etSignUpEmail.text.toString() == "") {
                    etLayoutEmail.error = "Please enter your email"
                } else if (!regexEmail.matches(etSignUpEmail.text.toString())) {
                    etLayoutEmail.error = "Email is incorrect"
                } else etLayoutEmail.isErrorEnabled = false
            }
        })
//        validate password edit text value
        etSignUpPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (etConfirmSignUpPassword.text.toString() != "") {
                    etConfirmSignUpPassword.setText("")
                }
                if (etSignUpPassword.text.toString() == "") {
                    etLayoutSignUpPassword.error = "Please enter your password"
                } else if (!regexPassword.matches(etSignUpPassword.text.toString())) {
                    etLayoutSignUpPassword.error =
                        "8+ characters contain uppercase, lowercase and number"
                } else etLayoutSignUpPassword.isErrorEnabled = false
            }
        })
//        validate confirm password edit text value
        etConfirmSignUpPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (etSignUpPassword.text.toString() == "") {
                    etLayoutSignUpConfirmPassword.error = "You have not entered the password"
                } else if (etConfirmSignUpPassword.text.toString() == "") {
                    etLayoutSignUpConfirmPassword.error = "Please enter your confirm password"
                } else if (etConfirmSignUpPassword.text.toString() != etSignUpPassword.text.toString()) {
                    etLayoutSignUpConfirmPassword.error = "Mismatch"
                } else etLayoutSignUpConfirmPassword.isErrorEnabled = false
            }
        })
    }
}