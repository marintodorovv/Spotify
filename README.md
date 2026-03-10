# Spotify

## Introduction

[`Spotify`] is a music streaming platform that provides users with access to millions of songs by artists from around the world.

> `Stream`-ing is a method of data transmission, commonly used for multimedia files. With this method, playback of the content on the user’s device begins as soon as it is accessed, without the need to first download it as a file and then open it in a suitable player. Data transmission occurs simultaneously with playback, which is why a constant internet connection is required.

## Architecture

Client-server application using Java NIO.

### Server Components

**SpotifyServer**
- Main server class using `Selector` and `ServerSocketChannel`
- Handles multiple clients concurrently using non-blocking I/O
- Manages client connections and delegates commands to the command executor

**Command Layer**
- Command pattern implementation
- Each command (register, login, search, play, etc.) is a separate class
- CommandExecutor processes commands and returns responses

**Service Layer**
- UserService: User authentication and management
- SongService: Song search and retrieval
- PlaylistService: Playlist operations
- StreamingService: Audio streaming to clients

**Repository Layer**
- UserRepository: JSON-based user storage (`data/users.json`)
- PlaylistRepository: JSON-based playlist storage (`data/playlists.json`)
- SongRepository: File-based song storage (`data/`)

### Client Components

**SpotifyClient**
- Main client with command-line interface
- Connects to server using `SocketChannel`
- Reads user input and sends commands to server

**AudioReceiveThread**
- Separate thread for receiving audio data from server
- Plays audio using Java Sound API
- Runs concurrently with main client thread

## Concurrent Execution

### Server Concurrency

**Non-blocking I/O with Selector**
- Single-threaded server handles multiple clients using NIO Selector


**Per-Client Context**
- Each client connection has associated context stored in `SelectionKey.attachment()`
- `ConnectionContext`: Tracks buffers and connection state
- `UserContext`: Tracks logged-in user and active streaming

**Streaming Service**
- Manages concurrent audio streaming to multiple clients
- Uses separate map to track which clients are currently receiving audio
- Sends audio chunks to all active listeners for the same song

### Client Concurrency

**Main Thread**
- Handles user input
- Sends commands to server
- Receives and displays text responses

**Audio Thread**
- Dedicated thread for audio streaming
- Runs independently from main client thread
- Receives audio data on separate socket connection
- Plays audio through `SourceDataLine`

## Thread Safety and Correctness

**Server**
- Single selector thread eliminates race conditions for client I/O
- User and playlist repositories use synchronized methods where necessary
- Streaming service synchronizes access to client streaming map
- Each client's data isolation through per-connection context

**Client**
- Main thread and audio thread operate on separate socket connections
- No shared mutable state between threads
- Thread-safe start/stop mechanism for audio playback

## Project Structure

```
spotify/
├── client/
│   ├── SpotifyClient.java
│   ├── model/
│   └── streaming/
│       └── AudioReceiveThread.java
├── server/
│   ├── SpotifyServer.java
│   ├── command/
│   ├── context/
│   ├── model/
│   ├── reponse/
│   ├── repository/
│   ├── request/
│   ├── security/
│   ├── service/
│   ├── streaming/
│   └── validation/
├── common/
│   ├── AudioEncodingMapper.java
│   ├── AudioFormatDTO.java
│   └── CommandType.java
└── exception/
    ├── authentication/
    ├── network/
    ├── playlist/
    ├── repository/
    ├── security/
    ├── song/
    └── validation/
```

## Setup

**Configuration:**
- Server port: 6260
- Data directory: `./data/`
- Log files: `spotify_server_logs.log` and `client_logs.log`

## Available Commands

```
register <email> <password>
login <email> <password>
search <keywords>
top <n>
create-playlist <n>
add-song-to <playlist> <song>
show-playlist <n>
play <song>
stop
disconnect
help
```
