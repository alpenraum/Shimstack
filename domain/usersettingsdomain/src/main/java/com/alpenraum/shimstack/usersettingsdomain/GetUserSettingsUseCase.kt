package com.alpenraum.shimstack.usersettingsdomain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserSettingsUseCase @Inject
constructor(private val userSettingsRepository: UserSettingsRepository) {
    operator fun invoke(): Flow<UserSettings> = userSettingsRepository.getUserSettings()
}