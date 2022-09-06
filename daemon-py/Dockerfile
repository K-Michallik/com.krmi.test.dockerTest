FROM python:3.10-slim-bullseye

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

ENV APP_NAME dockerdaemon
WORKDIR /usr/local/src/${APP_NAME}
COPY src/hello-world.py .

ENTRYPOINT ["python", "hello-world.py" ]

EXPOSE 40405
