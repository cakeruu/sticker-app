# Sticker Collection App

A digital sticker album collection platform where users can create albums, collect stickers, trade with other users, and participate in community forums.

## Features

- **Albums & Collections**: Create custom sticker albums with sections and collect stickers
- **Trading System**: Propose exchanges and trade stickers with other collectors  
- **Marketplace**: Buy and sell stickers through sale offers
- **Forums**: Join community discussions about collections
- **Real-time Messaging**: Chat with other users in real-time
- **User Management**: Profile management, user blocking, and authentication

## Tech Stack

- **Backend**: Spring Boot (Java 17), H2 Database, WebSocket
- **Frontend**: Next.js 15, React, TypeScript, Tailwind CSS
- **Authentication**: JWT tokens
- **Real-time**: WebSocket for messaging

## Project Setup Guide
### Prerequisites
#### Installing Node.js
##### Windows:

Visit the official Node.js website: []()https://nodejs.org/
Download the Windows Installer (.msi)
Run the downloaded installer
Follow the installation wizard:

Accept the license agreement
Choose the installation directory
Select the components you want to install
Click "Install"


Verify the installation by opening Command Prompt and running:
```
node --version
npm --version
```

##### macOS:

Using Homebrew (recommended):
If you don't have Homebrew installed, first install it
```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```
Install Node.js
```bash
brew install node
```
Alternatively, download from the official Node.js website:

Visit []()https://nodejs.org/
Download the macOS Installer
Open the downloaded .pkg file
Follow the installation wizard


Verify the installation in Terminal:
```bash
node --version
npm --version
```

#### Installing PNPM
##### Windows:

Open Command Prompt as Administrator
Run the following command:
```
npm install -g pnpm
```

##### macOS:
```bash
// Using npm
npm install -g pnpm

// Or using Homebrew
brew install pnpm
```
### Project Setup
#### 1. Start the Spring Boot API

Navigate to the project root directory
Find and run the Spring Boot application

#### 2. Populate Database

Open the src/main/resources/HttpTests/front.http file
Execute the HTTP requests in this file to populate the database
**Note:** Here's a screenshot showing where to execute the HTTP file:
![Database Icon](https://i.ibb.co/CbTHdzk/Captura-de-pantalla-2024-12-04-194204.png)

#### 3. Prepare the Frontend

Open a terminal in the front directory of the project

Install dependecies

````bash
pnpm install
````

Build the project:
````bash
pnpm run build
````
*Note: This might take a minute or so*

#### 4. Start the Frontend
````bash
pnpm run start
````
### Accessing the Application

**Application URL:** []()http://localhost:3000
**API URL:** []()http://localhost:8080

#### Test Accounts

**Primary Account:**

Email: acanals03@gmail.com
Password: 123456


**Secondary Account (for testing real-time messaging):**

Email: john.doe@example.com
Password: 123456



**Tip:** Open the application in two different browser windows (or use private/incognito mode) to test the real-time messaging feature with different accounts.
### Troubleshooting

Ensure all prerequisites are installed correctly
Check that no other applications are using ports 3000 or 8080
Verify that you're running the latest versions of Node.js and PNPM
If you encounter any issues, please check the project documentation or open an issue in the repository

### Support
For any questions or issues, feel free to contact at:
```
acanalsm@edu.tecnocampus.cat
```

