#!/bin/bash
set -euo pipefail
docker build -f docker/Dockerfile       -t flyingtoaster0/mind-mcp:latest        .
docker build -f docker/Dockerfile.Sse   -t flyingtoaster0/mind-mcp:sse-latest    .
docker build -f docker/Dockerfile.Stdio -t flyingtoaster0/mind-mcp:stdio-latest  .
