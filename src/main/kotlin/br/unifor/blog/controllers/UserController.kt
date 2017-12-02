package br.unifor.blog.controllers


import br.unifor.blog.database.DaoFactory
import br.unifor.blog.entity.User
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spark.Request
import spark.Response
import java.security.MessageDigest

class UserController {

    companion object : IController<User> {

        override val logger = LoggerFactory.getLogger(UserController::class.java)
        override val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        val userDAO = DaoFactory.getDaoInstance<User, Long>()

        override val store = { req: Request, resp: Response ->

            val json = req.body()
            val user = gson.fromJson(json, User::class.java)

            user.salt = "${System.currentTimeMillis()}"

            val digest = MessageDigest.getInstance("SHA-256")
            val hashedPassword = digest.digest("${user.email}:${user.password}:${user.salt}".toByteArray())
            user.password = String(hashedPassword)

            val ret = userDAO.create(user)

            if (ret == 1) {
                logger.info("Usuário criado com sucesso.")
                messageSuccess(200, "Usuário criado com sucesso.")
            } else {
                logger.error("Erro ao criar usuário")
                messageError(400, "Erro ao criar usuário")
            }

        }

        override val update = { req: Request, resp: Response ->

            val id = req.params("id").toLong()
            val json = req.body()

            val user = gson.fromJson(json, User::class.java)
            user.id = id

            user.salt = "${System.currentTimeMillis()}"

            val digest = MessageDigest.getInstance("SHA-256")
            val hashedPassword = digest.digest("${user.email}:${user.password}:${user.salt}".toByteArray())
            user.password = String(hashedPassword)

            val ret = userDAO.update(user)

            if (ret == 1) {
                logger.info("Usuário atualizado com sucesso.")
                messageSuccess(200, "Usuário atualizado com sucesso.")
            } else {
                logger.error("Erro ao atualizar usuário")
                messageError(400, "Erro ao atualizar usuário")
            }

        }

        override val delete = { req: Request, resp: Response ->

            val id = req.params("id").toLong()

            val statementBuilder = userDAO.queryBuilder()
            statementBuilder.where().idEq(id)
            val user = userDAO.query(statementBuilder.prepare())

            val ret = userDAO.delete(user)

            if (ret == 1) {
                logger.info("Usuário excluído com sucesso.")
                messageSuccess(200, "Usuário excluído com sucesso.")
            } else {
                logger.error("Erro ao excluír usuário")
                messageError(400, "Erro ao excluír usuário")
            }

        }

        override val show = { req: Request, resp: Response ->

            val id = req.params("id").toLong()

            val statementBuilder = userDAO.queryBuilder()
            statementBuilder.where().idEq(id)
            val user = userDAO.query(statementBuilder.prepare())

            if(user != null){
                logger.info("Usuário com o id $id foi recuperado")
                gson.toJson(user)
            } else {
                logger.info("Não há usuário com o id $id no banco de dados")
                "{}"
            }

        }

        override val index = { req: Request, resp: Response ->

            val users = userDAO.queryForAll()

            if(users.size > 0){
                logger.info("Lista de usuários encontrada.")
                gson.toJson(users)
            } else {
                logger.info("Não há usuários cadastrados no banco de dados.")
                "{}"
            }

        }

        private fun messageSuccess(cod:Int, message:String): String {
            return """ {"status":${cod}, "message":${message}} """
        }

        private fun messageError(cod:Int, message:String): String {
            return """ {"status":${cod}, "message":${message}} """
        }

    }
}