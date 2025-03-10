## 📖 Game Description
<img width="799" alt="Screenshot 2025-03-08 at 15 49 20" src="https://github.com/user-attachments/assets/46e0e951-5639-4ac4-a7da-519004b432af" />


Rock-Paper-Scissors Card Battle transforms the simple hand game into a strategic card battle. Players take turns attacking each other's cards or switching their cards with new ones from the deck. Each card type (Stone, Paper, Scissors) has unique attributes and special abilities, creating a dynamic and engaging gameplay experience.

## 🎮 Game Rules

### Basic Rules

- Each player starts with 5 random cards
- Players take turns to either attack an opponent's card or switch their cards with ones from the deck
- When a card is attacked, its defence is reduced first. When defence reaches 0, the card's life is reduced
- When a card's life reaches 0, it is defeated and that slot is locked
- The goal is to defeat all of your opponent's cards


<img width="815" alt="Screenshot 2025-03-08 at 15 49 36" src="https://github.com/user-attachments/assets/2cf62224-6a76-4c23-ba5b-cbec4af79e6c" />



### Card Types

#### Stone Cards

- **Life**: 2
- **Defence**: 10
- **Attack**: 2
- No special abilities


#### Paper Cards

- **Life**: 10
- **Defence**: 1
- **Attack**: 2
- **Special**: Mutes either defence or attack on the attacked card until after the player has made another turn


#### Scissors Cards

- **Life**: 5
- **Defence**: 3
- **Attack**: 3
- **Special**: Causes damage over time. First attack does max damage, and subsequent attacks (up to 3 total) do random damage from 1 to max attack


## 🛠️ Installation

### Prerequisites

- Java 17 or higher
- Maven


### Steps

1. Clone the repository:

```shellscript
git clone https://github.com/yourusername/rock-paper-scissors-card-battle.git
cd rock-paper-scissors-card-battle
```


2. Build the project with Maven:

```shellscript
mvn clean package
```


3. Run the game:

```shellscript
java -jar target/demo-1.0-SNAPSHOT.jar
```




## 🎯 How to Play

1. **Start Screen**: Enter player names and click "Start Game"
2. **Game Screen**: The game board shows both players' cards and the deck
3. **Taking a Turn**: On your turn, you can:

1. **Attack**: Select one of your cards, then select an opponent's card to attack
2. **Switch Cards**: Click the "Switch Mode" button, select cards from your hand and an equal number from the available deck cards, then click "Switch Selected Cards"



4. **End Turn**: Click "End Turn" to pass the turn to your opponent
5. **Winning**: Defeat all of your opponent's cards to win the game


### Card Switching Rules

- You can only switch cards once per turn
- You cannot switch cards after attacking
- You must select an equal number of your cards and deck cards
- You cannot switch defeated cards


## ✨ Features

- Turn-based strategic gameplay
- Three unique card types with different stats and abilities
- Custom card selection for switching
- Visual feedback for card selection and game state
- Detailed game log showing all actions and results
- Responsive UI design


## 🧰 Technologies Used

- Java 23
- JavaFX for the user interface
- Maven for dependency management
- Object-oriented design with MVC architecture


## 🔮 Future Improvements

- Add more card types with unique abilities
- Implement card animations for attacks and special abilities
- Add sound effects and background music
- Create an AI opponent for single-player mode
- Add online multiplayer functionality
- Implement a card collection system
- Add a tutorial mode for new players


## 🏗️ Project Structure

```plaintext
src/main/java/com/cardgame/
├── controller/           # UI controllers
│   ├── GameScreenController.java
│   └── MainMenuController.java
├── model/                # Game logic and data models
│   ├── Card.java
│   ├── Deck.java
│   ├── Game.java
│   ├── PaperCard.java
│   ├── Player.java
│   ├── ScissorsCard.java
│   └── StoneCard.java
├── view/                 # Custom UI components
│   └── CardView.java
└── Main.java             # Application entry point
```

## 👥 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request


## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- Inspired by the classic Rock-Paper-Scissors game
- Thanks to the JavaFX community for their excellent documentation and examples
- Special thanks to all contributors and testers


---

*This project was created as a learning exercise and for entertainment purposes.*
