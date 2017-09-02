package ch.rmy.android.http_shortcuts.dialogs

import android.content.Context
import android.support.annotation.StringRes
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.utils.HTMLUtil
import com.afollestad.materialdialogs.MaterialDialog

class HelpDialogBuilder(context: Context) {

    private val builder: MaterialDialog.Builder

    private val view: View

    init {
        val layoutInflater = LayoutInflater.from(context)
        view = layoutInflater.inflate(R.layout.help_dialog, null)

        builder = MaterialDialog.Builder(context)
                .customView(view, false)
                .positiveText(android.R.string.ok)
    }

    fun title(@StringRes title: Int): HelpDialogBuilder {
        builder.title(title)
        return this
    }

    fun message(@StringRes message: Int): HelpDialogBuilder {
        val textView = view.findViewById(R.id.help_text) as TextView
        textView.text = HTMLUtil.getHTML(view.context, message)
        textView.movementMethod = LinkMovementMethod.getInstance()
        return this
    }

    fun build() = HelpDialog(builder.build())

}
