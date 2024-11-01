# NGINX SETUP STAGE
FROM nginx:stable-alpine
COPY /build /usr/share/nginx/html
RUN apk add --no-cache certbot python3 py3-pip && pip3 install --upgrade pip && pip3 install certbot-nginx

RUN rm /etc/nginx/conf.d/default.conf
ARG DOMAIN
ARG EMAIL
# Отображение аргументов
RUN echo "Domain: $DOMAIN"
RUN echo "Email: $EMAIL"

COPY /nginx/nginx.conf /etc/nginx/conf.d
RUN sed -i "s/localhost/$DOMAIN/g" /etc/nginx/conf.d/nginx.conf

COPY /nginx/$DOMAIN/cert1.pem /etc/nginx/certs/certificate.crt
COPY /nginx/$DOMAIN/privkey1.pem /etc/nginx/certs/private_key.key

#certbot --nginx -d antifraud-test.symtech-cloud.kz --non-interactive --agree-tos --email dev@symtech.kz --no-eff-email --cert-name antifraud-test.symtech-cloud.kz --redirect
#docker cp front:/etc/letsencrypt/archive/antifraud-test.symtech-cloud.kz/ /home/ubuntu/
#cd /home/ubuntu/antifraud-test.symtech-cloud.kz/
#openssl pkcs12 -export -out antifraud-test.symtech-cloud.kz.p12 -inkey ./privkey1.pem -in cert1.pem -password pass:qwerty\$4
# Установка правильных разрешений на сертификат и ключ
RUN chmod 644 /etc/nginx/certs/certificate.crt
RUN chmod 644 /etc/nginx/certs/private_key.key

# Открытие портов 80 и 443
EXPOSE 80
EXPOSE 443

# Запуск Nginx
CMD ["nginx", "-g", "daemon off;"]
