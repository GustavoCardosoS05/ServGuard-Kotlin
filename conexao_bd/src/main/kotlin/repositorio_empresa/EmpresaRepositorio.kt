package repositorio_empresa

import dominio_empresa.Empresa
import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate

class EmpresaRepositorio {

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
            CREATE TABLE IF NOT EXISTS ServGuard.dominio_empresa.Empresa(
            idEmpresa INT NOT NULL AUTO_INCREMENT,
            nome VARCHAR(70) NOT NULL,
            nomeResponsavel VARCHAR(70) NOT NULL,
            emailResponsavel VARCHAR(70) UNIQUE NOT NULL,
            CNPJ CHAR(14) UNIQUE NOT NULL,

            PRIMARY KEY (idEmpresa)
            )
        """.trimIndent())
    }

    fun inserir(novoValor: Empresa): Boolean{
        return jdbcTemplate.update("""
            INSERT INTO dominio_empresa.Empresa (nome, nomeResponsavel, emailResponsavel, CNPJ)
        """,
            novoValor.getNomeEmpresa(),
            novoValor.getNomeResponsavel(),
            novoValor.getEmailResponsavel(),
            novoValor.getCNPJ()
            ) > 0
    }

    fun listar():List<Empresa>{
        return jdbcTemplate.query("SELECT * FROM dominio_empresa.Empresa", BeanPropertyRowMapper(Empresa::class.java))
    }

    fun existirPorId(id:Int):Boolean{
        return jdbcTemplate.queryForObject("""
            SELECT COUNT(*) FROM dominio_empresa.Empresa WHERE id = ?
        """,
            Int::class.java,
            id
            ) > 0
    }

    fun buscarPorId(id:Int): Empresa?{
        return jdbcTemplate.queryForObject("SELECT * FROM dominio_empresa.Empresa WHERE id = ?",
            BeanPropertyRowMapper(Empresa::class.java),
            id
            )
    }

    fun deletarPorId(id:Int): Boolean{
        return jdbcTemplate.update("DELETE FROM dominio_empresa.Empresa WHERE id = ?",
            id
            ) > 0
    }

    fun atualizarPorId(id:Int, empresaParaAtualizar: Empresa): Boolean{
        return jdbcTemplate.update("""
            UPDATE dominio_empresa.Empresa SET nome = ?, nomeResponsavel = ?, emailResponsavel = ?, CNPJ = ?
        """,
            empresaParaAtualizar.getNomeEmpresa(),
            empresaParaAtualizar.getNomeResponsavel(),
            empresaParaAtualizar.getEmailResponsavel(),
            empresaParaAtualizar.getCNPJ()
            ) > 0
    }


}