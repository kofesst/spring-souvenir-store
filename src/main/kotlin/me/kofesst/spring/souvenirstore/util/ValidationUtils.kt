package me.kofesst.spring.souvenirstore.util

import java.util.*
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [YearsDifferenceValidator::class])
@MustBeDocumented
annotation class YearsDifference(
    val minYears: Int = Int.MIN_VALUE,
    val message: String = "Некорректная дата",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class YearsDifferenceValidator : ConstraintValidator<YearsDifference, Date> {
    private var minYears: Int = Int.MIN_VALUE

    override fun initialize(constraintAnnotation: YearsDifference?) {
        minYears = constraintAnnotation?.minYears ?: Int.MIN_VALUE
    }

    override fun isValid(value: Date?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) return false

        val years = value yearsUntil Date()
        return years >= minYears
    }
}