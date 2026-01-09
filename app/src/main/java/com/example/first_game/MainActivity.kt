package com.example.first_game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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

//Экраны
enum class GameState(){
    SETUP,  //Главное меню
    INVENTORY, //Инвентарь
    BATTLE, //Сама игра
    LOSS //Поражение
}

@Composable
fun GameScreen() {
    // --- СОСТОЯНИЕ (STATE) ---
    var playerHp by remember { mutableStateOf(100) }
    var level by remember { mutableStateOf(1) }
    var potions by remember { mutableStateOf(0) }
    var gameState by remember { mutableStateOf(GameState.SETUP) }

    var currentMonster by remember { mutableStateOf(createMonster(level)) }
    var monsterHp by remember { mutableStateOf(currentMonster.maxHp) }
    var message by remember { mutableStateOf("Появился ${currentMonster.name}!") }

    var tempName by remember { mutableStateOf("") }

    // --- ИНТЕРФЕЙС (UI) ---
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (gameState) {
            GameState.SETUP -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Добро пожаловать, Герой!")

                    // 2. Само поле ввода (лучше использовать OutlinedTextField - он красивее)
                    OutlinedTextField(
                        value = tempName, // Показываем текущее значение переменной
                        onValueChange = { newText ->
                            // Сюда прилетает новый текст при каждом нажатии клавиши.
                            // Мы должны обновить нашу переменную.
                            tempName = newText
                        },
                        label = { Text("Введите имя") }, // Подсказка сверху
                        singleLine = true // Чтобы нельзя было нажать Enter и раздуть поле
                    )

                    // 3. Кнопка подтверждения
                    Button(
                        onClick = {
                            if (tempName.isNotBlank()) { // Проверка, что имя не пустое
                                // Сохраняем имя в основную переменную игрока (если она у тебя есть отдельно)
                                // Или просто используем tempName дальше
                                gameState = GameState.BATTLE // Переходим в игру!
                            }
                        }
                    ) {
                        Text("Начать приключение")
                    }
                }
            }
            GameState.BATTLE -> {
                // Статы Героя
                Text(text = "Герой: $tempName (Lvl $level)", fontSize = 24.sp)
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
                Button(onClick = {
                    gameState = GameState.INVENTORY
                }) {
                    Text("Инвентарь")
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
                if (playerHp <= 0){
                    gameState = GameState.LOSS
                }
            }
            GameState.LOSS -> {
                Button(onClick = {
                    playerHp = 100
                    gameState = GameState.BATTLE
                }) {
                    Text("Перезапуск")
                }
            }
            GameState.INVENTORY -> {
                Button(onClick = {
                    gameState = GameState.BATTLE
                }) {
                    Text("Вернуться в бой")
                }
            }
        }
    }
}