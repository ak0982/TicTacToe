### try : https://tictactoe-production-b02e.up.railway.app/
# Multiplayer Tic-Tac-Toe Game

A real-time multiplayer Tic-Tac-Toe game built with Spring Boot, WebSocket, and Thymeleaf.

## Features

- Real-time multiplayer gameplay
- Beautiful responsive UI with animations
- Game room creation and joining
- Player turn management
- Win detection and game state tracking
- Real-time game state updates via WebSocket

## Architecture

### Backend Components

1. **Model Layer**
   - `GameState`: Core game logic and board state management
   - `GameRoom`: Game session management and player coordination
   - `Player`: Player information and symbol management
   - `GameMove`: Data transfer object for move requests

2. **Controller Layer**
   - `GameController`: HTTP endpoints for game actions
   - `WebSocketConfig`: WebSocket configuration for real-time updates

3. **Service Layer**
   - `GameService`: Game room management and state persistence

### Frontend Components

1. **Templates**
   - `home.html`: Landing page with game creation/joining
   - `game.html`: Main game interface

2. **JavaScript Modules**
   - WebSocket connection management
   - Game board interaction
   - Real-time state updates
   - Turn management
   - UI animations

### Communication Flow

1. **Game Creation**
   ```
   Client → POST /create → Server
   Server → Creates GameRoom → Generates Game ID
   Server → WebSocket → Broadcasts initial state
   ```

2. **Game Joining**
   ```
   Client → POST /join/{gameId} → Server
   Server → Updates GameRoom → Adds Player 2
   Server → WebSocket → Broadcasts updated state
   ```

3. **Game Start**
   ```
   Client → POST /start/{gameId} → Server
   Server → Updates GameRoom → Assigns symbols
   Server → WebSocket → Broadcasts game start
   ```

4. **Making Moves**
   ```
   Client → POST /app/move → Server
   Server → Validates move → Updates state
   Server → WebSocket → Broadcasts new state
   ```

## Implementation Details

### Turn Management

The turn system follows these rules:
1. X always goes first (controlled by `GameState.xIsNext`)
2. Players are assigned X/O when game starts
3. Turn validation checks:
   - Game has started
   - It's the player's turn
   - Move is valid
   - Cell is empty

### State Management

1. **Game Room State**
   - Unique game ID
   - Two players (name, ID, symbol)
   - Game started flag
   - Current game state

2. **Game State**
   - 9-cell board array
   - Current turn tracker
   - Winner tracking
   - Winning line positions

3. **Player State**
   - Unique ID (UUID)
   - Display name
   - Game symbol (X/O)

### Real-time Updates

1. **WebSocket Subscriptions**
   ```javascript
   /topic/game/{gameId} → Game state updates
   ```

2. **Broadcast Events**
   - Player joins
   - Game starts
   - Moves made
   - Game ends

### Security Considerations

1. **Input Validation**
   - Game ID format checking
   - Move position bounds checking
   - Player turn validation

2. **State Protection**
   - Server-side turn validation
   - Move validity checking
   - Game state consistency checks

## Technical Details

### Data Structures

1. **Game Board**
   ```java
   String[] board = new String[9];
   // null = empty, "X" = X move, "O" = O move
   ```

2. **Winning Combinations**
   ```java
   int[][] lines = {
       {0, 1, 2}, {3, 4, 5}, {6, 7, 8},  // Rows
       {0, 3, 6}, {1, 4, 7}, {2, 5, 8},  // Columns
       {0, 4, 8}, {2, 4, 6}              // Diagonals
   };
   ```

### Key Algorithms

1. **Win Detection**
   ```java
   for (int[] line : lines) {
       if (a != null && a.equals(b) && a.equals(c)) {
           winner = a;
           winningLine = line;
       }
   }
   ```

2. **Turn Validation**
   ```java
   boolean isValidTurn = (isXTurn && isPlayer1 && player1HasX) ||
                        (isXTurn && isPlayer2 && !player1HasX) ||
                        (!isXTurn && isPlayer1 && !player1HasX) ||
                        (!isXTurn && isPlayer2 && player1HasX);
   ```

## Development Practices

1. **Logging**
   - Comprehensive game state logging
   - Move validation logging
   - Turn management logging
   - WebSocket communication logging

2. **Error Handling**
   - Invalid move handling
   - Turn violation handling
   - Connection error handling
   - Game state inconsistency handling

3. **Code Organization**
   - Clear separation of concerns
   - Model-View-Controller pattern
   - Stateless HTTP endpoints
   - Stateful WebSocket connections

## Future Enhancements

1. **Gameplay Features**
   - Game history tracking
   - Player statistics
   - Multiple game rooms
   - Spectator mode

2. **Technical Improvements**
   - State persistence
   - Player reconnection
   - Game replay
   - Chat functionality

3. **UI Enhancements**
   - Sound effects
   - More animations
   - Responsive design improvements
   - Theme customization

## Tech Stack

- Backend:
  - Java 17+
  - Spring Boot 2.7.0
  - Spring WebSocket
  - Spring MVC
  
- Frontend:
  - HTML5
  - CSS3 with animations
  - JavaScript (Vanilla)
  - WebSocket (STOMP)
  - Thymeleaf templating

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Setup & Running

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd TicTacToe
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. Access the game:
   - Open your browser and navigate to `http://localhost:8080`
   - Create a new game or join an existing one using the game code

## How to Play

1. **Create a Game**:
   - Enter your name and click "Create Game"
   - Share the game code with your opponent

2. **Join a Game**:
   - Enter your name and the game code
   - Click "Join Game"

3. **Start Playing**:
   - Once both players join, the "Play" button appears
   - The player who clicks "Play" gets to play as X and goes first
   - Players take turns placing their symbols (X or O)
   - First player to get 3 in a row (horizontal, vertical, or diagonal) wins

## Project Structure

```
src/main/
├── java/com/game/
│   ├── controller/
│   │   ├── GameController.java    # Main game controller
│   │   └── WebSocketConfig.java   # WebSocket configuration
│   ├── model/
│   │   ├── GameRoom.java         # Game room management
│   │   ├── GameState.java        # Game state tracking
│   │   ├── GameMove.java         # Move data structure
│   │   └── Player.java           # Player information
│   └── TicTacToeApplication.java  # Main application class
│
└── resources/
    ├── templates/
    │   ├── home.html             # Landing page
    │   └── game.html             # Game interface
    └── application.properties     # Application configuration
```

## Current State

- Basic game functionality is working
- Players can create and join games
- Turn management system implemented
- Real-time updates working via WebSocket
- Win detection implemented
- UI animations and styling complete

## Known Issues

- Port 8080 must be available for the server to start
- Browser refresh may cause game state loss
- Some edge cases in turn management being addressed

## Future Improvements

- Add game history
- Implement player statistics
- Add sound effects
- Add chat functionality
- Support for multiple simultaneous games
- Persistent game state
- Player reconnection handling

## How It Works

### Application Stack Explanation

1. **Spring Boot Framework**
   - Purpose: Provides the foundation for building stand-alone, production-grade Spring applications
   - Key Features Used:
     - Auto-configuration of application components
     - Embedded Tomcat server
     - Dependency injection
     - Application lifecycle management
   - Why We Use It: Simplifies setup and reduces boilerplate code

2. **Spring WebSocket**
   - Purpose: Enables real-time bidirectional communication between clients and server
   - Components:
     - STOMP (Simple Text Oriented Messaging Protocol) for message handling
     - SockJS for WebSocket fallback support
   - Why We Use It: Essential for real-time game state updates

3. **Spring MVC**
   - Purpose: Handles HTTP requests and responses
   - Key Features Used:
     - RESTful endpoint mapping
     - Request body parsing
     - Response serialization
   - Why We Use It: Manages game creation, joining, and move submissions

4. **Thymeleaf**
   - Purpose: Server-side Java template engine
   - Key Features Used:
     - HTML5 template processing
     - Dynamic content injection
     - Expression language support
   - Why We Use It: Renders dynamic game UI and integrates with Spring

### Application Flow

1. **Server Startup**
   ```
   SpringBootApplication
   ├── Initializes Spring context
   ├── Configures WebSocket
   ├── Sets up message broker
   └── Starts embedded Tomcat server
   ```

2. **Game Creation**
   ```
   Client Request
   ├── POST /create
   ├── GameController processes request
   ├── Creates new GameRoom instance
   └── Returns game ID to client
   ```

3. **WebSocket Connection**
   ```
   Client
   ├── Connects to /ws endpoint
   ├── Subscribes to /topic/game/{gameId}
   └── Ready for real-time updates
   ```

4. **Game State Management**
   ```
   Move Made
   ├── Client sends move to /app/move
   ├── GameController validates move
   ├── Updates GameState
   └── Broadcasts to all subscribers
   ```

### Library Dependencies

1. **Core Dependencies**
   ```xml
   <!-- Spring Boot Starter Web -->
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-web</artifactId>
       <!-- Provides: -->
       <!-- - Embedded Tomcat -->
       <!-- - Spring MVC -->
       <!-- - JSON processing -->
       <!-- - Basic security -->
   </dependency>

   <!-- Spring Boot Starter WebSocket -->
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-websocket</artifactId>
       <!-- Provides: -->
       <!-- - WebSocket support -->
       <!-- - STOMP messaging -->
       <!-- - SockJS fallback -->
   </dependency>

   <!-- Thymeleaf Template Engine -->
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-thymeleaf</artifactId>
       <!-- Provides: -->
       <!-- - HTML template processing -->
       <!-- - Spring integration -->
       <!-- - Expression language -->
   </dependency>
   ```

2. **Development Tools**
   ```xml
   <!-- Spring Boot DevTools -->
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-devtools</artifactId>
       <!-- Provides: -->
       <!-- - Auto restart -->
       <!-- - Live reload -->
       <!-- - Property defaults -->
   </dependency>
   ```

### Key Components and Their Roles

1. **Spring Boot**
   - Application bootstrap and configuration
   - Dependency management
   - Production-ready features
   - Metrics and health checks

2. **Spring WebSocket**
   - Real-time communication
   - Message routing
   - Connection management
   - Client session handling

3. **Spring MVC**
   - HTTP request handling
   - REST endpoint mapping
   - Request/response processing
   - Error handling

4. **Thymeleaf**
   - HTML template processing
   - Dynamic content rendering
   - Layout management
   - Spring integration

### Development Environment

1. **Required Tools**
   - JDK 17 or higher
   - Maven 3.6+
   - Modern web browser
   - IDE with Spring support

2. **Build Process**
   ```bash
   # Clean and compile
   mvn clean compile

   # Run tests
   mvn test

   # Package application
   mvn package

   # Run application
   mvn spring-boot:run
   ```

3. **Development Features**
   - Hot reload support
   - Debug logging
   - In-memory state management
   - CORS configuration

### Production Considerations

1. **Deployment**
   - Can be deployed as JAR
   - Runs on any Java-compatible server
   - Configurable port and context path
   - Environment-specific properties

2. **Scaling**
   - Stateful WebSocket connections
   - Session affinity requirements
   - Memory usage considerations
   - Connection pooling

3. **Monitoring**
   - Spring Boot Actuator endpoints
   - JVM metrics
   - Application health
   - WebSocket connection stats 
