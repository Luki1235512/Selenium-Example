FROM maven:3.9-eclipse-temurin-21

RUN apt-get update && \
    apt-get install -y wget gnupg xz-utils libgtk-3-0 libdbus-glib-1-2 libasound2t64 libx11-xcb1 && \
    wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /usr/share/keyrings/google-chrome.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-chrome.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && \
    apt-get install -y google-chrome-stable && \
    wget -O /tmp/firefox-installer "https://download.mozilla.org/?product=firefox-esr-latest&os=linux64&lang=en-US" && \
    tar -xf /tmp/firefox-installer -C /opt/ && \
    ln -s /opt/firefox/firefox /usr/local/bin/firefox && \
    rm -f /tmp/firefox-installer && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY . .

CMD ["mvn", "test"]
