FROM rabbitmq:management

COPY rabbitmq.config /etc/rabbitmq/
COPY definitions.json /etc/rabbitmq/
RUN chmod 777 /etc/rabbitmq/rabbitmq.config /etc/rabbitmq/definitions.json && chown rabbitmq:rabbitmq /etc/rabbitmq/rabbitmq.config /etc/rabbitmq/definitions.json 

ENV RABBITMQ_USE_LONGNAME true
ENV RABBITMQ_LOGS /var/log/rabbitmq/rabbit.log
