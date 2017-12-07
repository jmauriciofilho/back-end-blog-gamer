package br.unifor.blog

import br.unifor.blog.controllers.AuthController
import br.unifor.blog.controllers.CommentController
import br.unifor.blog.controllers.PostController
import br.unifor.blog.controllers.UserController
import br.unifor.blog.database.DaoFactory
import br.unifor.blog.entity.Comment
import br.unifor.blog.entity.Post
import br.unifor.blog.entity.Token
import br.unifor.blog.entity.User
import com.j256.ormlite.jdbc.JdbcConnectionSource
import org.slf4j.LoggerFactory
import spark.Spark.*

fun main(args: Array<String>){

    val DATABASE_URL = "jdbc:h2:./blog.db"
    DaoFactory.connSource = JdbcConnectionSource(DATABASE_URL)

    //DaoFactory.createTable<Post>()
    //DaoFactory.createTable<Comment>()
    //DaoFactory.createTable<Token>()
    //DaoFactory.createTable<User>()
    Util.Utilities.enableCORS("*","*","*")

    val logger = LoggerFactory.getLogger("App")

    path("/api", {


//        before("/auth/*", { req, _ ->
//
//            if(!req.uri().contains("login")){
//
//                val key = req.headers("auth-key") ?: ""
//
//                val tokenDao = DaoFactory.getDaoInstance<Token, Long>()
//                val tokenstatementBuilder = AuthController.tokenDAO.queryBuilder()
//                tokenstatementBuilder.where().eq("key", key)
//                val token = tokenDao.query(tokenstatementBuilder.prepare()).firstOrNull()
//
//                if(token == null){
//                    halt(404, """{"status":"ERROR", "description":"Você não possui permissão para acessar está rota."}""")
//                } else {
//                    logger.info("A requisição ${req.host()} de ${req.uri()} é autenticada.")
//                }
//
//            }
//
//        })

        path("/auth", {
            path("/posts", {
                post("", PostController.store)
                put("/:id", PostController.update)
                delete("/:id", PostController.delete)
            })

            path("/comments", {
                put("/:id", CommentController.update)
                delete("/:id", CommentController.delete)
            })

            path("/users", {
                put("/:id", UserController.update)
                delete("/:id", UserController.delete)
            })
        })

        path("/users", {
            get("", UserController.index)
            get("/:id", UserController.show)
            post("", UserController.store)
        })

        path("/login", {
            post("", AuthController.login)
        })

        path("/posts", {
            get("", PostController.index)
            get("/:id", PostController.show)
        })

        path("/comments", {
            get("", CommentController.index)
            get("/:id", CommentController.show)
            post("", CommentController.store)
        })
    })

}