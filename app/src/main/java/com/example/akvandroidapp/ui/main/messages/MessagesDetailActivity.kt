package com.example.akvandroidapp.ui.main.messages

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.akvandroidapp.BuildConfig
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.*
import com.example.akvandroidapp.ui.main.messages.adapter.ChatRecyclerAdapter
import com.example.akvandroidapp.ui.main.messages.models.Message
import com.example.akvandroidapp.util.Constants
import com.example.akvandroidapp.util.Constants.Companion.GALLERY_REQUEST_CODE
import com.example.akvandroidapp.util.Constants.Companion.PICK_FILE_CODE
import com.example.akvandroidapp.util.Constants.Companion.REQUEST_IMAGE_CAPTURE
import com.example.akvandroidapp.util.Converters
import com.example.akvandroidapp.util.ErrorHandling
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_dialog.*
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_support_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MessagesDetailActivity : BaseActivity(), ModalBottomSheetChat.BottomSheetDialogChatInteraction,
    SwipeRefreshLayout.OnRefreshListener
{

    private val mUserId: String = "123"
    private val myDataTransfer = arrayOf<Bundle?>(null)
    private lateinit var currentPhotoPath: String
    private lateinit var currentPhotoUri: Uri
    private lateinit var currentFileUri: Uri

    private lateinit var chatAdapter: ChatRecyclerAdapter
    private val modalBottomSheet: ModalBottomSheetChat = ModalBottomSheetChat(this)

    override fun displayProgressBar(bool: Boolean) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_layout)

        swipe_messages.setOnRefreshListener(this)

        initRecyclerView()
        mookDate()

        activity_dialog_attach_btn.setOnClickListener {
            showDialog()
        }

        main_back_img_btn.setOnClickListener {
            finish()
        }

        activity_dialog_send_btn.setOnClickListener {
            sendMessage()
        }
    }

    override fun expandAppBar() {
    }

    private fun initRecyclerView(){
        activity_dialog_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MessagesDetailActivity).apply { stackFromEnd = true }
            chatAdapter = ChatRecyclerAdapter(requestManager, mUserId)
            adapter = chatAdapter
        }
    }

    private fun mookDate(){
        chatAdapter.addMessage(Message("124", "asdsd"))
        chatAdapter.addMessage(Message("123", "asdsd"))
        chatAdapter.addMessage(Message("123", "asdsd"))
        chatAdapter.addMessage(Message("124", "asdsd"))
        chatAdapter.addMessage(Message("123", "asdsd"))
        //chatAdapter.addMessage(Message("123", "", Constants.MESSAGE_TYPE_PHOTO, Uri.parse("content://media/external/images/media/24437")))
    }

    private fun sendMessage(){
        val message = activity_dialog_message_et.text.toString()
        if (message.trim() != ""){
            chatAdapter.addMessage(
                Message(mUserId, message)
            )
        }
        activity_dialog_message_et.setText("")
        activity_dialog_recycler_view.smoothScrollToPosition(chatAdapter.itemCount)
    }

    private fun showDialog(){
        modalBottomSheet.show(supportFragmentManager, ModalBottomSheetChat.TAG)
    }

    override fun onCameraClicked() {
        if(isStoragePermissionGranted())
            dispatchTakePictureIntent()
        if (modalBottomSheet.isVisible)
            modalBottomSheet.dismiss()
    }

    override fun onPhotoClicked() {
        if (isStoragePermissionGranted())
            pickFromGallery()
        if (modalBottomSheet.isVisible)
            modalBottomSheet.dismiss()
    }

    override fun onDocumentClicked() {
        if (isStoragePermissionGranted())
            dispatchTakeFileIntent()
        if (modalBottomSheet.isVisible)
            modalBottomSheet.dismiss()
    }

    override fun onCancelClicked() {
        if (modalBottomSheet.isVisible)
            modalBottomSheet.dismiss()
    }

    override fun onRefresh() {
        swipe_messages.isRefreshing = false
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)
                    ?.also {

                        val photoFile: File? = try{
                            createImageFile()
                        } catch (ex: IOException){
                            null
                        }

                        photoFile?.also {
                            val photoUri: Uri = FileProvider.getUriForFile(
                                this,
                                BuildConfig.APPLICATION_ID + ".fileprovider",
                                it
                            )
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                            currentPhotoUri = photoUri
                            Log.e("MESSAGE_CAMERA_BITMAP", "$photoUri")
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                        }
                    }
            }
    }

    private fun dispatchTakeFileIntent() {
        Intent(Intent.ACTION_GET_CONTENT)
            .apply {
                type = "*/*"
            }.also {
                startActivityForResult(
                    Intent.createChooser(it, "Choose a file"),
                    PICK_FILE_CODE
                )
            }
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

        val myData = Bundle()
        myData.putString("where", "RED")
        myDataTransfer[GALLERY_REQUEST_CODE] = myData

        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(Intent.createChooser(intent, "Choose photos"), GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_IMAGE_CAPTURE -> {
                    try {
                        addMessageWithType(
                            Constants.MESSAGE_TYPE_PHOTO,
                            uriOfFile = currentPhotoUri)
                    }catch (ex: Exception){
                        showErrorDialog(ErrorHandling.ERROR_SOMETHING_WRONG_WITH_IMAGE)
                    }
                    Log.e("MESSAGE_CAMERA_URI", "$currentPhotoUri")
                }

                GALLERY_REQUEST_CODE -> {
                    data?.data?.let { uri ->
                        currentPhotoUri = uri
                        addMessageWithType(
                            Constants.MESSAGE_TYPE_PHOTO,
                            uriOfFile = uri)
                    }?: showErrorDialog(ErrorHandling.ERROR_SOMETHING_WRONG_WITH_IMAGE)
                    //temporary variable
                    val myData = myDataTransfer[requestCode]

                    Log.e("MESSAGE_GALLEY_URI", currentPhotoUri.toString())
                }

                PICK_FILE_CODE -> {
                    var fileName: String
                    var fileSize: Long
                    data?.data?.let {
                        currentFileUri = it
                        contentResolver
                            .query(it, null, null, null, null)
                            ?.use { cursor ->
                                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                                cursor.moveToFirst()
                                fileName = cursor.getString(nameIndex)
                                fileSize = cursor.getLong(sizeIndex)
                                addMessageWithType(
                                    Constants.MESSAGE_TYPE_DOC,
                                    fileName = fileName,
                                    fileSize = fileSize)
                        }
                    }?: showErrorDialog(ErrorHandling.ERROR_SOMETHING_WRONG_WITH_FILE)
                    Log.e("MESSAGE_DOCUMENT_URI", currentFileUri.toString())
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED)
            Log.e("MESSAGE_INTENT_CANCELED", "cancelled")
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    fun showErrorDialog(errorMessage: String){
        onDataStateChange(
            DataState(
                Event(StateError(Response(errorMessage, ResponseType.Dialog()))),
                Loading(isLoading = false),
                Data(Event.dataEvent(null), null)
            )
        )
    }

    fun addMessageWithType(type: Int, body: String = "", uriOfFile: Uri? = null, fileName: String = "", fileSize: Long = 0){
        when( type ){
            Constants.MESSAGE_TYPE_TEXT -> {
                chatAdapter.addMessage(
                    Message(mUserId, body, type)
                )
            }
            Constants.MESSAGE_TYPE_PHOTO -> {
                chatAdapter.addMessage(
                    Message(mUserId, "", type, uriOfFile)
                )
            }
            Constants.MESSAGE_TYPE_DOC -> {
                chatAdapter.addMessage(
                    Message(mUserId, "", type,
                        fileName = fileName,
                        fileSize = Converters.humanReadableByteCountSI(fileSize))
                )
            }
        }
    }

}
