package com.furiqd.data.mapper

import com.furiqd.domain.model.JointPet
import com.furiqd.local.entity.JointPetEntity
import com.furiqd.network.dto.JointPetDto
import java.util.Date

fun JointPetEntity.toDomain() = JointPet(
    id = id,
    name = name,
    level = level,
    experience = experience,
    stage = stage,
    hatchedAt = hatchedAt?.let { Date(it) }
)

fun JointPet.toEntity() = JointPetEntity(
    id = id,
    name = name,
    level = level,
    experience = experience,
    stage = stage,
    hatchedAt = hatchedAt?.time
)

fun JointPetDto.toDomain(): JointPet {
    return JointPet(
        id = this.id,
        name = this.name,
        level = this.level,
        experience = this.experience,
        stage = this.stage,
        hatchedAt = this.hatchedAt
    )
}
fun JointPet.toDto(): JointPetDto {
    return JointPetDto(
        id = this.id,
        name = this.name,
        level = this.level,
        experience = this.experience,
        stage = this.stage,
        hatchedAt = this.hatchedAt
    )
}