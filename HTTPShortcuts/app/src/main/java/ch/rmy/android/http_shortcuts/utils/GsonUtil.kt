package ch.rmy.android.http_shortcuts.utils

import ch.rmy.android.http_shortcuts.realm.models.Base
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import io.realm.RealmObject
import java.io.Reader
import java.util.*

object GsonUtil {

    fun prettyPrint(jsonString: String): String {
        return try {
            val parser = JsonParser()
            val json = parser.parse(jsonString).asJsonObject
            val gson = GsonBuilder().setPrettyPrinting().create()
            gson.toJson(json)
        } catch (e: JsonParseException) {
            jsonString
        }
    }

    fun toJson(item: RealmObject): String {
        val gson = jsonBuilder.create()
        return gson.toJson(item)
    }

    fun <T : RealmObject> fromJson(json: String, clazz: Class<T>): T {
        val gson = jsonBuilder.create()
        return gson.fromJson(json, clazz)
    }

    fun exportData(base: Base, writer: Appendable) {
        val gson = jsonBuilder.setPrettyPrinting().create()
        gson.toJson(base, writer)
    }

    fun exportData(base: Base): String {
        val gson = jsonBuilder.setPrettyPrinting().create()
        return gson.toJson(base)
    }

    fun importData(reader: Reader): Base {
        val gson = jsonBuilder.create()
        return gson.fromJson(reader, Base::class.java)
    }

    fun <T> fromJsonString(jsonString: String?): Map<String, T> {
        if (jsonString == null) {
            return Collections.emptyMap()
        }
        val gson = jsonBuilder.create()
        val type = object : TypeToken<Map<String, T>>() {
        }.type
        return gson.fromJson(jsonString, type)
    }

    fun toJson(item: Map<String, Any>): String {
        val gson = jsonBuilder.create()
        return gson.toJson(item)
    }

    private val jsonBuilder: GsonBuilder
        get() = GsonBuilder().addSerializationExclusionStrategy(RealmExclusionStrategy())

    private class RealmExclusionStrategy : ExclusionStrategy {

        override fun shouldSkipField(f: FieldAttributes) = f.declaringClass == RealmObject::class.java

        override fun shouldSkipClass(clazz: Class<*>) = false

    }

}
