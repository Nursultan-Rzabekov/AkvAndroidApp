package com.example.akvandroidapp.ui.main.messages.chatkit

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.akvandroidapp.BuildConfig
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.UserConversationsResponse
import com.example.akvandroidapp.ui.*
import com.example.akvandroidapp.ui.main.messages.ModalBottomSheetChat
import com.example.akvandroidapp.ui.main.messages.detailState.DetailsStateEvent
import com.example.akvandroidapp.ui.main.messages.detailState.DetailsViewModel
import com.example.akvandroidapp.ui.main.messages.detailState.DetailsViewState
import com.example.akvandroidapp.ui.main.messages.models.MessageDocument
import com.example.akvandroidapp.ui.main.messages.models.MessagePhoto
import com.example.akvandroidapp.ui.main.messages.models.MessageText
import com.example.akvandroidapp.ui.main.messages.models.mMessage
import com.example.akvandroidapp.ui.main.search.viewmodel.*
import com.example.akvandroidapp.util.Constants
import com.example.akvandroidapp.util.Constants.Companion.TOTAL_MESSAGES_COUNT
import com.example.akvandroidapp.util.Converters
import com.example.akvandroidapp.util.DateUtils
import com.example.akvandroidapp.util.ErrorHandling
import com.example.akvandroidapp.viewmodels.ViewModelProviderFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.messages.MessageHolders
import com.stfalcon.chatkit.messages.MessageInput
import com.stfalcon.chatkit.messages.MessageInput.AttachmentsListener
import com.stfalcon.chatkit.messages.MessagesList
import com.stfalcon.chatkit.messages.MessagesListAdapter
import com.stfalcon.chatkit.messages.MessagesListAdapter.OnMessageLongClickListener
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import handleIncomingBlogListData
import kotlinx.android.synthetic.main.activity_custom_layout_messages.*
import loadFirstPage
import nextPage
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import setImageMultipart
import setMessageBody
import setUserId
import java.io.File
import java.io.IOException
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashSet
import kotlin.properties.Delegates

class CustomLayoutMessagesActivity : BaseActivity(),
    OnMessageLongClickListener<mMessage?>,
    MessagesListAdapter.SelectionListener, MessagesListAdapter.OnLoadMoreListener,
    MessageInput.InputListener, AttachmentsListener , ModalBottomSheetChat.BottomSheetDialogChatInteraction{

    private var senderId = "1"

    private var imageLoader: ImageLoader? = null
    private var messagesAdapter: MessagesListAdapter<mMessage>? = null
    private var menu: Menu? = null
    private var selectionCount = 0

    lateinit var stateChangeListener: DataStateChangeListener
    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel: DetailsViewModel

    private var messagesList: MessagesList? = null
    private var targetPic :String? = null

    private lateinit var currentPhotoPath: String
    private lateinit var currentPhotoUri: Uri
    private lateinit var currentFileUri: Uri
    private val myDataTransfer = arrayOf<Bundle?>(null)
    private val calendar = Calendar.getInstance()
    private val modalBottomSheet: ModalBottomSheetChat = ModalBottomSheetChat(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_layout_messages)

        viewModel = ViewModelProvider(this, providerFactory).get(DetailsViewModel::class.java)
        stateChangeListener = this

        ChatClient(URI("ws://akv.kz/ws?token=${sessionManager.cachedToken.value?.token.toString()}")).connect()

        imageLoader =
            ImageLoader { imageView: ImageView?, url: String?, payload: Any? ->
                Glide.with(this@CustomLayoutMessagesActivity).load(url).error(R.drawable.test_image_back).into(imageView!!)
            }

        messagesList = findViewById(R.id.messagesList)
        val input = findViewById<MessageInput>(R.id.input)
        input.setInputListener(this)
        input.setAttachmentsListener(this)

        val targetName =  intent.getStringExtra("name")
        val targetId =  intent.getIntExtra("id",1)
        val targetImage =  intent.getStringExtra("image")

        senderId = targetId.toString()

        setToolbar()

        header_custom_dialog_nickname_tv.text = targetName
        targetPic = targetImage

        Glide.with(this)
            .load(targetImage)
            .error(R.drawable.profile_default_avavatar)
            .into(header_custom_dialog_civ)

        initAdapter()
        subscribeObservers()

        viewModel.setQuery(targetId).let {
            onBlogSearchOrFilter()
        }
    }

    private fun initAdapter() {
        val holdersConfig = MessageHolders()
            .setIncomingTextLayout(R.layout.item_custom_incoming_text_message)
            .setOutcomingTextLayout(R.layout.item_custom_outcoming_text_message)
            .setIncomingImageLayout(R.layout.item_custom_incoming_image_message)
            .setOutcomingImageLayout(R.layout.item_custom_outcoming_image_message)
        messagesAdapter =
            MessagesListAdapter(
                senderId,
                holdersConfig,
                imageLoader
            )

        messagesAdapter?.setOnMessageLongClickListener(this)
        messagesAdapter?.setLoadMoreListener(this)
        messagesList!!.setAdapter(messagesAdapter)
    }

    private fun setToolbar(){
        header_dialog_messenger_toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_back)

        header_dialog_messenger_toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private  fun resetUI(){
        stateChangeListener.hideSoftKeyboard()
    }

    private fun onBlogSearchOrFilter(){
        viewModel.loadFirstPage().let {
            resetUI()
        }
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(this, androidx.lifecycle.Observer{ dataState ->
            if(dataState != null) {
                handlePagination(dataState)
                stateChangeListener.onDataStateChange(dataState)
            }
        })

        viewModel.viewState.observe(this, androidx.lifecycle.Observer{ viewState ->
            if(viewState != null){
                Log.d(TAG, "send message 123 : page: ${viewState.sendMessageFields.messageBody}")
                if(viewState.sendMessageFields.sended){
                    Log.d(TAG, "send message lll : page: ${viewState.sendMessageFields.messageBody}")
                    // onBlogSearchOrFilter()
                    Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "send message send : page: ${viewModel.getPage()} size: ${viewState.myChatFields.blogList.size}")
                    viewModel.setSendedState(false)
                }
                else{
                    if(viewState.myChatFields.blogList.isNotEmpty()){
                        //Log.d(TAG, "send message vvvvvqqq: page: ${viewState.myChatFields.blogList}")
                        val set =  mutableListOf<UserConversationsResponse>()
                        if(viewState.myChatFields.blogList.isNotEmpty()){
                            viewState.myChatFields.blogList.forEach {
                                set.add(UserConversationsResponse(
                                    id = it.id,
                                    body = it.body.toString(),
                                    userName = it.userName.toString(),
                                    userId = it.userId,
                                    userEmail = it.userEmail,
                                    userPic = it.userPic,
                                    recipientName = it.recipientName.toString(),
                                    recipientId = it.recipientId,
                                    recipientEmail = it.recipientEmail,
                                    recipientPic = it.recipientPic,
                                    created_at = it.created_at.toString(),
                                    updated_at = it.updated_at.toString(),
                                    images = it.image
                                ))
                            }
                        }
                        Log.d(TAG, "send message hahaha : page: ${viewModel.getPage()} size: ${set.size}")
                        setMessageHistory(set.toList())
                        viewModel.setBlogListData(listOf())
                    }
                }
            }
        })
    }

    private fun handlePagination(dataState: DataState<DetailsViewState>){
        dataState.data?.let {
            it.data?.let{
                it.getContentIfNotHandled()?.let{
                    viewModel.handleIncomingBlogListData(it)
                }
            }
        }
        dataState.error?.let{ event ->
            event.peekContent().response.message?.let{
                if(ErrorHandling.isPaginationDone(it)){

                    // handle the error message event so it doesn't display in UI
                    event.getContentIfNotHandled()

                    // set query exhausted to update RecyclerView with
                    // "No more results..." list item
                    viewModel.setQueryExhausted(true)
                }
            }
        }
    }

    private fun setMessageHistory(userConversationResponse: List<UserConversationsResponse>) {
        val messages = mutableListOf<mMessage>()
        userConversationResponse.forEach {
            if(it.images!=null){
                messages.add(
                    getMessageWithType(
                        userId = it.userId!!,
                        type = Constants.MESSAGE_TYPE_PHOTO,
                        imageUrl = "${Constants.BASE_URL_IMAGE}${it.images}",
                        createdAt = it.created_at.toString(),
                        user = User(it.recipientId.toString(),
                            it.recipientName.toString(),
                            targetPic.toString(),
                            true
                        )
                    )
                )
            }
            else{
                messages.add(
                    getMessageWithType(
                        userId = it.userId!!,
                        type = Constants.MESSAGE_TYPE_TEXT,
                        body = it.body,
                        createdAt = it.created_at.toString(),
                        user = User(it.recipientId.toString(),
                            it.recipientName.toString(),
                            targetPic.toString(),
                            true
                        )
                    )
                )
            }
        }
        messagesAdapter?.addToEnd(messages, false)
    }

    private fun getMessageWithType(userId: Int, type: Int,
                                   body: String = "", uriOfFile: Uri? = null,
                                   user: User,
                                   imageUrl:String? = null, fileName: String = "", fileSize: Long = 0,
                                   createdAt: String): mMessage{
        when(type){
            Constants.MESSAGE_TYPE_TEXT -> {
                return MessageText(
                    userId = userId,
                    body = body,
                    created_at = DateUtils.convertLongStringToDate(createdAt),
                    user = user
                )

            }
            Constants.MESSAGE_TYPE_PHOTO -> {
                return MessagePhoto(
                    userId = userId,
                    photo = uriOfFile,
                    user = user,
                    image = imageUrl,
                    created_at = DateUtils.convertLongStringToDate(createdAt)
                )
            }
            Constants.MESSAGE_TYPE_DOC -> {
                return MessageDocument(
                    userId = userId,
                    fileName = fileName,
                    fileSize = Converters.humanReadableByteCountSI(fileSize),
                    created_at = DateUtils.convertLongStringToDate(createdAt)
                )
            }
            else -> {
                return MessageText(userId, body, DateUtils.convertLongStringToDate(createdAt), user = user)
            }
        }
    }

    override fun onSubmit(input: CharSequence): Boolean {
        viewModel.setMessageBody(input.toString())
        viewModel.setUserId(viewModel.getTargetQuery())
        viewModel.setStateEvent(DetailsStateEvent.SendMessageEvent())
        return true
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

    override fun onAddAttachments() {
        showDialog()
    }

    override fun onLoadMore(page: Int, totalItemsCount: Int) {
        Log.i("TAG", "onLoadMore: $page $totalItemsCount")

        loadMessages()
    }

    private fun loadMessages() {
        viewModel.nextPage()

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
                            startActivityForResult(takePictureIntent,
                                Constants.REQUEST_IMAGE_CAPTURE
                            )
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
                    Constants.PICK_FILE_CODE
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
        myDataTransfer[Constants.GALLERY_REQUEST_CODE] = myData

        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(Intent.createChooser(intent, "Choose photos"),
            Constants.GALLERY_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                Constants.REQUEST_IMAGE_CAPTURE -> {
                    try {
                        messagesAdapter?.addToStart(getMessageWithType(
                            senderId.toInt(),
                            imageUrl = currentPhotoUri.toString(),
                            user = User(senderId,"qwew","qwe",true),
                            type = Constants.MESSAGE_TYPE_PHOTO,
                            uriOfFile = currentPhotoUri,
                            createdAt = calendar.time.toString()),true)


                    }catch (ex: Exception){
                        showErrorDialog(ErrorHandling.ERROR_SOMETHING_WRONG_WITH_IMAGE)
                    }
                    Log.e("MESSAGE_CAMERA_URI", "$currentPhotoUri")
                }

                Constants.GALLERY_REQUEST_CODE -> {
                    data?.data?.let { uri ->
                        launchImageCrop(uri)
                    }?: showErrorDialog(ErrorHandling.ERROR_SOMETHING_WRONG_WITH_IMAGE)
                    //temporary variable
                }

                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    Log.d(TAG, "CROP: CROP_IMAGE_ACTIVITY_REQUEST_CODE")
                    val result = CropImage.getActivityResult(data)
                    val resultUri = result.uri
                    Log.d(TAG, "CROP: CROP_IMAGE_ACTIVITY_REQUEST_CODE: uri: ${resultUri}")

                    currentPhotoUri = resultUri

                    messagesAdapter?.addToStart(getMessageWithType(
                        senderId.toInt(),
                        imageUrl = resultUri.toString(),
                        user = User(senderId,"qwew","qwe",true),
                        type = Constants.MESSAGE_TYPE_PHOTO,
                        uriOfFile = resultUri,
                        createdAt = calendar.time.toString()), true)

                    currentPhotoUri.path?.let { filePath->
                        val imageFile = File(filePath)
                        Log.d(TAG, "CreateBlogFragment, imageFile: file: ${imageFile}")

                        val requestBody =
                            RequestBody.create(
                                MediaType.parse("multipart/form-data"),
                                imageFile
                            )
                        Log.d(TAG, "PostCreateHouse request777: ${requestBody}")

                        val multipartBody = MultipartBody.Part.createFormData(
                            "images",
                            imageFile.name,
                            requestBody
                        )
                        multipartBody.let {
                            viewModel.setMessageBody("Only Photos")
                            viewModel.setImageMultipart(it)
                            viewModel.setUserId(viewModel.getTargetQuery())
                            viewModel.setStateEvent(DetailsStateEvent.SendMessageEvent())
                        }
                    }

                    Log.e("MESSAGE_GALLEY_URI", currentPhotoUri.toString())
                }

                CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {
                    Log.d(TAG, "CROP: ERROR")
                    showErrorDialog(ErrorHandling.ERROR_SOMETHING_WRONG_WITH_IMAGE)
                }

                Constants.PICK_FILE_CODE -> {
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
                                messagesAdapter?.addToStart(getMessageWithType(
                                    1,
                                    Constants.MESSAGE_TYPE_DOC,
                                    fileName = fileName,
                                    fileSize = fileSize,
                                    createdAt = calendar.time.toString(),
                                    user = User(senderId,"Nurs","qweq",true)), true)
                            }
                    }?: showErrorDialog(ErrorHandling.ERROR_SOMETHING_WRONG_WITH_FILE)
                    Log.e("MESSAGE_DOCUMENT_URI", currentFileUri.toString())
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED)
            Log.e("MESSAGE_INTENT_CANCELED", "cancelled")
    }

    private fun launchImageCrop(uri: Uri){
        CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this)
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

    private fun showDialog(){
        modalBottomSheet.show(supportFragmentManager, ModalBottomSheetChat.TAG)
    }

    override fun onMessageLongClick(message: mMessage?) {

    }

    override fun displayProgressBar(bool: Boolean) {}
    override fun expandAppBar() {}

    override fun onBackPressed() {
        if (selectionCount == 0) {
            super.onBackPressed()
        } else {
            messagesAdapter!!.unselectAllItems()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.chat_actions_menu, menu)
        onSelectionChanged(0)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> messagesAdapter!!.deleteSelectedMessages()
            R.id.action_copy -> {
                messagesAdapter!!.copySelectedMessagesText(this, messageStringFormatter, true)
                Toast.makeText(
                    this,
                    R.string.copied_message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return true
    }

    override fun onSelectionChanged(count: Int) {
        selectionCount = count
        menu!!.findItem(R.id.action_delete).isVisible = count > 0
        menu!!.findItem(R.id.action_copy).isVisible = count > 0
    }


    inner class ChatClient(url: URI): WebSocketClient(url) {
        override fun onOpen(handshakedata: ServerHandshake?) {
            Log.d("ChatClient", "ChatClient Connected")
        }

        override fun onMessage(message: String) = this@CustomLayoutMessagesActivity.runOnUiThread {
            Log.e("nursultan","^^^^^^^^^^^^^^^^hahah $message")

            val json = ObjectMapper().readTree(message)
            val body = json.get("body").asText()
            val id = json.get("id").asText()
            val created_at = json.get("created_at").asText()
            val updated_at = json.get("updated_at").asText()
            val images = json.get("images")
            val userId = json.get("user").get("id").asText()
            val userName = json.get("user").get("email").asText()

            val recipientId = json.get("recipient").get("id").asText()
            val recipientName = json.get("recipient").get("email").asText()

            Log.e("nursultan","$$$$$$ userJson ${userName}")
            Log.e("nursultan","$$$$$$ created at ${created_at}")
            println("json msg ${message}")

            if(images == null){
                messagesAdapter?.addToStart(getMessageWithType(
                    userId = userId.toInt(),
                    type = Constants.MESSAGE_TYPE_PHOTO,
                    imageUrl = "${Constants.BASE_URL_IMAGE}${images}",
                    createdAt = created_at,
                    user = User(
                        recipientId.toString(),
                        recipientName.toString(),
                        targetPic.toString(),
                        true)
                ),true)
            }
            else{
                messagesAdapter?.addToStart(getMessageWithType(
                    userId = userId.toInt(),
                    type = Constants.MESSAGE_TYPE_TEXT,
                    body = body,
                    createdAt = created_at,
                    user = User(
                        recipientId.toString(),
                        recipientName.toString(),
                        targetPic.toString(),
                        true)
                ), true)
            }
        }

        override fun onClose(code: Int, reason: String?, remote: Boolean) {
            Log.d("ChatClient", "ChatClient Disconnected")
        }

        override fun onError(ex: java.lang.Exception) {
            ex.printStackTrace()
        }

    }

    private val messageStringFormatter: MessagesListAdapter.Formatter<mMessage>
        get() = MessagesListAdapter.Formatter { message: mMessage ->
            val createdAt: String = SimpleDateFormat(
                "MMM d, EEE 'at' h:mm a",
                Locale.getDefault()
            )
                .format(message.createdAt)
            var text: String? = message.text
            if (text == null) text = "[attachment]"
            String.format(
                Locale.getDefault(), "%s: %s (%s)",
                message.user.name, text, createdAt
            )
        }
}