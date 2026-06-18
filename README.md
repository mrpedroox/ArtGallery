# ArtGallery

A desktop application for managing artworks, built in Java with a Swing GUI and file-based persistence.

---

## Table of Contents

- [About](#about)
- [Features](#features)
- [Directory Structure](#directory-structure)
- [Architecture](#architecture)
- [Requirements](#requirements)
- [How to Run](#how-to-run)
- [Data Persistence](#data-persistence)
- [Exceptions](#exceptions)

---

## About

ArtGallery is a desktop application developed in pure Java with a Swing graphical interface. It allows users to manage artworks, expositions, evaluations, and rankings, following the principles of **Object-Oriented Programming (OOP)** with a layered architecture.

---

## Features

- **Artworks** — create, list, search, edit, and remove artworks
- **Expositions** — manage expositions linked to artworks
- **Evaluations** — register and consult artwork evaluations
- **Search** — search artworks by different criteria
- **Ranking** — view the highest-rated artworks
- **Persistence** — all data is saved to files and retained between sessions

---

## Directory Structure

```
ArtGallery/
|
+-- src/
|   +-- artgallery/
|       |
|       +-- exception/                        # Custom exceptions
|       |   +-- NotaInvalidaException.java
|       |   +-- ObraJaCadastradaException.java
|       |   +-- ObraNaoEncontradaException.java
|       |
|       +-- interfaces/                       # Layer contracts
|       |   +-- IArtGallery.java
|       |   +-- IRepositorioObra.java
|       |
|       +-- model/                            # Domain entities
|       |   +-- Obra.java                     # Base class (abstract)
|       |   +-- ArteGenerativa.java           # Subclass of Obra
|       |   +-- Avaliacao.java
|       |   +-- Exposicao.java
|       |   +-- Modelagem3D.java              # Subclass of Obra
|       |   +-- PinturaDigital.java           # Subclass of Obra
|       |
|       +-- persistencia/                     # File read and write
|       |   +-- PersistenciaArquivo.java
|       |
|       +-- repository/                       # Data access
|       |   +-- RepositorioObra.java          # Implements IRepositorioObra
|       |
|       +-- service/                          # Business logic
|       |   +-- ArtGallery.java               # Implements IArtGallery
|       |
|       +-- ui/                               # Swing graphical interface
|           +-- MainFrame.java                # Main window
|           +-- ObraPanel.java
|           +-- ExposicaoPanel.java
|           +-- AvaliacaoPanel.java
|           +-- BuscaPanel.java
|           +-- RankingPanel.java
|
+-- dados/                                    # Persistence files
```

---

## Architecture

The project follows a layered **MVC + Repository** architecture, applying the core pillars of OOP:

```
UI (View)
   |
Service (ArtGallery)              <- business rules
   |
Repository (RepositorioObra)      <- data access
   |
Persistencia (PersistenciaArquivo) <- file read/write
```

**OOP concepts applied:**

| Concept | Where it is applied |
|---|---|
| Inheritance | `ArteGenerativa`, `Modelagem3D`, and `PinturaDigital` extend `Obra` |
| Polymorphism | Artworks handled generically through the base class `Obra` |
| Encapsulation | Private attributes with getters/setters across all model classes |
| Abstraction | Interfaces `IArtGallery` and `IRepositorioObra` define the contracts |
| Exceptions | Custom exception hierarchy under `exception/` |

---

## Requirements

- **Java JDK 11** or higher
- An IDE such as [IntelliJ IDEA](https://www.jetbrains.com/idea/) (recommended) or Eclipse

To check your Java version:

```bash
java -version
```

---

## How to Run

### Via IntelliJ IDEA

1. Clone or download the repository
2. Open the project in IntelliJ: `File -> Open -> select the ArtGallery folder`
3. Wait for the project to be indexed
4. Locate the `MainFrame` class under `src/artgallery/ui/`
5. Right-click -> `Run 'MainFrame'`

### Via command line

```bash
# From the project root, compile:
javac -d out src/artgallery/**/*.java

# Run:
java -cp out artgallery.ui.MainFrame
```

---

## Data Persistence

Data is saved automatically to the `dados/` folder at the project root. Persistence is handled by the `PersistenciaArquivo` class, which serializes and deserializes objects to and from files.

The `dados/` folder is created automatically on the first run — there is no need to create it manually.

> Warning: Do not manually edit the files inside `dados/`, as doing so may corrupt the application data.

---

## Exceptions

The system defines custom exceptions to handle domain-specific errors:

| Exception | When it is thrown |
|---|---|
| `ObraJaCadastradaException` | When trying to register an artwork with an already existing identifier |
| `ObraNaoEncontradaException` | When searching, editing, or removing a non-existent artwork |
| `NotaInvalidaException` | When registering an evaluation with a score outside the allowed range |
