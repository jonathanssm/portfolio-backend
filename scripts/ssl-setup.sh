#!/bin/bash

# Script para configurar certificados SSL Let's Encrypt
# Uso: ./scripts/ssl-setup.sh [domain] [email]
# Exemplo: ./scripts/ssl-setup.sh api.seudominio.com admin@seudominio.com

set -e

DOMAIN=${1:-"localhost"}
EMAIL=${2:-"admin@localhost"}
SSL_DIR="/etc/letsencrypt/live/$DOMAIN"
NGINX_SSL_DIR="./ssl"

echo "🔐 Configurando certificados SSL Let's Encrypt para $DOMAIN"

# Verificar se o domínio é localhost (usar certificados auto-assinados)
if [ "$DOMAIN" = "localhost" ]; then
    echo "🏠 Domínio localhost detectado - usando certificados auto-assinados"
    mkdir -p ssl
    
    # Gera certificado auto-assinado válido por 10 anos
    openssl req -x509 -newkey rsa:4096 -keyout ssl/key.pem -out ssl/cert.pem -days 3650 -nodes \
      -subj "/C=BR/ST=SP/L=SaoPaulo/O=Portfolio/OU=IT/CN=localhost" \
      -addext "subjectAltName=DNS:localhost,DNS:*.localhost,IP:127.0.0.1,IP:0.0.0.0"
    
    chmod 600 ssl/key.pem
    chmod 644 ssl/cert.pem
    
    echo "✅ Certificados auto-assinados gerados com sucesso"
    exit 0
fi

# Verificar se certbot está instalado
if ! command -v certbot &> /dev/null; then
    echo "📦 Instalando certbot..."
    sudo apt-get update
    sudo apt-get install -y certbot
fi

# Verificar se o nginx está rodando
if ! pgrep nginx > /dev/null; then
    echo "⚠️  Nginx não está rodando. Iniciando nginx temporariamente..."
    sudo systemctl start nginx || true
fi

# Criar diretório para certificados
mkdir -p ssl

# Verificar se já existem certificados válidos
if [ -f "$SSL_DIR/fullchain.pem" ] && [ -f "$SSL_DIR/privkey.pem" ]; then
    echo "✅ Certificados Let's Encrypt já existem para $DOMAIN"
    
    # Verificar se os certificados não expiram em 30 dias
    if openssl x509 -in "$SSL_DIR/fullchain.pem" -noout -checkend 2592000 > /dev/null 2>&1; then
        echo "✅ Certificados são válidos por mais de 30 dias"
        
        # Copiar certificados para o diretório local
        sudo cp "$SSL_DIR/fullchain.pem" ssl/cert.pem
        sudo cp "$SSL_DIR/privkey.pem" ssl/key.pem
        sudo chown $USER:$USER ssl/cert.pem ssl/key.pem
        chmod 644 ssl/cert.pem
        chmod 600 ssl/key.pem
        
        echo "✅ Certificados copiados para ssl/"
        exit 0
    else
        echo "⚠️  Certificados expiram em menos de 30 dias - renovando..."
    fi
fi

# Gerar certificados Let's Encrypt
echo "🔐 Gerando certificados Let's Encrypt para $DOMAIN..."

# Parar nginx temporariamente
sudo systemctl stop nginx || true

# Gerar certificado usando webroot
sudo certbot certonly \
  --standalone \
  --non-interactive \
  --agree-tos \
  --email "$EMAIL" \
  --domains "$DOMAIN" \
  --expand

# Verificar se os certificados foram gerados
if [ -f "$SSL_DIR/fullchain.pem" ] && [ -f "$SSL_DIR/privkey.pem" ]; then
    echo "✅ Certificados Let's Encrypt gerados com sucesso"
    
    # Copiar certificados para o diretório local
    sudo cp "$SSL_DIR/fullchain.pem" ssl/cert.pem
    sudo cp "$SSL_DIR/privkey.pem" ssl/key.pem
    sudo chown $USER:$USER ssl/cert.pem ssl/key.pem
    chmod 644 ssl/cert.pem
    chmod 600 ssl/key.pem
    
    echo "✅ Certificados copiados para ssl/"
    
    # Configurar renovação automática
    echo "🔄 Configurando renovação automática..."
    (crontab -l 2>/dev/null; echo "0 12 * * * /usr/bin/certbot renew --quiet --post-hook 'systemctl reload nginx'") | crontab -
    
    echo "✅ Renovação automática configurada"
else
    echo "❌ Erro ao gerar certificados Let's Encrypt"
    exit 1
fi

# Reiniciar nginx
sudo systemctl start nginx

echo ""
echo "🎉 Configuração SSL concluída com sucesso!"
echo "📋 Certificados:"
echo "   - Certificado: ssl/cert.pem"
echo "   - Chave privada: ssl/key.pem"
echo "   - Domínio: $DOMAIN"
echo "   - Válido por: 90 dias (renovação automática configurada)"
echo ""
echo "🌐 Teste o certificado:"
echo "   curl -I https://$DOMAIN/actuator/health"
