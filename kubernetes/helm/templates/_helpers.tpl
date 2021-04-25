{{/*
Expand the name of the chart.
*/}}
{{- define "poc-spring.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "poc-spring.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "poc-spring.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "poc-spring.labels" -}}
helm.sh/chart: {{ include "poc-spring.chart" . }}
{{ include "poc-spring.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "poc-spring.selectorLabels" -}}
app.kubernetes.io/name: {{ include "poc-spring.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "poc-spring.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "poc-spring.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{- define "poc-spring.consulEnvirementConfig" -}}
{{- if .Values.consul.enabled -}}
- name: SPRING_PROFILES_ACTIVE
  value: prod, consul
- name: SPRING_CLOUD_CONSUL_HOST
  valueFrom:
    configMapKeyRef:
      name: consul-config
      key: SPRING_CLOUD_CONSUL_HOST
- name: SPRING_CLOUD_CONSUL_PORT
  valueFrom:
    configMapKeyRef:
      name: consul-config
      key: SPRING_CLOUD_CONSUL_PORT
- name: SPRING_CLOUD_CONSUL_DISCOVERY_HOSTNAME
  value: customer
- name: SPRING_CLOUD_CONSUL_DISCOVERY_PORT
  value: '80'
{{- else -}}
- name: SPRING_PROFILES_ACTIVE
  value: prod, kubernetes
{{- end -}}
{{- end -}}

{{- define "poc-spring.keycloak.url" -}}
{{- if .Values.keycloak.ingress.enabled -}}
{{- with index .Values.keycloak.ingress.rules 0 -}}
http{{ if $.Values.keycloak.ingress.tls }}s{{ end }}://{{ .host }}/
{{- end -}}
{{- else -}}
{{- .Values.keycloak.url -}}
{{- end }}
{{- end }}