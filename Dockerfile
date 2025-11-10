FROM amazoncorretto:17-alpine

# Install Xvfb + fonts + bash
RUN apk add --no-cache xvfb xauth xset fontconfig ttf-dejavu bash

WORKDIR /app
COPY target/snake-game-1.0.jar app.jar

# Start virtual display and run game
ENTRYPOINT ["/bin/bash", "-c", "Xvfb :99 -screen 0 1024x768x16 & export DISPLAY=:99 && java -jar app.jar"]
