#!/usr/bin/env bash
docker exec -ti broker /usr/bin/kafka-console-consumer --bootstrap-server broker:9092 --topic xrp

