package com.akv.akvandroidapprelease.ui.main.search.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.ImageButton
import android.widget.RatingBar
import com.akv.akvandroidapprelease.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class ReviewDialog(
    context: Context,
    var stars: Int,
    var body: String,
    var interaction: ReviewDialogInteraction,
    val isUpdate: Boolean = false
): Dialog(context, R.style.CustomBasicDialog)  {

    var reviewId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setDimAmount(0.5F)
        setContentView(R.layout.dialog_review_rating)

        val body = findViewById<TextInputEditText>(R.id.dialog_review_text_et)
        val rating = findViewById<RatingBar>(R.id.dialog_review_rating_ratingbar)

        body.setText(this.body)
        rating.rating = stars.toFloat()

        findViewById<ImageButton>(R.id.dialog_review_rating_close_btn).setOnClickListener {
            interaction.onReviewCloseBtnListener()
            dismiss()
        }

        findViewById<MaterialButton>(R.id.dialog_review_rating_send_btn).setOnClickListener {
            if (checkFields(body.text.toString(), rating.rating)){
                if (isUpdate)
                    interaction.onReviewUpdateBtnListener(
                        body = body.text.toString().trim(),
                        stars = rating.rating,
                        reviewId = reviewId
                    )
                else
                    interaction.onReviewSendBtnListener(
                        body = body.text.toString().trim(),
                        stars = rating.rating
                    )
                dismiss()
            }
        }
    }

    fun checkFields(body: String, rating: Float): Boolean{
        return body.trim().isNotBlank() && rating >= 1
    }

    interface ReviewDialogInteraction{
        fun onReviewCloseBtnListener()
        fun onReviewSendBtnListener(stars: Float, body: String)
        fun onReviewUpdateBtnListener(stars: Float, body: String, reviewId: Int)
    }
}