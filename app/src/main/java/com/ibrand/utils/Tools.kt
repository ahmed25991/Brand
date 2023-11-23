@file:Suppress("unused")

package com.ibrand.utils


import android.content.*
import android.content.res.Resources
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.Px
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.getSystemService
import androidx.databinding.ktx.BuildConfig
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.ibrand.R
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "DSGP"

fun copyToClipboard(context: Context, text: String?, @StringRes labelId: Int) {
    if (!text.isNullOrBlank()) {
        context.getSystemService<ClipboardManager>()?.let {
            val label = context.getString(labelId)
            val clipData = ClipData.newPlainText(label, text)

            it.setPrimaryClip(clipData)

            if (Handler(Looper.myLooper()!!).postDelayed({ !it.hasPrimaryClip() }, 100)) {
                showToast(context, R.string.message_copied_to_clipboard)
            }
        }
    }
}

fun <T> getResultSafely(default: T? = null, block: () -> T?) = try {
    block.invoke()
} catch (throwable: Throwable) {
    log(throwable)

    default
}

fun getDateFromString(string: String, pattern: String, locale: Locale = Locale("en"))
        : Date? = SimpleDateFormat(pattern, locale).parse(string)

fun getDipsFromPixels(context: Context, @Px pixels: Int) =
    pixels / (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)

fun getExtensionsFromMediaTypes(mediaTypes: List<String>) =
    mediaTypes.map { MimeTypeMap.getSingleton().getExtensionFromMimeType(it) }

fun getMediaTypeFromFile(file: File): String? {
    val extension = file.extension.lowercase()

    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
}

fun getMediaTypesFromExtensions(extensions: List<String>) =
    extensions.map { MimeTypeMap.getSingleton().getMimeTypeFromExtension(it) }

fun getPartFromBytes(
    bytes: ByteArray,
    fileName: String,
    mediaType: String,
    fieldName: String,
): MultipartBody.Part {
    val requestBody = bytes.toRequestBody(mediaType.toMediaTypeOrNull())

    return MultipartBody.Part.createFormData(fieldName, fileName, requestBody)
}

fun getPartFromString(string: String, fieldName: String) =
    MultipartBody.Part.createFormData(fieldName, string)


fun createPartFromString(string: String?): RequestBody {
    return string!!.toRequestBody(MultipartBody.FORM)
}


//fun getPartFromFileDetails(
//    fileDetails: FileDetails,
//    fieldName: String,
//): MultipartBody.Part = fileDetails.run {
//    MultipartBody.Part.createFormData(
//        fieldName,
//        name,
//        bytes.toRequestBody(mediaType.toMediaTypeOrNull())
//    )
//}

fun getPixelsFromDips(context: Context, dips: Int) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dips.toFloat(),
    context.resources.displayMetrics,
)

fun getRequestBodyFromString(string: String) =
    string.toRequestBody("text/plain".toMediaTypeOrNull())

fun getRequestBodiesFromString(data: Map<String, String?>): Map<String, RequestBody> =
    data.filterValues { !it.isNullOrEmpty() }.mapValues {
        getRequestBodyFromString(it.value!!)
    }

fun getStringFromDate(date: Date, pattern: String, locale: Locale = Locale("en"))
        : String = SimpleDateFormat(pattern, locale).format(date)

fun isEmailValid(email: String?) =
    !email.isNullOrBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

fun isPhoneValid(phone: String?) =
    !phone.isNullOrBlank() && phone.length <= 15 && Patterns.PHONE.matcher(phone).matches()

fun isUrlValid(url: String?) =
    !url.isNullOrBlank() && Patterns.WEB_URL.matcher(url).matches()

fun log(message: String?) {
    if (BuildConfig.DEBUG) Log.d(TAG, message ?: "null")
}

fun log(throwable: Throwable, message: String = "") {
    if (BuildConfig.DEBUG) Log.e(TAG, message, throwable)
}

fun openUrl(context: Context, url: String) = try {
    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

    true
} catch (exception: ActivityNotFoundException) {
    log(exception)

    false
}

val screenHeight get() = Resources.getSystem().displayMetrics.heightPixels

val screenWidth get() = Resources.getSystem().displayMetrics.widthPixels

fun showToast(context: Context, message: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message ?: "null", duration).show()
}

fun showToast(context: Context, @StringRes stringId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, stringId, duration).show()
}

fun transformDateString(
    source: String,
    sourcePattern: String,
    targetPattern: String,
    locale: Locale = Locale("en"),
): String {
    val sourceDate = SimpleDateFormat(sourcePattern, locale).parse(source)

    if (sourceDate != null) return SimpleDateFormat(targetPattern, locale).format(sourceDate)

    return ""
}


@JvmOverloads
fun Double.toFormattedString(format: String = "#,###.##"): String {
    return DecimalFormat(format).apply {
        isDecimalSeparatorAlwaysShown = false
//        this.minimumFractionDigits = 3
    }.format(this).formatNumber()
}


//fun setCommaSeparator(number: Int) = DecimalFormat("#,###,###").format(number).formatNumber()

// set phone number in arabic language
//fun setPhoneCode(inputLayout: TextInputLayout, isArabicCode: Boolean) {
//    inputLayout.apply {
//        if (isArabicCode) {
//            suffixText = EMIRATES_CODE_NUMBER
//        } else {
//            prefixText = EMIRATES_CODE_NUMBER
//        }
//
//        val prefixView =
//            this.findViewById<AppCompatTextView>(com.google.android.material.R.id.textinput_prefix_text)
//        prefixView.layoutParams = LinearLayout.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.MATCH_PARENT
//        )
//        prefixView.gravity = Gravity.CENTER_VERTICAL
//
//        val suffixView =
//            this.findViewById<AppCompatTextView>(com.google.android.material.R.id.textinput_suffix_text)
//        suffixView.layoutParams = LinearLayout.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.MATCH_PARENT
//        )
//        suffixView.gravity = Gravity.CENTER_VERTICAL
//    }
//}

fun goToGoogleMap(context: Context, lat: Double, lon: Double) {
    Intent(
        Intent.ACTION_VIEW,
        Uri.parse(
            "geo:${lat}," +
                    "${lon}?q=" +
                    "${lat}," +
                    "$lon +"
        )
    ).apply {
        setPackage("com.google.android.apps.maps")
        resolveActivity(context.packageManager)
    }.also {
        startActivity(context, it, null)
    }
}


fun convertDate(birthdate: String): String {
    return birthdate.substring(0, 10)
}


fun convertTime(birthdate: String): String {
    return birthdate.substring(11, 19)
}




fun convertMonthDate(birthdate: String) : String {
    val monthInNum = birthdate.substring(5,7)
    val dayInNum = birthdate.substring(8,10)
    val yearInNum = birthdate.substring(0,4)
    var monthInText = ""

    when (monthInNum) {
        "01" -> {
            monthInText = "Jan"
        }
        "02" -> {
            monthInText = "Feb"
        }
        "03" -> {
            monthInText = "Mar"
        }
        "04" -> {
            monthInText = "April"
        }
        "05" -> {
            monthInText = "May"
        }
        "06" -> {
            monthInText = "June"
        }
        "07" -> {
            monthInText = "July"
        }
        "08" -> {
            monthInText = "Aug"
        }
        "09" -> {
            monthInText = "Sept"
        }
        "10" -> {
            monthInText = "Oct"
        }
        "11" -> {
            monthInText = "Nov"
        }
        "12" -> {
            monthInText = "Dec"
        }
    }


    return "$dayInNum $monthInText $yearInNum"
}


fun View.setSafeOnClickListener(timeSpan: Long = 500L, listener: (View) -> Unit) {
    var lastClickTime = 0L
    setOnClickListener { findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= timeSpan) {
            lastClickTime = currentTime
            listener(it)
          }
       }
    }
 }



