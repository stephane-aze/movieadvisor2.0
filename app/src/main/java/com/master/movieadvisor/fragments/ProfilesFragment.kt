package com.master.movieadvisor.fragments

import android.Manifest.permission.*
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.master.movieadvisor.HomeActivity
import com.master.movieadvisor.R
import com.master.movieadvisor.databinding.ProfilesViewBinding
import com.master.movieadvisor.ui.toEditable
import kotlinx.android.synthetic.main.edit_profile_dialog.view.*
import kotlinx.android.synthetic.main.profiles_view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


private const val REQUEST_IMAGE_CAPTURE =100
private const val REQUEST_TAKE_PHOTO = 1
private const val PICK_IMAGE_REQUEST = 71
private const val PERMISSION_CODE = 1000
private const val IMAGE_CAPTURE_CODE = 1001
private const val REQUEST_CODE_GALLERY=2
private const val WRITE_EXT_STORAGE_REQUEST_CODE = 123

class ProfilesFragment: Fragment() {
    private lateinit var packageManager: PackageManager
    private lateinit var currentPhotoPath: String
    private lateinit var contextProfiles: Context
    private lateinit var binding: ProfilesViewBinding
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private var imageUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profiles_view, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProfilesViewBinding.bind(view)
        init()
        setDataOnView()
        binding.uploadImage.setOnClickListener {
            selectImage()
        }
        binding.btnEditProfile.setOnClickListener {
            editProfile()
        }
        binding.signOut.setOnClickListener { logout() }

    }
    private fun init(){
        auth = Firebase.auth
        contextProfiles = context!!
        packageManager = contextProfiles.packageManager
    }
    private fun setDataOnView() {
        currentUser = auth.currentUser
        var mPhotoUrl:String? = null
        currentUser?.run {
            // Name, email address, and profile photo Url

            mPhotoUrl = photoUrl.toString().plus("?height=500")
            binding.profileName.text = displayName
            binding.profileEmail.text = email
        }
        Glide.with(this)
            .load(mPhotoUrl)
            .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
            .circleCrop()
            .into(binding.profileImage)

    }

    private fun editProfile() {
        showDialog()

    }

    private fun showDialog() {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.edit_profile_dialog,null)
        val editTextProfilesSex: EditText = view.findViewById(R.id.editTextProfilesSex)
        val editTextProfilesName: EditText= view.findViewById(R.id.editTextProfilesName)
        val editTextProfilesEmail: EditText = view.findViewById(R.id.editTextProfilesEmail)

        val builder = AlertDialog.Builder(contextProfiles)
            .setView(view)
            .setTitle("Modification")

        val  mAlertDialog = builder.show()
        view.btn_send_edit_user.setOnClickListener {
            val sex = editTextProfilesSex.text.toString()
            val name = editTextProfilesName.text.toString()
            val email = editTextProfilesEmail.text.toString()
            val profileUpdates = userProfileChangeRequest {
                displayName=name
            }
            currentUser?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User profile updated.")
                    }
                }
            mAlertDialog.dismiss()

        }
        view.btn_cancel.setOnClickListener {
            mAlertDialog.dismiss()
        }

    }


    private fun selectImage() {
        val options =
            arrayOf<CharSequence>("Prendre une photo", "Choisir dans la gallerie", "Annulé")
        val builder =AlertDialog.Builder(context)
        builder.setTitle("Ajouter une Photo!")
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Prendre une photo" -> {
                    if(hasCamera()){
                        managePermission()

                    }
                }
                options[item] == "Choisir dans la gallerie" -> {
                   val intent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )

                    startActivityForResult(intent, REQUEST_CODE_GALLERY)

                }
                options[item] == "Annulé" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }
    private fun hasCamera():Boolean= packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)

    private fun managePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(contextProfiles,CAMERA)
                == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(contextProfiles,WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED){
                //permission was not enabled
                val permission = arrayOf(CAMERA,WRITE_EXTERNAL_STORAGE)
                //show popup to request permission
                requestPermissions(permission, PERMISSION_CODE)
            }
            else{
                //permission already granted
                dispatchTakePictureIntent()
            }
        }
        else{
            //system os is < marshmallow
            dispatchTakePictureIntent()
        }
    }

    private fun dispatchTakePictureIntent() {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()

                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.d("Error take photo",ex.toString())
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        contextProfiles,
                        "com.master.movieadvisor.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        Log.d("hello","Hey!!!!")
        // Create an image file name
        val timeStamp: String = SimpleDateFormat(getString(R.string.simple_date_format)).format(Date())
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            contextProfiles.getExternalFilesDir(Environment.DIRECTORY_PICTURES) /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            val file = File(currentPhotoPath)
            Glide.with(this)
                .load(Uri.fromFile(file))
                .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                .circleCrop()
                .into(binding.profileImage)
            galleryAddPic()
            val profileUpdates = userProfileChangeRequest {
                photoUri = Uri.fromFile(file)
            }
            currentUser?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User profile updated.")
                    }
                }

        }
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            val uri=data?.data
            Glide.with(this)
                .load(uri)
                .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                .circleCrop()
                .into(binding.profileImage)
            val profileUpdates = userProfileChangeRequest {
                photoUri = uri
            }
            currentUser?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User profile updated.")
                    }
                }

        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED){
                    //permission from popup was granted
                    dispatchTakePictureIntent()
                }
                else{
                    //permission from popup was denied
                    Toast.makeText(contextProfiles, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            contextProfiles.sendBroadcast(mediaScanIntent)
        }
    }
    private fun logout() {

        auth.signOut()
        val intent = Intent(context, HomeActivity::class.java)
        startActivity(intent)
        LoginManager.getInstance().logOut()

    }

}