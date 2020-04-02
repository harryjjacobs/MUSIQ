package com.harryjjacobs.musiq.ui.util

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

object Serialization {
    val JSON = Json(JsonConfiguration.Stable);
}