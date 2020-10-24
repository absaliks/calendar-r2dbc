package absaliks.calendar.model

enum class Recurrence {
    /* --------------------
    Enum values are stored in DB by their ordinals, so:
    - do not reorder
    - add new items at the end of the list
    ----------------------- */

    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY,
    FIXED_PERIOD,
    WEEKDAYS,
    DAY_OF_WEEK_SET
}
