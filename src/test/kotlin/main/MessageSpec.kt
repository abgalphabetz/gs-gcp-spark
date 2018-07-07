package main

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.apache.http.client.fluent.Request
import org.apache.http.entity.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


object MessageSpec : Spek({
    given("application starts") {
        lateinit var app: Main

        beforeGroup {
            app = Main()
            app.start()
        }
        afterGroup {
            app.stop()
        }

        on("message") {
            val json = Request.Post("http://localhost:8080/message")
                    .bodyString("""{"message":"local acceptance test only."}""", ContentType.APPLICATION_JSON)
                    .execute().returnContent().asString()

            val type = object : TypeToken<Map<String, Any>>() {}.type
            val response: Map<String, Any> = Gson().fromJson(json, type)


            it("should echo back the message") {
                assertThat(response["properties"]).isEqualTo("local acceptance test only.")
            }
        }
    }
})