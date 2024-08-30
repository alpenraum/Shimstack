package com.alpenraum.shimstack.codingConventions

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.properties
import com.lemonappdev.konsist.api.ext.list.withConstructors
import com.lemonappdev.konsist.api.ext.list.withNameContaining
import com.lemonappdev.konsist.api.ext.list.withoutName
import com.lemonappdev.konsist.api.ext.provider.hasAnnotationOf
import com.lemonappdev.konsist.api.verify.assertFalse
import org.junit.Test
import javax.inject.Inject

class PresentationRules {
    private val contextTypes = listOf("Context", "Activity", "Fragment", "Application")

    @Test
    fun `no ViewModel should use field injection`() {
        Konsist.scopeFromProjectWithoutTests().classes()
            .withNameContaining("ViewModel")
            .withoutName("BaseViewModel")
            .withConstructors()
            .properties()
            .assertFalse { it.hasAnnotationOf<Inject>() }
    }

    @Test
    fun `No viewModel may have a reference to a Context`() {
        println(
            Konsist.scopeFromProjectWithoutTests().classes().withNameContaining("ViewModel").withMemberVariables()
                .assertFalse { contextTypes.any { types -> it.type?.hasNameContaining(types) == true } }
        )
    }
}