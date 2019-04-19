
# Use the docker image node:11.13
FROM node:11.13
# set working directory
RUN mkdir /app
WORKDIR /app
# It will copy the existing files to the `/app` directory.
COPY . /app
# Run npm install
RUN npm install --silent
RUN npm install react-scripts
# Start the app.
CMD npm start --production
# Expose the port of the app thats running in the container.
EXPOSE 3000
