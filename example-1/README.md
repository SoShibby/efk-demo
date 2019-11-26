## Create Docker Machine

Create a new Docker virtual machine with the name "java-kompetensgrupp" (note: this assumes that you are using docker-machine with virtualbox).
```sh
docker-machine create -d virtualbox --virtualbox-memory 4096 java-kompetensgrupp
```

Set Docker environment variables:
```sh
docker-machine env java-kompetensgrupp
```

Now copy all the commands that you got in the output of the previous command and execute them to set the environment variables.

Now if you run the following command you should get the client and server version (if you get an error here, you have done something wrong in the previous steps): 
```sh
docker version
```

## Build Fluentd Dockerfile

Run the following command to build Fluentd:
```sh
docker-compose build
```

## Start Services

Run the following command to start Elasticsearch, Fluentd, Kibana:
```sh
docker-compose up
```

## Docker Machine IP

Get the ip of your Docker virtual machine by running:
```sh
docker-machine ip
```

You will need this ip in the next steps.

## Kibana

Note: It might take a few minutes for all services to start up. So if the Kibana dashboard fails to load in the next step it might be that all services hasn't started yet. 

Open the following url in your browser http://YOUR-DOCKER-MACHINE-IP:5601 to see the Kibana dashboard. If you are presented with a login form this means that Kibana hasn't connected with Elasticsearch yet. This can be Elasticsearch hasn't started yet so wait a few minutes to let Elasticsearch startup. Continue with the next step as soon as you can see the dashboard.

## Send Request

Open the following url in your browser: 

http://YOUR-DOCKER-MACHINE-IP:8080/myapp.tag?json={"foo":"bar"} 

This will send a request to Fluentd with the json object and Fluentd will send the json object to Elasticsearch. So if you now go to Kibana you should see a row with the property "foo" and value "bar".