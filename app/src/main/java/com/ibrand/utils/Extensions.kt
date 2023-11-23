@file:Suppress("unused")

package com.ibrand.utils


import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Typeface
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import android.provider.Settings
import android.text.Editable
import android.text.TextPaint
import android.text.TextWatcher
import android.text.style.MetricAffectingSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.annotation.*
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.getSystemService
import androidx.core.net.toFile
import androidx.core.os.postDelayed
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.ibrand.R
import com.ibrand.databinding.DialogBaseBinding
import com.ibrand.utils.Constants.DURATION_SCROLL_MILLISECONDS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.NumberFormat
import java.util.*
import kotlin.system.exitProcess


val Activity.view: View get() = findViewById(android.R.id.content)

fun Activity.hideKeyboard() {
    view.hideKeyboard()
}

fun Activity.showSnackbar(message: String?, duration: Int = Snackbar.LENGTH_SHORT) {
    view.showSnackbar(message, duration)
}

fun Activity.showSnackbar(@StringRes stringId: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    view.showSnackbar(stringId, duration)
}


fun Any?.isIn(vararg values: Any?): Boolean {
    values.forEach { if (this == it) return true }

    return false
}

fun Any?.isNotIn(vararg values: Any?): Boolean {
    values.forEach { if (this == it) return false }

    return true
}


val BottomSheetDialogFragment.behavior get() = (requireDialog() as BottomSheetDialog).behavior

val Context.isNightModeEnabled: Boolean
    get() {
        val uiMode = resources.configuration.uiMode
        val nightMode = uiMode and Configuration.UI_MODE_NIGHT_MASK

        return nightMode == Configuration.UI_MODE_NIGHT_YES
    }

fun Context.arePermissionsGranted(vararg permissions: String): Boolean {
    permissions.forEach {
        val result = ContextCompat.checkSelfPermission(this, it)

        if (result == PackageManager.PERMISSION_DENIED) return false
    }

    return true
}

@RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, conditional = true)
fun Context.downloadFile(url: String) {
    getSystemService<DownloadManager>()?.let {
        val uri = Uri.parse(url)
        val fileName = uri.lastPathSegment
        val request = DownloadManager.Request(uri)
            .setTitle(fileName)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        it.enqueue(request)
    }
}

@WorkerThread
private fun Context.getBytes(uri: Uri) =
    contentResolver.openInputStream(uri)?.use { it.readBytes() }

@WorkerThread
fun Context.getFileNameAndSizeInBytes(uri: Uri): Pair<String, Long>? {
    var nameAndSizeInBytes: Pair<String, Long>? = null

    if (uri.scheme == "content") {
        val columns = arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE)
        contentResolver.query(uri, columns, null, null, null)?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
            if (it.moveToFirst()) {
                nameAndSizeInBytes = it.getString(nameIndex) to it.getLong(sizeIndex)
            }
        }
    } else if (uri.scheme == "file") {
        val file = uri.toFile()
        nameAndSizeInBytes = file.name to file.length()
    }

    return nameAndSizeInBytes
}

@WorkerThread
private fun Context.getMediaType(uri: Uri): String? {
    var mediaType: String? = null

    if (uri.scheme == "content") {
        mediaType = contentResolver.getType(uri)
    } else if (uri.scheme == "file") {
        val extension = uri.toFile().extension.lowercase()
        mediaType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }

    return mediaType
}

@WorkerThread
fun Context.getOwnedFile(name: String, extension: String, isCached: Boolean): File {
    val fullName = "$name.$extension"
    val directory = if (isCached) cacheDir else filesDir

    return File(directory, fullName)
}

@WorkerThread
fun Context.getOwnedFileContentUri(file: File): Uri {
    val authority = "${applicationContext.packageName}.fileprovider"

    return FileProvider.getUriForFile(this, authority, file)
}

@ColorInt
fun Context.getThemeColor(@AttrRes attributeId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attributeId, typedValue, true)

    return typedValue.data
}

@WorkerThread
fun Context.openFile(uri: Uri) {
    Intent(Intent.ACTION_VIEW, uri).apply {
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }.also {
        startActivity(Intent.createChooser(it, null))
    }
}

fun Context.restartApplication() {
    packageManager.getLaunchIntentForPackage(packageName)?.let {
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(it)
        exitProcess(0)
    }
}

@WorkerThread
fun Context.shareOwnedFile(contentUri: Uri, mediaType: String) {
    Intent(Intent.ACTION_SEND).apply {
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        putExtra(Intent.EXTRA_STREAM, contentUri)
        type = mediaType
    }.also {
        startActivity(Intent.createChooser(it, null))
    }
}

fun Context.openGoogleMapsForLatLng(latitude: String, longitude: String, address: String?) {
    Intent(
        Intent.ACTION_VIEW,
        Uri.parse(
            "geo:$latitude," + "$longitude?q=" +
                    "$latitude," +
                    "$longitude +" +
                    " (${address ?: ""})"
        )
    ).apply {
        setPackage("com.google.android.apps.maps")
        resolveActivity(packageManager)
    }.also { startActivity(it) }
}

fun Context.composeEmail(addresses: String, subject: String?) {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:$addresses") // only email apps should handle this
    intent.putExtra(Intent.EXTRA_EMAIL, addresses)
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    startActivity(intent)
}

fun Context.openDialer(phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$phoneNumber")
    startActivity(intent)
}

fun Context.openAccessibilitySettings() {
    Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).also {
        startActivity(it)
    }
}

fun Context.shareText(text: String) {
    Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }.also {
        startActivity(Intent.createChooser(it, null))
    }
}

fun Context.getDpFromDimension(@DimenRes dimenRes: Int) =
    resources.getDimension(dimenRes) / resources.displayMetrics.density


class CustomTypefaceSpan(private val typeface: Typeface?) : MetricAffectingSpan() {
    override fun updateDrawState(paint: TextPaint) {
        paint.typeface = typeface
    }

    override fun updateMeasureState(paint: TextPaint) {
        paint.typeface = typeface
    }
}


fun EditText.doAfterTextChanged(
    coroutineScope: CoroutineScope,
    debounceMs: Long,
    action: (Editable?) -> Unit,
): TextWatcher {
    var job: Job? = null

    return doAfterTextChanged {
        job?.cancel()
        job = coroutineScope.launch {
            delay(debounceMs)
            action.invoke(it)
        }
    }
}

fun EditText.showKeyboard() {
    requestFocus()

    context.getSystemService<InputMethodManager>()?.showSoftInput(this, 0)
}


fun Fragment.clearFocus() {
    activity?.currentFocus?.clearFocus()
}

fun Fragment.clearInputErrors() {
    (view as? ViewGroup)?.clearInputErrors()
}

fun Fragment.hideKeyboard() {
    view?.hideKeyboard()
}


fun Fragment.setInputEnabled(isEnabled: Boolean) {
    (view as? ViewGroup)?.setInputEnabled(isEnabled)
}

fun Fragment.showSnackbar(
    message: String?,
    duration: Int = Snackbar.LENGTH_SHORT,
    fromActivity: Boolean = false,
) {
    if (fromActivity) {
        activity?.showSnackbar(message, duration)
    } else {
        view?.showSnackbar(message, duration)
    }
}

fun Fragment.showSnackbar(
    @StringRes stringId: Int,
    duration: Int = Snackbar.LENGTH_SHORT,
    fromActivity: Boolean = false,
) {
    if (fromActivity) {
        activity?.showSnackbar(stringId, duration)
    } else {
        view?.showSnackbar(stringId, duration)
    }
}




fun <T> MutableLiveData<T>.doAndNotify(async: Boolean = false, block: T.() -> Unit) {
    val updatedValue =
        value ?: throw IllegalStateException("MutableLiveData hasn't been initialized")
    block.invoke(updatedValue)
    if (async) postValue(updatedValue) else value = updatedValue
}



fun NavController.navigateSafely(@IdRes actionId: Int, @IdRes currentDestinationId: Int) {
    if (currentDestination?.id == currentDestinationId) navigate(actionId)
}

fun NavController.navigateSafely(directions: NavDirections, @IdRes currentDestinationId: Int) {
    if (currentDestination?.id == currentDestinationId) navigate(directions)
}


val TextView.textOrEmpty get() = textOrNull ?: ""

val TextView.textOrNull get() = text?.toString()

fun TextView.textColor(@ColorRes color: Int) {
    setTextColor(ContextCompat.getColor(context, color))
}


fun View.hideKeyboard() {
    context.getSystemService<InputMethodManager>()?.hideSoftInputFromWindow(windowToken, 0)
}

fun View.setPercentHeight(percentage: Double) {
    layoutParams.height = (screenHeight * percentage).toInt()
}

fun View.setPercentWidth(percentage: Double) {
    layoutParams.width = (screenWidth * percentage).toInt()
}

fun View.showSnackbar(message: String?, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, message ?: "null", duration).show()
}

fun View.showSnackbar(@StringRes stringId: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, stringId, duration).show()
}


fun ViewGroup.clearInputErrors() {
    forEach {
        if (it is TextInputLayout) {
            it.error = null
        } else if (it is ViewGroup) {
            it.clearInputErrors()
        }
    }
}

fun ViewGroup.setInputEnabled(isEnabled: Boolean) {
    forEach {
        if (it.hasOnClickListeners()) {
            it.isClickable = isEnabled
        }

        when (it) {
            is Button, is EditText -> {
                it.isEnabled = isEnabled
            }
            is Chip -> {
                it.isCloseIconVisible = isEnabled
            }
            is ChipGroup -> {
                it.children.forEach {
                    if (it is Chip) {
                        it.isCloseIconVisible = isEnabled
                    }
                }
            }
            is ViewGroup -> {
                it.setInputEnabled(isEnabled)
            }
        }
    }
}

fun LinearLayoutCompat.setCurrentIndicator(
    index: Int,
    @DrawableRes activeIndicator: Int? = R.drawable.ic_sldier_active,
    @DrawableRes unActiveIndicator: Int? = R.drawable.ic_slider_inactive
) {
    for (item in 0 until childCount) {
        val imageView = get(item) as ImageView

        if (index == item) {
            imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    activeIndicator!!
                )
            )
        } else {
            imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    unActiveIndicator!!
                )
            )
        }
    }
}

fun LinearLayoutCompat.setupFunctionIndicator(
    size: Int,
    @DrawableRes unActiveIndicator: Int? = R.drawable.ic_slider_inactive
) {
    size.apply {
        val indicators = arrayOfNulls<ShapeableImageView>(this)
        val layoutParams: LinearLayoutCompat.LayoutParams =
            LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

        layoutParams.setMargins(8, 0, 8, 0)

        for (item in indicators.indices) {
            indicators[item] = ShapeableImageView(context)
            indicators[item]?.let {
                it.setImageDrawable(ContextCompat.getDrawable(context, unActiveIndicator!!))
                it.layoutParams = layoutParams
            }

            addView(indicators[item])
        }
    }
}

fun RecyclerView.delayedScrollToTop() {
    Handler(Looper.getMainLooper()).postDelayed(DURATION_SCROLL_MILLISECONDS) { scrollToPosition(0) }
}

fun MaterialCardView.strokeColor(color: Int) {
    strokeColor = ContextCompat.getColor(context, color)
}


fun String.formatNumber(): String {
    return (this + "")
        .replace("٬".toRegex(), ",")
        .replace("١".toRegex(), "1").replace("٢".toRegex(), "2")
        .replace("٣".toRegex(), "3").replace("٤".toRegex(), "4")
        .replace("٥".toRegex(), "5").replace("٦".toRegex(), "6")
        .replace("٧".toRegex(), "7").replace("٨".toRegex(), "8")
        .replace("٩".toRegex(), "9").replace("٠".toRegex(), "0")
}


fun <T> AutoCompleteTextView.addDataAndListener(
    items: List<T>,
    getStringFromItem: (T) -> String,
    resource: Int = android.R.layout.simple_spinner_dropdown_item,
    onItemSelectedListener: (T) -> Unit,
) {
    val adapter =
        CustomArrayAdapter(
            context,
            resource,
            items.map(getStringFromItem)
        )
    setOnClickListener {
        showDropDown()
    }
    setAdapter(adapter)
    setOnItemClickListener { parent, view, position, id ->
        adapter.getItem(position)?.let {
            onItemSelectedListener(items[position])
        }
    }
}



class CustomArrayAdapter(context: Context, resource: Int, private val items: List<String>) :
    ArrayAdapter<String>(context, resource, items) {
    private val filter = NoFilter()

    override fun getFilter(): Filter { return filter }

    inner class NoFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResult = FilterResults()
            filterResult.values = items
            filterResult.count = items.size
            return filterResult
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            notifyDataSetChanged()
        }

    }
}


fun convertPrice(price:String?):String{
    val numberFormat: NumberFormat = NumberFormat.getNumberInstance(Locale.US)
    return   numberFormat.format(price)
}




