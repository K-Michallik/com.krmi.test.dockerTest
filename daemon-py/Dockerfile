FROM i386/python:3.10-slim-buster
#FROM i386/python:3.10-alpine

COPY requirements.txt ./
#RUN pip install --no-cache-dir -r requirements.txt
#Need to get dependencies for wheel to install requirements. Cleans up build tools after installing.
RUN apt-get update && \
    apt-get install -y \
        build-essential \
        make \
        gcc \
    && pip install --no-cache-dir -r requirements.txt \
    && apt-get remove -y --purge make gcc build-essential \
    && apt-get autoremove -y \
    && rm -rf /var/lib/apt/lists/*

ENV APP_NAME dockerdaemon
WORKDIR /usr/local/src/${APP_NAME}
COPY src/modbus_xmlrpc.py .
# HEALTHCHECK CMD curl --fail http://localhost:40408 || exit 1  

ENTRYPOINT ["python", "modbus_xmlrpc.py" ]

EXPOSE 40408
