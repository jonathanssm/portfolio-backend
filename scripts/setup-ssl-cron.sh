#!/bin/bash

# Script para configurar renovação automática de certificados SSL
# Uso: ./scripts/setup-ssl-cron.sh [domain]
# Exemplo: ./scripts/setup-ssl-cron.sh api.seudominio.com

set -e

DOMAIN=${1:-"localhost"}
SCRIPT_DIR="$(dirname "$0")"
RENEW_SCRIPT="$SCRIPT_DIR/ssl-renew.sh"

echo "⏰ Configurando renovação automática de certificados SSL para $DOMAIN"

# Verificar se é domínio real
if [ "$DOMAIN" = "localhost" ]; then
    echo "🏠 Domínio localhost - não é necessário configurar renovação automática"
    exit 0
fi

# Verificar se o script de renovação existe
if [ ! -f "$RENEW_SCRIPT" ]; then
    echo "❌ Script de renovação não encontrado: $RENEW_SCRIPT"
    exit 1
fi

# Tornar o script executável
chmod +x "$RENEW_SCRIPT"

# Configurar cron job para renovação diária às 12:00
CRON_JOB="0 12 * * * $RENEW_SCRIPT $DOMAIN >> /var/log/ssl-renew.log 2>&1"

# Verificar se o cron job já existe
if crontab -l 2>/dev/null | grep -q "$RENEW_SCRIPT"; then
    echo "✅ Cron job já configurado"
else
    # Adicionar cron job
    (crontab -l 2>/dev/null; echo "$CRON_JOB") | crontab -
    echo "✅ Cron job configurado com sucesso"
fi

# Verificar configuração
echo "📋 Cron jobs configurados:"
crontab -l | grep ssl-renew || echo "Nenhum cron job encontrado"

echo ""
echo "🎉 Configuração de renovação automática concluída!"
echo "📋 Detalhes:"
echo "   - Domínio: $DOMAIN"
echo "   - Renovação: Diária às 12:00"
echo "   - Log: /var/log/ssl-renew.log"
echo "   - Script: $RENEW_SCRIPT"
echo ""
echo "🔍 Para verificar logs:"
echo "   tail -f /var/log/ssl-renew.log"
