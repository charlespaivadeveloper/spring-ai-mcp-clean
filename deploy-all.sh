#!/usr/bin/env bash
# deploy-all.sh — Full KinD + Helm deployment for spring-ai-mcp-clean
# Usage: ./deploy-all.sh [--openai-key <key>]
#        OPENAI_API_KEY=sk-xxx ./deploy-all.sh

set -euo pipefail

CLUSTER_NAME="spring-ai-mcp"
NAMESPACE="spring-ai-mcp"
RELEASE_NAME="spring-ai-mcp"
CHART_DIR="helm/spring-ai-mcp"
REGISTRY="spring-ai-mcp"
IMAGE_TAG="1.0.0"
OPENAI_KEY="${OPENAI_API_KEY:-REPLACE_WITH_YOUR_OPENAI_API_KEY}"

while [[ $# -gt 0 ]]; do
  case $1 in
    --openai-key) OPENAI_KEY="$2"; shift 2 ;;
    *) echo "Unknown argument: $1"; exit 1 ;;
  esac
done

echo "=== Step 1: Create KinD cluster ==="
kind create cluster --config kind-config.yaml --name "${CLUSTER_NAME}" || echo "Cluster already exists, continuing..."

echo "=== Step 2: Resolve Helm dependencies ==="
helm repo add kong https://charts.konghq.com --force-update
helm dependency update "${CHART_DIR}"

echo "=== Step 4: Build JAR files ==="
./mvnw clean package -DskipTests

echo "=== Step 5: Build Docker images ==="
docker build -t "${REGISTRY}/person-mcp-service:${IMAGE_TAG}"  person-mcp-service/
docker build -t "${REGISTRY}/account-mcp-service:${IMAGE_TAG}" account-mcp-service/
docker build -t "${REGISTRY}/sample-client:${IMAGE_TAG}"       sample-client/

echo "=== Step 6: Load images into KinD ==="
kind load docker-image "${REGISTRY}/person-mcp-service:${IMAGE_TAG}"  --name "${CLUSTER_NAME}"
kind load docker-image "${REGISTRY}/account-mcp-service:${IMAGE_TAG}" --name "${CLUSTER_NAME}"
kind load docker-image "${REGISTRY}/sample-client:${IMAGE_TAG}"       --name "${CLUSTER_NAME}"

echo "=== Step 7: Helm install / upgrade ==="
helm upgrade --install "${RELEASE_NAME}" "${CHART_DIR}" \
  --namespace "${NAMESPACE}" \
  --create-namespace \
  --values "${CHART_DIR}/values-local.yaml" \
  --set secrets.openai.apiKey="${OPENAI_KEY}" \
  --wait \
  --timeout 5m

echo ""
echo "=== Deployment complete! ==="
echo "  Person MCP:    http://localhost:8060/actuator/health"
echo "  Account MCP:   http://localhost:8040/actuator/health"
echo "  Sample Client: http://localhost:8080/actuator/health"
echo ""
echo "  Try: curl http://localhost:8080/api/v1/persons/nationality/Germany"
echo ""
echo "  Helm status:   helm status ${RELEASE_NAME} -n ${NAMESPACE}"
echo "  Teardown:      helm uninstall ${RELEASE_NAME} -n ${NAMESPACE} && kind delete cluster --name ${CLUSTER_NAME}"
