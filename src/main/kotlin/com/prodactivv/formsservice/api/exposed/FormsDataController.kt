package com.prodactivv.formsservice.api.exposed

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["v1/data"])
class FormsDataController {

    @GetMapping("/t/{t}")
    fun getFieldData(@PathVariable t: String): ResponseEntity<Any> {
        TODO("Here one have to implement a feature that will allow to fetch data for multivalued field. See README")
    }

    @GetMapping("/u/{u}")
    fun updateFields(@PathVariable u: String): ResponseEntity<Any> {
        TODO("Here one have to implement a feature that will allow to fetch data or dataUrl to fields that are now " +
                "resolved due to related field being changed. See README")
    }

}