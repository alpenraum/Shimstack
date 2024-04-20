package com.alpenraum.shimstack.codingConventions

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.modifierprovider.withoutAbstractModifier
import com.lemonappdev.konsist.api.ext.list.withNameContaining
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

class UseCaseRules {
    private val useCaseScope =
        Konsist.scopeFromPackage("com.alpenraum.shimstack.usecases..")
            .slice { !it.path.contains("/test/") }

    @Test
    fun `All UseCases must have the operator fun 'invoke()' defined`() {
        useCaseScope.classes().withNameContaining("UseCase").assertTrue { clazz ->
            clazz.hasFunction {
                (it.hasOverrideModifier || it.hasOperatorModifier) && it.hasNameContaining("invoke")
            }
        }
    }

    @Test
    fun `All UseCases may not have any public functions apart from 'invoke()'`() {
        useCaseScope.classes().withNameContaining("UseCase").assertTrue { clazz ->
            clazz.countFunctions(includeNested = false) {
                !(it.hasOverrideModifier || it.hasOperatorModifier) && it.hasPublicOrDefaultModifier
            } == 0
        }
    }

    @Test
    fun `A UseCase's primary constructor must be annotated with '@Inject' `() {
        useCaseScope.classes().withoutAbstractModifier().withNameContaining("UseCase")
            .assertTrue { clazz ->
                clazz.primaryConstructor?.hasAnnotation { it.hasNameContaining("Inject") }
                    ?: false
            }
    }
}