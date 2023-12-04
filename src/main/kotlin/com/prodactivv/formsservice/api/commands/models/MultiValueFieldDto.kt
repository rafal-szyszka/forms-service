package com.prodactivv.formsservice.api.commands.models

import com.prodactivv.formsservice.core.proql.models.ProQLQuery

class MultiValueFieldDto(
    val proQLQuery: ProQLQuery,
    var result: List<Map<String, Any>>? = null
)