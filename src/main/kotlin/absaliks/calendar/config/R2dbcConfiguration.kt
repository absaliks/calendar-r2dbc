package absaliks.calendar.config

import absaliks.calendar.converters.RecurrenceReadConverter
import absaliks.calendar.converters.RecurrenceWriteConverter
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration

@Configuration
class R2dbcConfiguration : AbstractR2dbcConfiguration() {
    override fun connectionFactory(): ConnectionFactory {
        return ConnectionFactories.get("r2dbc:h2:mem:///test?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
    }

    override fun getCustomConverters(): MutableList<Any> {
        return arrayListOf(
            RecurrenceReadConverter(),
            RecurrenceWriteConverter()
        )
    }
}