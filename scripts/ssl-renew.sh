#!/bin/bash

# Script para renovar certificados Let's Encrypt
# Uso: ./scripts/ssl-renew.sh [domain]
# Exemplo: ./scripts/ssl-renew.sh api.seudominio.com

set -e

DOMAIN=${1:-"localhost"}
SSL_DIR="/etc/letsencrypt/live/$DOMAIN"
NGINX_SSL_DIR="./ssl"

echo "🔄 Renovando certificados SSL Let's Encrypt para $DOMAIN"

# Verificar se é domínio real
if [ "$DOMAIN" = "localhost" ]; then
    echo "🏠 Domínio localhost - não é necessário renovar certificados auto-assinados"
    exit 0
fi

# Verificar se certbot está instalado
if ! command -v certbot &> /dev/null; then
    echo "❌ Certbot não está instalado"
    exit 1
fi

# Verificar se os certificados existem
if [ ! -f "$SSL_DIR/fullchain.pem" ]; then
    echo "❌ Certificados não encontrados para $DOMAIN"
    exit 1
fi

# Verificar se os certificados precisam ser renovados (expirem em 30 dias)
if openssl x509 -in "$SSL_DIR/fullchain.pem" -noout -checkend 2592000 > /dev/null 2>&1; then
    echo "✅ Certificados são válidos por mais de 30 dias - não é necessário renovar"
    exit 0
fi

echo "⚠️  Certificados expiram em menos de 30 dias - renovando..."

# Parar nginx temporariamente
sudo systemctl stop nginx || true

# Renovar certificados
sudo certbot renew --standalone --non-interactive

# Verificar se a renovação foi bem-sucedida
if [ -f "$SSL_DIR/fullchain.pem" ] && [ -f "$SSL_DIR/privkey.pem" ]; then
    echo "✅ Certificados renovados com sucesso"
    
    # Copiar certificados para o diretório local
    sudo cp "$SSL_DIR/fullchain.pem" ssl/cert.pem
    sudo cp "$SSL_DIR/privkey.pem" ssl/key.pem
    sudo chown $USER:$USER ssl/cert.pem ssl/key.pem
    chmod 644 ssl/cert.pem
    chmod 600 ssl/key.pem
    
    echo "✅ Certificados copiados para ssl/"
    
    # Reiniciar nginx
    sudo systemctl start nginx
    
    # Reiniciar containers Docker se estiverem rodando
    if docker ps | grep -q nginx-proxy; then
        echo "🔄 Reiniciando container nginx-proxy..."
        docker restart nginx-proxy
    fi
    
    echo "✅ Renovação concluída com sucesso"
else
    echo "❌ Erro ao renovar certificados"
    sudo systemctl start nginx
    exit 1
fi
