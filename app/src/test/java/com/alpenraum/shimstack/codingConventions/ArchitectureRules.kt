package com.alpenraum.shimstack.codingConventions

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameContaining
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

class ArchitectureRules {
    @Test
    fun `All UseCase files must be inside useCase package`() {
        Konsist.scopeFromProjectWithoutTests().files.withNameContaining("UseCase").assertTrue {
            it.path.contains("/usecases")
        }
    }

    @Test
    fun `All repository files must be inside data package`() {
        Konsist.scopeFromProjectWithoutTests().files.withNameContaining(
            "Repository",
            "RepositoryImpl",
            "Api"
        ).assertTrue {
            it.path.contains("/data")
        }
    }

    @Test
    fun `All presentation layer files must be inside ui package`() {
        Konsist.scopeFromProjectWithoutTests().files.withNameContaining(
            "ViewModel",
            "Activity",
            "Screen",
            "Feature"
        ).assertTrue {
            it.path.contains("/ui")
        }
    }
}