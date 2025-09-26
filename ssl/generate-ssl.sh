#!/bin/bash

# Script para gerar certificados SSL auto-assinados para desenvolvimento
# Para produção, use certificados de uma CA confiável (Let's Encrypt, etc.)

set -e

SSL_DIR="$(dirname "$0")"
CERT_FILE="$SSL_DIR/cert.pem"
KEY_FILE="$SSL_DIR/key.pem"

echo "Gerando certificados SSL auto-assinados..."

# Criar diretório se não existir
mkdir -p "$SSL_DIR"

# Gerar chave privada
openssl genrsa -out "$KEY_FILE" 2048

# Gerar certificado auto-assinado
openssl req -new -x509 -key "$KEY_FILE" -out "$CERT_FILE" -days 365 -subj "/C=BR/ST=SP/L=SaoPaulo/O=Portfolio/OU=IT/CN=localhost"

# Definir permissões corretas
chmod 600 "$KEY_FILE"
chmod 644 "$CERT_FILE"

echo "Certificados gerados com sucesso:"
echo "  Certificado: $CERT_FILE"
echo "  Chave privada: $KEY_FILE"
echo ""
echo "ATENÇÃO: Estes são certificados auto-assinados para desenvolvimento."
echo "Para produção, substitua por certificados de uma CA confiável."
