FROM node:lts-alpine as  build
WORKDIR /home/app
COPY public /home/app/public
COPY src /home/app/src
COPY package.json /home/app
COPY tsconfig.json /home/app
COPY .eslintrc.js /home/app
RUN npm install --save @popperjs/core
COPY .env /home/app
RUN npm install && npm run build


FROM nginx:1.21.3-alpine
COPY ./docker/nginx.conf /etc/nginx/
COPY --from=build /home/app/dist /usr/share/nginx/html
EXPOSE 80
