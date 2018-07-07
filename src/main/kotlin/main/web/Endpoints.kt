package main.web

object Endpoints {
    val ROOT = "/"
    val HEALTH_CHEK = "/_ah/health"
    val LIVENESS_CHEK = "/liveness_check"
    val ECHO = "/echo/:message"
    val MESSAGE = "/message"
}