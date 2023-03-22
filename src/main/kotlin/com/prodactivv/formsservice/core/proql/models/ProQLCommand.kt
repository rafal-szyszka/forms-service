package com.prodactivv.formsservice.core.proql.models

class ProQLCommand(
    var type: String,
    var properties: MutableMap<String, Any>,
    var commands: MutableList<ProQLCommand>,
    var parentProperty: String? = null,
) {
}