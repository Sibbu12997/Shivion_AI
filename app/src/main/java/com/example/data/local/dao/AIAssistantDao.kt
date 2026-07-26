package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entities.AIAssistantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AIAssistantDao {
    @Query("SELECT * FROM ai_assistants ORDER BY name ASC")
    fun getAllAssistants(): Flow<List<AIAssistantEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssistant(assistant: AIAssistantEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInitialAssistants(assistants: List<AIAssistantEntity>)

    @Query("SELECT * FROM ai_assistants WHERE id = :id")
    suspend fun getAssistantById(id: String): AIAssistantEntity?
}
