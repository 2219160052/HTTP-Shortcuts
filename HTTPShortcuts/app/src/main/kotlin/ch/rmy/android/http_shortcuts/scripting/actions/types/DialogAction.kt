package ch.rmy.android.http_shortcuts.scripting.actions.types

import android.content.Context
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.activities.ExecuteActivity
import ch.rmy.android.http_shortcuts.dialogs.DialogBuilder
import ch.rmy.android.http_shortcuts.http.ErrorResponse
import ch.rmy.android.http_shortcuts.http.ShortcutResponse
import ch.rmy.android.http_shortcuts.utils.HTMLUtil
import ch.rmy.android.http_shortcuts.variables.VariableManager
import ch.rmy.android.http_shortcuts.variables.Variables
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers

class DialogAction(
    actionType: DialogActionType,
    data: Map<String, String>
) : BaseAction(actionType) {

    private val message: String = data[KEY_TEXT] ?: ""

    private val title: String = data[KEY_TITLE] ?: ""

    override fun perform(context: Context, shortcutId: String, variableManager: VariableManager, response: ShortcutResponse?, responseError: ErrorResponse?, recursionDepth: Int): Completable {
        val finalMessage = Variables.rawPlaceholdersToResolvedValues(message, variableManager.getVariableValuesByIds())
        return if (finalMessage.isNotEmpty()) {
            Completable
                .create { emitter ->
                    (context as ExecuteActivity).runOnUiThread {
                        DialogBuilder(context)
                            .title(title)
                            .message(HTMLUtil.format(finalMessage))
                            .positive(R.string.dialog_ok)
                            .dismissListener { emitter.onComplete() }
                            .show()
                    }
                }
                .subscribeOn(AndroidSchedulers.mainThread())
        } else {
            Completable.complete()
        }
    }

    companion object {

        const val KEY_TEXT = "text"
        const val KEY_TITLE = "title"

    }

}