package main.web

import com.google.cloud.Timestamp
import com.google.cloud.datastore.DatastoreOptions
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.FullEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import spark.Route
import java.net.Inet6Address
import java.time.ZonedDateTime

object Routes {
    val default = Route { _, _ ->
        mapOf(
                "womai" to Inet6Address.getLocalHost().hostName,
                "timestamp" to ZonedDateTime.now().toString()
        )
    }

    val healthcheck = Route { _, _ -> "" }

    val echo = Route { request, _ ->
        val entity = persist(mapOf("message" to (request.params("message") ?: "")))

        mapOf("message" to entity.getString("message"), "timestamp" to entity.getTimestamp("created_timestamp"))
    }

    val message = Route { request, _ ->
        val type = object : TypeToken<Map<String, String>>() {}.type
        val message: Map<String, String> = Gson().fromJson(request.body(), type)

        persist(message)
    }

    private fun persist(message: Map<String, String>): Entity {
        val now = Timestamp.now()

        val datastore = DatastoreOptions.getDefaultInstance().service
        val key = datastore.newKeyFactory().setNamespace("test").setKind("Echo").newKey()
        val entity = FullEntity.newBuilder(key)
                .set("created_timestamp", now)
                .let { builder ->
                    message.forEach { k, v -> builder.set(k, v) }
                    builder.build()
                }

        val persisted = datastore.put(entity)
        val retrieved = datastore.get(persisted.key)

        System.out.printf("Retrieved %s%n", retrieved)

        return retrieved
    }
}