package com.prodactivv.formsservice.core.data.repos

import com.prodactivv.formsservice.core.data.models.Field
import com.prodactivv.formsservice.core.data.models.Form
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface FieldRepository: MongoRepository<Field, String> {
}