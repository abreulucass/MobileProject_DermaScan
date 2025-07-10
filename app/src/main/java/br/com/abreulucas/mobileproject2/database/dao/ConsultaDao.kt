package br.com.abreulucas.mobileproject2.database.dao

import androidx.room.*
import br.com.abreulucas.mobileproject2.database.entity.Consulta

@Dao
interface ConsultaDao {
    @Insert
    suspend fun insertConsulta(consulta: Consulta)

    @Query("SELECT * FROM consultas ORDER BY id DESC")
    suspend fun getAllConsultas(): List<Consulta>
}
