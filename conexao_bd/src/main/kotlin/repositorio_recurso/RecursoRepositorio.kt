package repositorio_recurso

import dominio_recurso.Recurso
import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate


class RecursoRepositorio {

    lateinit var jdbcTemplate: JdbcTemplate

    fun configurar(){
        val datasource = BasicDataSource()
        datasource.driverClassName = "com.mysql.cj.jdbc.Driver"
        datasource.url = "jdbc:mysql://localhost:3306/ServGuard"
        datasource.username = "seu_usuario"
        datasource.password = "sua_senha"

        jdbcTemplate = JdbcTemplate(datasource)
    }

    fun criarTabela(){
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS ServGuard.dominio_recurso.Recurso(
            idRecurso INT NOT NULL AUTO_INCREMENT,
            nome VARCHAR(45) NOT NULL,
            unidadeMedida VARCHAR(45) NOT NULL,
            
            PRIMARY KEY (idRecurso)
            ) 
        """.trimIndent())
    }

    fun inserir(novoValor: Recurso):Boolean{
        return jdbcTemplate.update("""
            INSERT INTO dominio_recurso.Recurso (nome, unidadaMedida) VALUES (?,?)
        """,
            novoValor.getNomeRecurso(),
            novoValor.getUnidadeMedida()
            ) > 0
    }

    fun listar():List<Recurso>{
        return jdbcTemplate.query("SELECT * FROM dominio_recurso.Recurso", BeanPropertyRowMapper(Recurso::class.java))
    }

    fun existePorId(id: Int): Boolean{
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM dominio_recurso.Recurso WHERE id = ?",
            Int::class.java,
            id
        ) > 0
    }

    fun buscarPorId(id:Int): Recurso? {
        return jdbcTemplate.queryForObject("SELECT * FROM dominio_recurso.Recurso WHERE id = ?",
            BeanPropertyRowMapper(Recurso::class.java),
            id
        )
    }

    fun deletarPorId(id:Int):Boolean{
        return jdbcTemplate.update(
            "DELETE FROM dominio_recurso.Recurso WHERE id = ?",
            id
        ) > 0
    }

    fun atualizarPorId(id:Int, recursoParaAtualizar: Recurso):Boolean{
        return jdbcTemplate.update(
            """ UPDATE dominio_recurso.Recurso SET nome = ?, unidadeMedida = ?
            """,
            recursoParaAtualizar.getNomeRecurso(),
            recursoParaAtualizar.getUnidadeMedida(),
            id
        ) > 0

    }


}