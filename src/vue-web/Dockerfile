FROM node:16-alpine as build-stage

# Set the working directory
WORKDIR /web

# Copy npm and yarn files
COPY .npmrc package.json yarn.lock ./

# Install dependencies
RUN yarn install --frozen-lockfile

# Copy all files
COPY . .

# 构建命令
ARG NODE_ENV
RUN if [ "$NODE_ENV" = "production" ]; then \
      yarn build:prod; \
    else \
      yarn build; \
    fi

## -- nginx --
FROM nginx:alpine

ENV TZ=Asia/Shanghai

COPY ./nginx.conf /etc/nginx/conf.d/default.conf
# Copy build files from the previous stage
COPY --from=build-stage /web/dist /usr/share/nginx/html

EXPOSE 80