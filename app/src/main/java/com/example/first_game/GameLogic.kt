package com.example.first_game

data class Monster(
    val name: String,
    val maxHp: Int,
    val damage: Int,
    val exp: Int
)


fun createMonster(level: Int): Monster {
    val isBoss = (1..10).random() == 1

    if (isBoss) {
        return Monster(
            name = "Пенисоглот (Босс)",
            maxHp = 100,
            damage = 20,
            exp = 100
        )
    } else {
        return Monster(
            name = "Спермик",
            maxHp = 10 + (level * 2),
            damage = 5,
            exp = 10
        )
    }
}