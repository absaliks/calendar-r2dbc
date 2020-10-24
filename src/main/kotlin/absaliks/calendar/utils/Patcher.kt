package absaliks.calendar.utils

import com.fasterxml.jackson.databind.JsonNode
import java.time.Instant
import kotlin.reflect.KFunction1

class Patcher(private val patch: JsonNode) {

    fun patch(fieldName: String, currentValue: String): String =
        patch(fieldName, currentValue, this::textConverter)

    fun patchNullable(fieldName: String, currentValue: String?): String? =
        patchNullable(fieldName, currentValue, this::textConverter)

    private fun textConverter(node: JsonNode) = node.textValue()


    fun patch(fieldName: String, currentValue: Int): Int =
        patch(fieldName, currentValue, this::intConverter)

    fun patchNullable(fieldName: String, currentValue: Int?): Int? =
        patchNullable(fieldName, currentValue, this::intConverter)

    private fun intConverter(node: JsonNode) = node.intValue()


    fun patch(fieldName: String, currentValue: Boolean): Boolean =
        patch(fieldName, currentValue, this::booleanConverter)

    private fun booleanConverter(node: JsonNode) = node.booleanValue()


    fun patch(fieldName: String, currentValue: Instant): Instant =
        patch(fieldName, currentValue, this::instantConverter)

    fun patchNullable(fieldName: String, currentValue: Instant?): Instant? =
        patchNullable(fieldName, currentValue, this::instantConverter)

    private fun instantConverter(node: JsonNode) =
        Instant.parse(node.textValue())

    fun <T> patch(
        fieldName: String,
        currentValue: T,
        converter: KFunction1<JsonNode, T>
    ): T {
        val node = patch.get(fieldName)
        return if (node != null) {
            if (node.isNull) throw IllegalArgumentException("$fieldName cannot be null")
            converter(node)
        } else currentValue
    }

    private fun <T> patchNullable(
        fieldName: String,
        currentValue: T?,
        converter: KFunction1<JsonNode, T>
    ): T? {
        val node = patch.get(fieldName)
        return if (node != null) {
            if (node.isNull) null else converter(node)
        } else currentValue
    }
}