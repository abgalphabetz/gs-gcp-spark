package main

import com.google.gson.Gson
import org.apache.http.client.fluent.Request
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import com.google.gson.reflect.TypeToken



object EchoSpec : Spek({
    given("application starts") {
        val app = Main()

        beforeGroup { app.start() }
        afterGroup { app.stop() }

        on("echo") {
            val json = Request.Get("http://localhost:8080/echo/sample-message")
                    .execute().returnContent().asString()

            val type = object : TypeToken<Map<String, Any>>() {}.type
            val response: Map<String, Any> = Gson().fromJson(json, type)

            it("should echo back the message") {
                assertThat(response).containsEntry("message", "sample-message")
            }
        }
    }
})