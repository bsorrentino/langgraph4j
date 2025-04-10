# Start from the official Node.js 22 image (Debian-based)
FROM node:22-slim

# Install dependencies and JRE 17 (Temurin / OpenJDK)
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    && rm -rf /var/lib/apt/lists/*

# Add Temurin JDK 17 repository and install JRE
RUN wget -O - https://packages.adoptium.net/artifactory/api/gpg/key/public | gpg --dearmor -o /usr/share/keyrings/adoptium-archive-keyring.gpg && \
    echo "deb [signed-by=/usr/share/keyrings/adoptium-archive-keyring.gpg] https://packages.adoptium.net/artifactory/deb bookworm main" | tee /etc/apt/sources.list.d/adoptium.list && \
    apt-get update && \
    apt-get install -y temurin-17-jre && \
    rm -rf /var/lib/apt/lists/*

# Check versions (optional)
RUN node -v && java -version

# Set working directory
WORKDIR /app


# Copy your Node.js app
COPY builder-webui/ .
ARG LANGRAPH4J_GEN
COPY target/${LANGRAPH4J_GEN} .

EXPOSE 3000

# Command to run your app
CMD ["node", "server.cjs"]