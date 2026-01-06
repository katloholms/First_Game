package com.example.first_game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameScreen()
        }
    }
}

@Composable
fun GameScreen() {
    // --- СОСТОЯНИЕ (STATE) ---
    var playerHp by remember { mutableStateOf(100) }
    var level by remember { mutableStateOf(1) }
    var potions by remember { mutableStateOf(0) }

    // 🔥 МАГИЯ ЗДЕСЬ: Создаем монстра, используя функцию из соседнего файла!
    // currentMonster хранит имя и максимальные статы
    var currentMonster by remember { mutableStateOf(createMonster(level)) }

    // monsterHp хранит текущее здоровье врага
    var monsterHp by remember { mutableStateOf(currentMonster.maxHp) }

    var message by remember { mutableStateOf("Появился ${currentMonster.name}!") }


    // --- ИНТЕРФЕЙС (UI) ---
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Статы Героя
        Text(text = "Герой: Link (Lvl $level)", fontSize = 24.sp)
        Text(text = "HP: $playerHp | Зелья: $potions", fontSize = 18.sp)

        Text(text = "\n--- VS ---\n")

        // 🔥 Статы Монстра (берем из объекта)
        Text(text = "Враг: ${currentMonster.name}", fontSize = 22.sp)
        Text(text = "HP Врага: $monsterHp / ${currentMonster.maxHp}", fontSize = 20.sp)

        Text(text = "\n$message\n", fontSize = 16.sp)

        // --- КНОПКИ ---

        // Кнопка АТАКИ
        Button(onClick = {
            if (monsterHp > 0 && playerHp > 0) {
                // 1. Мы бьем монстра
                val myDamage = (10..20).random()
                monsterHp -= myDamage
                message = "Ты нанес $myDamage урона!"

                // 2. Если монстр выжил — он бьет в ответ (Урон берем из ФАЙЛА!)
                if (monsterHp > 0) {
                    playerHp -= currentMonster.damage
                    message += "\n${currentMonster.name} кусь за ${currentMonster.damage} HP!"
                } else {
                    // 3. Победа
                    monsterHp = 0
                    message = "Победа! Враг повержен."

                    // Тут можно добавить кнопку "Найти нового врага"
                }
            }
        }) {
            Text(text = "⚔️ Атаковать")
        }

        // Кнопка "Найти нового врага" (показываем, только если монстр мертв)
        if (monsterHp <= 0) {
            Button(onClick = {
                // Вызываем функцию из соседнего файла снова!
                currentMonster = createMonster(level)
                monsterHp = currentMonster.maxHp // Обновляем HP
                message = "Вы встретили: ${currentMonster.name}"
            }) {
                Text("Искать следующего")
            }
        }
    }
}