FROM azul/zulu-openjdk-debian:23.0.2-jdk


RUN echo "Acquire::http::Pipeline-Depth 0;" > /etc/apt/apt.conf.d/99custom && \
    echo "Acquire::http::No-Cache true;" >> /etc/apt/apt.conf.d/99custom && \
    echo "Acquire::BrokenProxy    true;" >> /etc/apt/apt.conf.d/99custom && \
    apt-get update && \
    apt-get install -y bash maven sudo && \
    rm -rf /var/lib/apt/lists/*

ENTRYPOINT ["/bin/bash"]
