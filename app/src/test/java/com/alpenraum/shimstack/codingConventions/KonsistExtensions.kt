package com.alpenraum.shimstack.codingConventions

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.declaration.KoClassDeclaration
import com.lemonappdev.konsist.api.ext.list.properties

fun Konsist.scopeFromProjectWithoutTests() = this.scopeFromProject().slice { !it.path.contains("/test/") }

fun List<KoClassDeclaration>.withMemberVariables() = this.properties().filter { it.hasVarModifier || it.hasValModifier }