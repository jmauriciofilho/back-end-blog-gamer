package br.unifor.blog

import br.unifor.blog.controllers.PostController
import br.unifor.blog.database.DaoFactory
import br.unifor.blog.entity.Post
import com.j256.ormlite.jdbc.JdbcConnectionSource
import org.slf4j.LoggerFactory
import spark.Spark.*

fun main(args: Array<String>){

    val DATABASE_URL = "jdbc:h2:./blog.db"
    DaoFactory.connSource = JdbcConnectionSource(DATABASE_URL)

    //DaoFactory.createTable<Post>()

    val logger = LoggerFactory.getLogger("App")

    path("/api", {
        path("/posts", {
            get("", PostController.index)
            get("/:id", PostController.show)
            post("", PostController.store)
            put("/:id", PostController.update)
            delete("/:id", PostController.delete)
        })
    })

}