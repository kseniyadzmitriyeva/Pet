package com.furiqd.pairing.data.datasource.model

object RoomConstants {
    const val COLLECTION_ROOMS = "rooms"
    const val COLLECTION_USERS = "users"

    object Fields {
        const val CODE = "code"
        const val STATUS = "status"
        const val FRIEND_ID = "friendId"
        const val PET_STAGE = "pet.stage"
        const val PET_HATCHED_AT = "pet.hatchedAt"
    }

    object Status {
        const val WAITING = "WAITING"
        const val ACTIVE = "ACTIVE"
    }

    object PetStage {
        const val EGG = "EGG"
        const val BABY = "BABY"
    }

    object Defaults {
        const val INITIAL_LEVEL = 1
        const val INITIAL_EXP = 0
    }
}