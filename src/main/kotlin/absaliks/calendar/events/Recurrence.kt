package absaliks.calendar.events

// Add new items at the end of the list -- stored in DB by ordinal
enum class Recurrence {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY,
    FIXED_PERIOD,
    WEEKDAYS,
    DAY_OF_WEEK_SET
}
