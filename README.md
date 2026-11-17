# Lottery Application

## Group Members

| # | Full Name | Student ID |
|---|---|---|
| 1 | Kumala Adugna | Ugr/37298/17 |
| 2 | Shimelis Regassa | Ugr/37774/17 |
| 3 | Ziyad Areba | Ugr/38058/17 |

---

**Course**: Advanced Programming  
**Institution**: Adama Science and Technology University  
**Department**: Computer Science and Engineering  
**Assignment**: Lab Assignment I

---

A menu-driven **Lottery Application** built with **Java**, developed as Lab Assignment I for the Advanced Programming course at Adama Science and Technology University.

## Features

| # | Feature |
|---|---|
| 1 | Register participants with unique IDs |
| 2 | Select lottery numbers (validated range, no duplicates) |
| 3 | Randomly generate winning numbers |
| 4 | Identify and display winners |
| 5 | Display full lottery results |
| 6 | Search participant by ID |

## Project Structure

```
LotteryApplication/
├── src/
│   ├── Participant.java          # Model class (OOP, encapsulation)
│   └── LotteryApplication.java  # Main app — all features + menu
├── data/
│   └── participants.txt         # Persistent participant storage
├── out/                         # Compiled .class files (git-ignored)
├── .gitignore
└── README.md
```

## Java Concepts Used

- **OOP** — Classes, objects, encapsulation, getters/setters
- **Java Collection Framework** — `HashMap`, `ArrayList`, `HashSet`
- **File Handling** — `BufferedReader` / `BufferedWriter`
- **Exception Handling** — `try-catch` for I/O and input errors

## How to Run

### 1. Compile

```bash
javac -d out src/Participant.java src/LotteryApplication.java
```

### 2. Run

```bash
java -cp out LotteryApplication
```

## Lottery Rules

| Setting | Value |
|---|---|
| Numbers per participant | 6 |
| Number range | 1 – 49 |
| Winning numbers | 6 (randomly generated) |

## Course Info

- **Course**: Advanced Programming  
- **Institution**: Adama Science and Technology University  
- **Department**: Computer Science and Engineering
