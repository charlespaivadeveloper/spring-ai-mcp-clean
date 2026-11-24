#!/usr/bin/env bash
# deploy-local-gateway.sh — KinD + Helm deployment with Ollama + LiteLLM gateway inside cluster
# Usage: ./deploy-local-gateway.sh [--ollama-model <model>]
#
# Architecture: sample-client → LiteLLM (OpenAI API) → Ollama (local LLM)
#
# Memory requirements (configure Docker Desktop before running):
#   llama3.1 (8B):  ~5 GB model  — Docker Desktop RAM >= 8 GB recommended
#   llama3.2 (3B):  ~2 GB model  — Docker Desktop RAM >= 5 GB recommended
#   qwen2.5:3b:     ~2 GB model  — Docker Desktop RAM >= 5 GB recommended

set -euo pipefail

CLUSTER_NAME="spring-ai-mcp"
NAMESPACE="spring-ai-mcp"
RELEASE_NAME="spring-ai-mcp"
CHART_DIR="helm/spring-ai-mcp"
REGISTRY="spring-ai-mcp"
IMAGE_TAG="1.0.0"
OLLAMA_MODEL="llama3.1"

while [[ $# -gt 0 ]]; do
  case $1 in
    --ollama-model) OLLAMA_MODEL="$2"; shift 2 ;;
    *) echo "Unknown argument: $1"; exit 1 ;;
  esac
done

echo "=== Step 1: Create KinD cluster ==="
kind create cluster --config kind-config.yaml --name "${CLUSTER_NAME}" || echo "Cluster already exists, continuing..."

echo "=== Step 2: Resolve Helm dependencies ==="
helm repo add kong https://charts.konghq.com --force-update
helm dependency update "${CHART_DIR}"

echo "=== Step 3: Build JAR files ==="
./mvnw clean package -DskipTests

echo "=== Step 4: Build Docker images (app services only) ==="
docker build -t "${REGISTRY}/person-mcp-service:${IMAGE_TAG}"  person-mcp-service/
docker build -t "${REGISTRY}/account-mcp-service:${IMAGE_TAG}" account-mcp-service/
docker build -t "${REGISTRY}/sample-client:${IMAGE_TAG}"       sample-client/

echo "=== Step 5: Load app images into KinD ==="
echo "  (Ollama and LiteLLM are pulled from Docker Hub by the cluster)"
kind load docker-image "${REGISTRY}/person-mcp-service:${IMAGE_TAG}"  --name "${CLUSTER_NAME}"
kind load docker-image "${REGISTRY}/account-mcp-service:${IMAGE_TAG}" --name "${CLUSTER_NAME}"
kind load docker-image "${REGISTRY}/sample-client:${IMAGE_TAG}"       --name "${CLUSTER_NAME}"

echo "=== Step 6: Helm install / upgrade (gateway mode) ==="
helm upgrade --install "${RELEASE_NAME}" "${CHART_DIR}" \
  --namespace "${NAMESPACE}" \
  --create-namespace \
  --values "${CHART_DIR}/values-local.yaml" \
  --values "${CHART_DIR}/values-local-gateway.yaml" \
  --set ollama.model="${OLLAMA_MODEL}" \
  --wait \
  --timeout 10m

echo ""
echo "=== Deployment complete! (Kong gateway + LiteLLM → Ollama ${OLLAMA_MODEL}) ==="
echo ""
echo "  Note: first boot may take several minutes while Ollama pulls the model."
echo "  Watch progress: kubectl logs -n ${NAMESPACE} -l app=ollama -f"
echo ""
echo "  Kong gateway:  http://localhost:8000  (single entry point)"
echo "  Kong admin:    http://localhost:8001  (inspect routes, plugins)"
echo ""
echo "  Direct access (debug):"
echo "    Person MCP:    http://localhost:8060/actuator/health"
echo "    Account MCP:   http://localhost:8040/actuator/health"
echo "    Sample Client: http://localhost:8080/actuator/health"
echo ""
echo "  Try via Kong gateway:"
echo "    curl http://localhost:8000/api/v1/persons/nationality/Germany"
echo "  Try direct:"
echo "    curl http://localhost:8080/api/v1/persons/nationality/Germany"
echo ""
echo "  Helm status:   helm status ${RELEASE_NAME} -n ${NAMESPACE}"
echo "  Teardown:      helm uninstall ${RELEASE_NAME} -n ${NAMESPACE} && kind delete cluster --name ${CLUSTER_NAME}"
