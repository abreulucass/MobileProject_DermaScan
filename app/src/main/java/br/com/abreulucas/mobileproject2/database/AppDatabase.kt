package br.com.abreulucas.mobileproject2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.abreulucas.mobileproject2.database.dao.ConsultaDao
import br.com.abreulucas.mobileproject2.database.entity.Consulta

@Database(entities = [Consulta::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun consultaDao(): ConsultaDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "consultas.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}