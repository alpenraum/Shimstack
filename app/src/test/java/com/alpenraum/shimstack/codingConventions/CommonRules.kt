package com.alpenraum.shimstack.codingConventions

import androidx.compose.ui.tooling.preview.Preview
import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.properties
import com.lemonappdev.konsist.api.ext.list.withAnnotationOf
import com.lemonappdev.konsist.api.verify.assertFalse
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

class CommonRules {
    @Test
    fun `no file may have wildcard imports`() {
        Konsist.scopeFromProject().files.assertFalse {
            it.hasImport { import ->
                import.name.contains(
                    '*'
                )
            }
        }
    }

    @Test
    fun `All JetPack Compose previews contain 'Preview' in method name`() {
        Konsist.scopeFromProject().functions().withAnnotationOf(Preview::class).assertTrue {
            it.name.contains("Preview")
        }
    }

    @Test
    fun `no empty files allowed`() {
        Konsist.scopeFromProject().files.assertFalse { it.text.isEmpty() }
    }

    @Test
    fun `no field should have 'm' prefix`() {
        Konsist.scopeFromProject().classes().properties().assertFalse {
            if (it.hasNameStartingWith("mTan")) {
                false
            } else {
                val secondCharacterIsUppercase = it.name.getOrNull(1)?.isUpperCase() ?: false
                it.name.startsWith('m') && secondCharacterIsUppercase
            }
        }
    }
}