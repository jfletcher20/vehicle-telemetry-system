FROM amd64/eclipse-temurin:21 AS builder

EXPOSE 8080

RUN mkdir -p app

WORKDIR /app
COPY jfletcher20_vjezba_07_dz_2_app /app
RUN chmod 777 cs.sh

CMD ["./cs.sh"]