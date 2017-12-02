package br.unifor.blog.controllers

import br.unifor.blog.database.DaoFactory
import br.unifor.blog.entity.Comment
import com.google.gson.GsonBuilder
import org.slf4j.LoggerFactory
import spark.Request
import spark.Response

class CommentController{

    companion object: IController<Comment> {

        override val logger = LoggerFactory.getLogger(CommentController::class.java)
        override val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        val commentDAO = DaoFactory.getDaoInstance<Comment, Long>()

        override val store = { req: Request, _: Response ->

            val json = req.body()
            val comment = gson.fromJson(json, Comment::class.java)

            val result = commentDAO.create(comment)

            if(result == 1){
                logger.info("Comentário criado com sucesso.")
                messageSuccess(200, "Comentário criado com sucesso.")
            }else{
                logger.error("Error ao criar um comentário.")
                messageError(400, "Erro ao criar um comentário.")
            }

        }

        override val update = { req: Request, _: Response ->

            val id = req.params("id").toLong()
            val json = req.body()

            val post = gson.fromJson(json, Comment::class.java)
            post.id = id

            val result = commentDAO.update(post)

            if(result == 1){
                logger.info("Comentário atualizado com sucesso.")
                messageSuccess(200, "Comentário atualizado com sucesso.")
            }else{
                logger.error("Error ao atualizar um comentário.")
                messageError(400, "Erro ao atualizar um comentário.")
            }

        }

        override val delete = { req: Request, _: Response ->

            val id = req.params("id").toLong()

            val consult = commentDAO.queryBuilder()
            consult.where().idEq(id)
            val post = commentDAO.query(consult.prepare())

            val result = commentDAO.delete(post)

            if(result == 1){
                logger.info("Comentário excluido com sucesso")
                messageSuccess(200, "Comentário excluido com sucesso")
            }else{
                logger.error("Erro ao excluir comentário.")
                messageError(400, "Erro ao excluir comentário.")
            }
        }

        override val show = { req: Request, _: Response ->

            val id = req.params("id").toLong()

            val consult = commentDAO.queryBuilder()
            consult.where().idEq(id)
            val post = commentDAO.query(consult.prepare())

            if(post != null){
                logger.info("Comentário com o id ${id} foi recuperado")
                gson.toJson(post)
            }else{
                logger.info("Não há comentário com o id ${id} no banco")
                messageError(404, "Não há comentário com o id ${id} no banco")
            }
        }

        override val index = { _: Request, _: Response ->

            val posts = commentDAO.queryForAll()

            if(posts.size > 0){
                logger.info("Lista de comentários foi recuperada.")
                gson.toJson(posts)
            }else{
                logger.info("Não há comentários cadastrados")
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