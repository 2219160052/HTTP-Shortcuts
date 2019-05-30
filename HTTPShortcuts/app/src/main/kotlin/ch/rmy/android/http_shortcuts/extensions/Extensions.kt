package ch.rmy.android.http_shortcuts.extensions

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.utils.CrashReporting
import ch.rmy.android.http_shortcuts.utils.Destroyable
import ch.rmy.android.http_shortcuts.utils.Destroyer
import com.afollestad.materialdialogs.MaterialDialog
import io.reactivex.disposables.Disposable

fun Fragment.showMessageDialog(@StringRes stringRes: Int) {
    MaterialDialog.Builder(context!!)
        .content(stringRes)
        .positiveText(R.string.dialog_ok)
        .showIfPossible()
}

@ColorInt
fun color(context: Context, @ColorRes colorRes: Int): Int = ContextCompat.getColor(context, colorRes)

fun drawable(context: Context, @DrawableRes drawableRes: Int): Drawable? = ContextCompat.getDrawable(context, drawableRes)

fun Activity.dimen(@DimenRes dimenRes: Int) = dimen(this, dimenRes)
fun dimen(context: Context, @DimenRes dimenRes: Int) = context.resources.getDimensionPixelSize(dimenRes)

inline fun consume(f: () -> Unit): Boolean {
    f()
    return true
}

inline fun <T> T.mapIf(predicate: Boolean, block: (T) -> T): T = if (predicate) block(this) else this

inline fun <T, U> T.mapFor(iterable: Iterable<U>, block: (T, U) -> T): T {
    val iterator = iterable.iterator()
    var item = this
    while (iterator.hasNext()) {
        item = block.invoke(item, iterator.next())
    }
    return item
}

fun Any.logException(e: Throwable) {
    if (CrashReporting.enabled) {
        CrashReporting.logException(e)
    } else {
        Log.e(this.javaClass.simpleName, "An error occurred", e)
    }
}

fun Context.showToast(message: String, long: Boolean = false) {
    Toast.makeText(this, message, if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}

fun Context.showToast(@StringRes message: Int, long: Boolean = false) {
    Toast.makeText(this, message, if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}

fun <T> Array<T>.findIndex(item: T) =
    indices.firstOrNull { this[it] == item } ?: 0

fun Disposable.toDestroyable() = object : Destroyable {
    override fun destroy() {
        dispose()
    }
}

fun Disposable.attachTo(destroyer: Destroyer) {
    destroyer.own { dispose() }
}
