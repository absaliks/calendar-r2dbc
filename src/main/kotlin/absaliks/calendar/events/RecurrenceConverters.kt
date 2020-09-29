package absaliks.calendar.events

import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter

@ReadingConverter
class RecurrenceReadConverter : Converter<Byte, Recurrence> {
    override fun convert(source: Byte): Recurrence = Recurrence.values()[source.toInt()]
}

@WritingConverter
class RecurrenceWriteConverter : Converter<Recurrence, Byte> {
    override fun convert(source: Recurrence): Byte = source.ordinal.toByte()
}