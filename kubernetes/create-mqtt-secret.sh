#!/bin/bash

MQTT_USER="mqtt_user"
MQTT_PASSWORD="mqtt_password"
SECRET_NAME="mqtt-secret"
NAMESPACE="iot"

kubectl create secret generic $SECRET_NAME \
  --from-literal=username=$MQTT_USER \
  --from-literal=password=$MQTT_PASSWORD \
  -n $NAMESPACE

echo "Secret '$SECRET_NAME' utworzony w namespace '$NAMESPACE'"
