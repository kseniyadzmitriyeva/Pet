package com.furiqd.shared.data.mapper

import com.furiqd.shared.domain.model.JointPet
import com.furiqd.shared.domain.model.PetEggType
import com.furiqd.local.entity.JointPetEntity
import com.furiqd.network.dto.JointPetDto
import java.util.Date

fun JointPetEntity.toDomain() = JointPet(
    id = id,
    name = name,
    level = level,
    experience = experience,
    stage = stage,
    hatchedAt = hatchedAt?.let { Date(it)

    }
)

fun JointPet.toEntity() = JointPetEntity(
    id = id,
    name = name,
    level = level,
    experience = experience,
    stage = stage,
    hatchedAt = hatchedAt?.time,
    petEggType = petEggType.id
)

fun JointPetDto.toDomain(): JointPet {
    return JointPet(
        id = this.id,
        name = this.name,
        level = this.level,
        experience = this.experience,
        stage = this.stage,
        hatchedAt = this.hatchedAt,
        petEggType = PetEggType.fromId(this.petEggType)
    )
}
fun JointPet.toDto(): JointPetDto {
    return JointPetDto(
        id = this.id,
        name = this.name,
        level = this.level,
        experience = this.experience,
        stage = this.stage,
        hatchedAt = this.hatchedAt,
        petEggType = this.petEggType.id
    )
}