package com.example.first_game

// 1. Чертеж монстра
data class Monster(
    val name: String,
    val maxHp: Int,
    val damage: Int,
    val exp: Int,
    val dropRate : Double
)

open class Loot(
    val name: String,
    val type: String,
    val category: String,
    val tag: String,
    val rare: Double
)

class Weapon(
    name: String,
    val damage: Int,
    rare: Double

) : Loot(
    name = name,
    type = "Weapon",
    category = "Main Hand",
    tag = "Wearable",
    rare = rare
)

class Armour(
    name: String,
    val damage: Int,
    rare: Double
) : Loot(
    name = name,
    type = "Armour",
    category = "Body",
    tag = "Wearable",
    rare = rare
)

class Utility(
    name: String,
    val damage: Int,
    rare: Double
) : Loot(
    name = name,
    type = "Utility",
    category = "Backpack",
    tag = "Unwearable",
    rare = rare
)

fun createMonster(level: Int): Monster {
    val isBoss = (1..10).random() == 1

    if (isBoss) {
        return Monster(
            name = "Пенисоглот (Босс)",
            maxHp = 100,
            damage = 20,
            exp = 100,
            dropRate = 0.5
        )
    } else {
        return Monster(
            name = "Спермик",
            maxHp = 10 + (level * 2),
            damage = 5,
            exp = 10 ,
            dropRate = 0.1
        )
    }
}