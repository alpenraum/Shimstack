package com.alpenraum.shimstack.codingConventions

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.properties
import com.lemonappdev.konsist.api.ext.list.withConstructors
import com.lemonappdev.konsist.api.ext.list.withNameContaining
import com.lemonappdev.konsist.api.ext.provider.hasAnnotationOf
import com.lemonappdev.konsist.api.verify.assertFalse
import org.junit.Test
import javax.inject.Inject

class RepositoryRules {
    private val repositoryScope =
        Konsist.scopeFromPackage("com.alpenraum.shimstack.data..")
            .slice { !it.path.contains("/test/") }

    @Test
    fun `no Repository should use field injection`() {
        Konsist
        repositoryScope.classes().withNameContaining("Repository").withConstructors().properties()
            .assertFalse { it.hasAnnotationOf<Inject>() }
    }
}