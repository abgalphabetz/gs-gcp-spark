package main

import com.google.gson.Gson
import main.web.Endpoints.ECHO
import main.web.Endpoints.HEALTH_CHEK
import main.web.Endpoints.LIVENESS_CHEK
import main.web.Endpoints.MESSAGE
import main.web.Endpoints.ROOT
import main.web.Routes
import spark.ResponseTransformer
import spark.Service
import spark.Service.ignite


class Main {
    lateinit var http: Service
    val gson = Gson()

    fun start() {
        http = ignite()
        http.port(System.getenv()[CONF_PORT]?.toInt() ?: DEFAULT_PORT)

        val transformer = ResponseTransformer { o: Any -> gson.toJson(o) }

        http.get(HEALTH_CHEK, Routes.healthcheck)
        http.get(LIVENESS_CHEK, Routes.healthcheck)
        http.get(ROOT, Routes.default, transformer)
        http.get(ECHO, Routes.echo, transformer)
        http.post(MESSAGE, Routes.message, transformer)
    }

    fun stop() {
        http.stop()
    }

    companion object {

        private val CONF_PORT = "PORT"
        private val DEFAULT_PORT = 8080

        @JvmStatic
        fun main(args: Array<String>) {
            Main().start()
        }
    }
}