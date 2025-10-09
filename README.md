# MIND MCP Server

A Model Context Protocol (MCP) server that provides an interface to [MIND](https://github.com/Casvt/MIND), a self-hosted reminder application. This server allows AI assistants and other MCP clients to create and manage reminders through MIND's API.

## What is MIND?

MIND is a simple self-hosted reminder application that can send push notifications to your device using the Apprise API. It supports scheduled reminders, static reminders, and can send notifications to 100+ platforms through Apprise integration.

## Features

- Create reminders with custom titles, messages, and scheduled times
- Configurable timezone support
- Redis-based caching for improved performance
- Multiple deployment options (Docker Compose, direct execution)
- SSE compatibility through SuperGateway wrapper for integrations like Home Assistant MCP

## Running with Docker Compose

The project includes several Docker Compose configurations in the `docker/` directory:

- `docker-compose.yml` - Standard configuration with Redis
- `docker-compose-with-server.yml` - Includes the MIND server itself
- `docker-compose-noredis.yml` - Runs without Redis caching
- `docker-compose-stdio.yml` - STDIO-compatible configuration with Redis

### Setup

1. Copy the example environment file:
   ```bash
   cp .example.env docker/.env
   ```

2. Edit `docker/.env` with your MIND server credentials:
   ```
   MIND_USERNAME=your_username
   MIND_PASSWORD=your_password
   MIND_BASE_URL=http://mind_host:mind_port
   MIND_TIMEZONE=America/Toronto
   NOTIFICATION_SERVICE_ID=1
   ```

3. Run with Docker Compose:
   ```bash
   cd docker
   docker-compose up
   ```

The MCP server will be available on port 8000 and wrapped with SuperGateway for SSE compatibility.

## Running Directly with Gradle

You can run the MCP server directly over stdio without Docker:

```bash
./gradlew :server:bootRun --args='--spring.profiles.active=noredis' \
  -PmainClass=co.flyingtoaster.mind.mcp_server.MindMcpServerApplicationKt
```

When running with Gradle, set the environment variables:
```bash
export MIND_USERNAME=your_username
export MIND_PASSWORD=your_password
export MIND_BASE_URL=http://mind_host:mind_port
export MIND_TIMEZONE=America/Toronto
export NOTIFICATION_SERVICE_ID=1
```

## Finding Your Notification Service ID

The `fetch-notification-services.sh` script helps you discover which notification services are configured on your MIND server. This script authenticates with your MIND server and retrieves the list of available notification services.

Usage:
```bash
./fetch-notification-services.sh username password http://mind_host:mind_port
```

The script will output the JSON response containing all configured notification services. Each service will have an ID that you can use for the `NOTIFICATION_SERVICE_ID` environment variable to specify which notification service should receive reminder notifications.

## Future Work

- Support for notifying multiple notification services simultaneously
- Add notification service management tools
- Allow specifying notification service when creating reminders
