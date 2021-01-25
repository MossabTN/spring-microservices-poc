kubectl create ns poc-spring

##### install postgresql
```bash
kubectl create secret generic init-postgres --from-file=../databases/data/postgres-entrypoint/init-postgres.sql -n poc-spring 
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install postgresql bitnami/postgresql -f values/postgresql.yml -n poc-spring --version 10.2.4
```

##### install mongodb
```bash
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install mongodb bitnami/mongodb -f values/mongodb.yml -n poc-spring --version 10.5.0
```

##### install consul
```bash
helm repo add hashicorp https://helm.releases.hashicorp.com
helm install consul hashicorp/consul -f values/consul.yml -n poc-spring --version 0.20.1
```

##### install kafka && schema registry
```bash
helm repo add confluentinc https://confluentinc.github.io/cp-helm-charts/
helm install kafka confluentinc/cp-helm-charts -f values/kafka.yml -n poc-spring --version 0.6.0
```

