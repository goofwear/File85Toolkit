package com.goofwear.file85toolkit

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.HtmlCompat
import java.io.FileOutputStream
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private var selectedUri: Uri? = null
    private var processedData: ByteArray? = null
    private var outputFileName: String = "output.bin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val selectButton: Button = findViewById(R.id.selectButton)
        val encodeButton: Button = findViewById(R.id.encodeButton)
        val decodeButton: Button = findViewById(R.id.decodeButton)
        val saveButton: Button = findViewById(R.id.saveButton)

        selectButton.setOnClickListener { selectFile() }
        encodeButton.setOnClickListener { encodeFile() }
        decodeButton.setOnClickListener { decodeFile() }
        saveButton.setOnClickListener { saveFile() }
    }

    private fun selectFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, 100)
    }

    private fun encodeFile() {
        if (selectedUri == null) {
            showToast("Please select a file first")
            return
        }
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(selectedUri!!)
            val bytes = inputStream?.readBytes() ?: return
            val compressed = Codecs.lzmaCompress(bytes)
            val encoded = Codecs.base85Encode(compressed)
            processedData = encoded

            val name = getFileName(selectedUri!!)
            outputFileName = "$name.lzma.b85"

            showToast("File encoded successfully")
        } catch (e: Exception) {
            showToast("Error encoding: ${e.message}")
        }
    }

    private fun decodeFile() {
        if (selectedUri == null) {
            showToast("Please select a file first")
            return
        }
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(selectedUri!!)
            val bytes = inputStream?.readBytes() ?: return
            val decoded = Codecs.base85Decode(bytes)
            val decompressed = Codecs.lzmaDecompress(decoded)
            processedData = decompressed

            var name = getFileName(selectedUri!!)
            if (name.endsWith(".lzma.b85")) {
                name = name.removeSuffix(".lzma.b85")
            }
            outputFileName = name

            showToast("File decoded successfully")
        } catch (e: Exception) {
            showToast("Error decoding: ${e.message}")
        }
    }

    private fun saveFile() {
        if (processedData == null) {
            showToast("Nothing to save")
            return
        }
        // Trigger Save As dialog
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_TITLE, outputFileName)
        }
        startActivityForResult(intent, 200)
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    result = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1 && cut != null) {
                result = result?.substring(cut + 1)
            }
        }
        return result ?: "file.bin"
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                showAboutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAboutDialog() {
        val message = """
            <b>About File85Toolkit</b><br><br>
            This app compresses files with LZMA and encodes them in Base85.<br><br>
            Created by GooFWeaR<br><br>
            <b>Follow me on:</b><br>
            • <a href="https://github.com/goofwear">GitHub</a><br>
            • <a href="https://xdaforums.com/m/goofwear.2489239/">XDA</a><br>
            • <a href="https://www.reddit.com/user/goofwear/">Reddit</a><br>
            • <a href="https://keybase.io/goofwear">Keybase</a>
        """.trimIndent()

        val dialog = AlertDialog.Builder(this)
            .setMessage(HtmlCompat.fromHtml(message, HtmlCompat.FROM_HTML_MODE_LEGACY))
            .setPositiveButton("OK", null)
            .create()

        dialog.show()
        val textView: TextView? = dialog.findViewById(android.R.id.message)
        textView?.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            selectedUri = data?.data
            showToast("Selected: ${getFileName(selectedUri!!)}")
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            if (uri != null && processedData != null) {
                try {
                    contentResolver.openOutputStream(uri)?.use {
                        it.write(processedData)
                    }
                    showToast("File saved successfully")
                } catch (e: Exception) {
                    showToast("Error saving: ${e.message}")
                }
            }
        }
    }
}

