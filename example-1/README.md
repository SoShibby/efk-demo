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

## Kibana

Open the following url in your browser http://192.168.99.110:5601 to see the Kibana dashboard. If you are presented with a login form this means that Kibana hasn't connected with Elasticsearch yet. This can be Elasticsearch hasn't started yet so wait a few minutes to let Elasticsearch startup. Continue with the next step as soon as you can see the dashboard.

## Send Request

Open the following url in your browser: 

http://192.168.99.110:8080/myapp.tag?json={"foo":"bar"} 

This will send a request to Fluentd with the json object and Fluentd will send the json object to Elasticsearch. So if you now go to Kibana you should see a row with the property "foo" and value "bar".