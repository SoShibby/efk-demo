## Build Spring-boot application

```sh
./demo-app/gradlew build
```

## Build docker files

```sh
docker-compose build
```

## Start services

```sh
docker-compose up
```

## Kibana

Open the Kibana dashboard at http://192.168.99.110:5601. If you are presented with a login form this means that Kibana haven't connected with elasticsearch yet. This can be because of an error or that elasticsearch haven't started yet.

## Send request

Visit http://192.168.99.110:8090/api/ping and row with the field name "log.msg" should show up with the value "Pong" in Kibana.

### Debugging

While debugging in might be easier to start each service by themselves in their own shell so the output don't get mangled together.

```sh
docker-compose up elasticsearch
docker-compose up kibana
docker-compose up fluentd
docker-compose up demo-app
```

### Kibana Scripted Fields

open-in-vscode: return doc['log.logger.keyword'].size() > 0 ? "vscode://file/c:/repo/" + doc['log.build.git-project-name.keyword'].value + doc['log.project-relative-path.keyword'].value + "/" + doc['log.build.name.keyword'].value + "/src/main/java/" + doc['log.logger.keyword'].value.replace(".", "/") + ".java:" + String.valueOf(doc['log.caller_line_number'].value) : "";

git-checkout-revision: return doc['log.build.commit-id.keyword'].size() > 0 ? "git checkout " + doc['log.build.commit-id.keyword'].value : "";

git-clone: return doc['log.build.git-group-path.keyword'].size() > 0 ? "https://github.com/" + doc['log.build.git-group-path.keyword'].value + "/" + doc['log.build.git-project-name.keyword'].value + ".git" : "";

open-in-github: return doc['log.logger.keyword'].size() > 0 ? "https://github.com/" + doc['log.build.git-group-path.keyword'].value + "/" + doc['log.build.git-project-name.keyword'].value + "/blob/" + doc['log.build.commit-id.keyword'].value + doc['log.project-relative-path.keyword'].value + "/" + doc['log.build.name.keyword'].value + "/src/main/java/" + doc['log.logger.keyword'].value.replace(".", "/") + ".java#L" + String.valueOf(doc['log.caller_line_number'].value) : "";

