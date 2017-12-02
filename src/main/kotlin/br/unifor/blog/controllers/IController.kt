package br.unifor.blog.controllers

import com.google.gson.Gson
import org.slf4j.Logger
import spark.Request
import spark.Response

interface IController<T>{

    val logger: Logger
    val gson: Gson

    val store:(req: Request, resp: Response) -> String
    val update:(req: Request, resp: Response) -> String
    val delete:(req: Request, resp: Response) -> String
    val show:(req: Request, resp: Response) -> String
    val index:(req: Request, resp: Response) -> String

}