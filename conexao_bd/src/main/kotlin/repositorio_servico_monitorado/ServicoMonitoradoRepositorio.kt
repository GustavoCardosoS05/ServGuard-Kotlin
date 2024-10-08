package repositorio_servico_monitorado

import dominio_servico_monitorado.ServicoMonitorado
import org.apache.commons.dbcp2.BasicDataSource
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate

class ServicoMonitoradoRepositorio {

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
            CREATE TABLE IF NOT EXISTS ServGuard.dominio_servico_monitorado.ServicoMonitorado(
            idServicoMonitorado INT NOT NULL AUTO_INCREMENT,
            fkEmpresa INT NOT NULL,
            nome VARCHAR(50) NOT NULL,
            URL VARCHAR(255) NOT NULL,

            CONSTRAINT fkEmpresaServicoMonitorado FOREIGN KEY (fkEmpresa) REFERENCES ServGuard.dominio_empresa.Empresa(idEmpresa),
            PRIMARY KEY (idServicoMonitorado, fkEmpresa)
            )
        """.trimIndent())

    }

    fun inserir(novoValor: ServicoMonitorado):Boolean{
        return jdbcTemplate.update("""
            INSERT INTO dominio_servico_monitorado.ServicoMonitorado (fkEmpresa, nome, URL) VALUES (?,?,?)
        """,
            novoValor.getfkEmpresa(),
            novoValor.getNomeServico(),
            novoValor.getURL()
            ) > 0
    }

    fun listar():List<ServicoMonitorado>{
        return jdbcTemplate.query("SELECT * FROM dominio_servico_monitorado.ServicoMonitorado",
            BeanPropertyRowMapper(ServicoMonitorado::class.java))
    }

    fun existirPorId(id:Int):Boolean{
        return jdbcTemplate.queryForObject("""
            SELECT COUNT(*) FROM dominio_servico_monitorado.ServicoMonitorado WHERE id = ?
        """,
            Int::class.java,
            id
            ) > 0
    }

    fun buscarPorid(id:Int): ServicoMonitorado?{
        return jdbcTemplate.queryForObject("SELECT * FROM dominio_servico_monitorado.ServicoMonitorado WHERE id = ?",
            BeanPropertyRowMapper(ServicoMonitorado::class.java),
            id
            )
    }

    fun deletarPorId(id:Int): Boolean{
        return jdbcTemplate.update("DELETE FROM dominio_servico_monitorado.ServicoMonitorado WHERE id = ?",
            id
        ) > 0

    }

    fun atualizarPorId(id:Int, servicoParaAtualizar: ServicoMonitorado): Boolean{
        return jdbcTemplate.update("""
            UPDATE dominio_servico_monitorado.ServicoMonitorado SET fkEmpresa = ?, nome = ?, URL = ?
        """,
            servicoParaAtualizar.getfkEmpresa(),
            servicoParaAtualizar.getNomeServico(),
            servicoParaAtualizar.getURL(),
            id
        ) > 0
    }




}