package absaliks.calendar.exceptions

import kotlin.reflect.KClass

class EntityNotFoundException(id: Long, entityClass: KClass<*>)
    : RuntimeException("Couldn't find ${entityClass.simpleName}#${id}")