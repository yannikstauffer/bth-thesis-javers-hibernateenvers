FROM debian:stable

ADD "https://apt.corretto.aws/corretto.key" /corretto.key

RUN echo "Acquire::http::Pipeline-Depth 0;" > /etc/apt/apt.conf.d/99custom && \
    echo "Acquire::http::No-Cache true;" >> /etc/apt/apt.conf.d/99custom && \
    echo "Acquire::BrokenProxy    true;" >> /etc/apt/apt.conf.d/99custom && \
    apt-get update && \
    apt-get install -y bash ca-certificates cgroup-tools gpg maven sudo && \
    gpg --dearmor -o /usr/share/keyrings/corretto-keyring.gpg /corretto.key && \
    echo "deb [signed-by=/usr/share/keyrings/corretto-keyring.gpg] https://apt.corretto.aws stable main" | tee /etc/apt/sources.list.d/corretto.list && \
    rm /corretto.key &&  \
    apt-get update && \
    apt-get install -y java-23-amazon-corretto-jdk && \
    rm -rf /var/lib/apt/lists/*

ENTRYPOINT ["/bin/bash"]
