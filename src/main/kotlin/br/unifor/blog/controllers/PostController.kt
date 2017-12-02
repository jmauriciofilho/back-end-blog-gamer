package br.unifor.blog.controllers

import br.unifor.blog.database.DaoFactory
import br.unifor.blog.entity.Post
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spark.Request
import spark.Response

class PostController{

    companion object: IController<Post> {

        override val logger = LoggerFactory.getLogger(PostController::class.java)
        override val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        val postDAO = DaoFactory.getDaoInstance<Post, Long>()

        override val store = { req: Request, _: Response ->

            val json = req.body()
            val post = gson.fromJson(json, Post::class.java)

            val result = postDAO.create(post)

            if(result == 1){
                logger.info("Post criado com sucesso.")
                messageSuccess(200, "Post criado com sucesso.")
            }else{
                logger.error("Error ao criar um post.")
                messageError(400, "Erro ao criar um post.")
            }

        }

        override val update = {req: Request, _: Response ->

            val id = req.params("id").toLong()
            val json = req.body()

            val post = gson.fromJson(json, Post::class.java)
            post.id = id

            val result = postDAO.update(post)

            if(result == 1){
                logger.info("Post atualizado com sucesso.")
                messageSuccess(200, "Post atualizado com sucesso.")
            }else{
                logger.error("Error ao atualizar um post.")
                messageError(400, "Erro ao atualizar um post.")
            }

        }

        override val delete = {req: Request, _: Response ->

            val id = req.params("id").toLong()

            val consult = postDAO.queryBuilder()
            consult.where().idEq(id)
            val post = postDAO.query(consult.prepare())

            val result = postDAO.delete(post)

            if(result == 1){
                logger.info("Post excluido com sucesso")
                messageSuccess(200, "Post excluido com sucesso")
            }else{
                logger.error("Erro ao excluir post.")
                messageError(400, "Erro ao excluir post.")
            }
        }

        override val show = {req: Request, _: Response ->

            val id = req.params("id").toLong()

            val consult = postDAO.queryBuilder()
            consult.where().idEq(id)
            val post = postDAO.query(consult.prepare())

            if(post != null){
                logger.info("Post com o id ${id} foi recuperado")
                gson.toJson(post)
            }else{
                logger.info("Não há post com o id ${id} no banco")
                messageError(404, "Não há post com o id ${id} no banco")
            }
        }

        override val index = {_: Request, _: Response ->

            val posts = postDAO.queryForAll()

            if(posts != null){
                logger.info("Lista de posts foi recuperada.")
                gson.toJson(posts)
            }else{
                logger.info("Não há posts cadastrados")
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