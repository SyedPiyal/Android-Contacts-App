package com.example.chatcircle

import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatcircle.adapter.ContactsAdapter
import com.example.chatcircle.data.Contact
import android.Manifest
import android.annotation.SuppressLint
import com.example.chatcircle.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val REQUEST_CODE_READ_CONTACTS = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check and request permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_CODE_READ_CONTACTS
            )
        } else {
            // Load contacts if permission is granted
            loadContacts()
        }
    }

    // Request permission result callback
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_READ_CONTACTS && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            loadContacts()
        } else {
            Toast.makeText(this, "Permission denied to read contacts", Toast.LENGTH_SHORT).show()
        }
    }

    // Load contacts from ContentProvider
    private fun loadContacts() {
        val contacts = getContacts(this)
        val adapter = ContactsAdapter(contacts)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    // Get contacts from ContentProvider
    private fun getContacts(context: MainActivity): List<Contact> {
        val contactList = mutableListOf<Contact>()
        val contentResolver: ContentResolver = context.contentResolver

        val uri = ContactsContract.Contacts.CONTENT_URI

        val projection = arrayOf(
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY // Only the name
        )

        //---> to  get contact number and name list
//        val projection = arrayOf(
//            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
//            ContactsContract.Contacts.HAS_PHONE_NUMBER,
//            ContactsContract.Contacts._ID
//        )
//        val selection = "${ContactsContract.Contacts.HAS_PHONE_NUMBER} > 0"
//        val cursor: Cursor? = contentResolver.query(uri, projection, selection, null, null)
        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
//            val hasPhoneNumberIndex = it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
//            val contactIdIndex = it.getColumnIndex(ContactsContract.Contacts._ID)

            // to get contact number and name ------>

//            while (it.moveToNext()) {
//                val contactName = it.getString(nameIndex)
//                val contactId = it.getString(contactIdIndex)
//
//                if (it.getInt(hasPhoneNumberIndex) > 0) {
//                    val phoneNumber = getPhoneNumber(contentResolver, contactId)
//                    contactList.add(Contact(contactName, phoneNumber))
//                }
//            }

            // Check if indices are valid (>= 0) before using them
//            if (nameIndex >= 0 && hasPhoneNumberIndex >= 0 && contactIdIndex >= 0) {
//                while (it.moveToNext()) {
//                    val contactName = it.getString(nameIndex)
//                    val contactId = it.getString(contactIdIndex)
//
//                    if (it.getInt(hasPhoneNumberIndex) > 0) {
//                        val phoneNumber = getPhoneNumber(contentResolver, contactId)
//                        contactList.add(Contact(contactName, phoneNumber))
//                    }
//                }
//            } else {
//                Toast.makeText(this, "Error: Column indices are invalid", Toast.LENGTH_SHORT).show()
//            }

            // Check if the column index is valid (>= 0) before accessing it
            if (nameIndex >= 0) {
                while (it.moveToNext()) {
                    val contactName = it.getString(nameIndex)
                    contactList.add(Contact(contactName))
                }
            } else {
                Toast.makeText(this, "Error: Column index is invalid", Toast.LENGTH_SHORT).show()
            }
        }

        return contactList
    }

    // Get phone number from a contact
    @SuppressLint("Range")
    private fun getPhoneNumber(contentResolver: ContentResolver, contactId: String): String {
        val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val phoneSelection = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?"
        val phoneCursor = contentResolver.query(
            phoneUri,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            phoneSelection,
            arrayOf(contactId),
            null
        )

        phoneCursor?.use {
            if (it.moveToFirst()) {
                return it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            }
        }

        return "No Phone Number"
    }
}


