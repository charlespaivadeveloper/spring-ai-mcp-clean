{{/*
Common labels applied to every resource.
*/}}
{{- define "spring-ai-mcp.labels" -}}
helm.sh/chart: {{ .Chart.Name }}-{{ .Chart.Version | replace "+" "_" }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}
