package com.example.uebungenfd.srhtest

import io.ktor.client.HttpClient
import io.ktor.client.request.get

//klasse die daten zwischen programmen hin und her zu schicken ->>>> hier view model schickt -> hier richtung backend

class LoremIpsumApi(private val client: HttpClient) {
    suspend fun get(paragraphCount: Int ): String =
        client.get("https://loripsum.net/api/$paragraphCount/short/plaintext")
}