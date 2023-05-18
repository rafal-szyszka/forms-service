package com.prodactivv.formsservice.api.commands

import com.prodactivv.formsservice.api.commands.models.FormDto
import com.prodactivv.formsservice.core.data.models.Field
import com.prodactivv.formsservice.core.data.models.Form
import com.prodactivv.formsservice.core.data.repos.FieldRepository
import com.prodactivv.formsservice.core.data.repos.FormRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class FormsService(
    private val formRepository: FormRepository,
    private val fieldRepository: FieldRepository,
) {

    fun createForm(formDto: FormDto): Form {
        var form = Form(
            name = formDto.name,
            fields = formDto.fields.map {
                fieldRepository.save(
                    Field(
                        label = it.label,
                        decorators = it.decorators,
                        dataUrl = "https://forms.service.bpower2.com/v1/data/?t=438234bsdfds92fn",
                        updateUrl = null,
                        persistenceData = it.modelField
                    )
                )
            },
            type = formDto.type
        )

        form = formRepository.save(form)
        return form
    }

    fun getForm(id: String): Optional<Form> {
        return formRepository.findById(id)
    }

}