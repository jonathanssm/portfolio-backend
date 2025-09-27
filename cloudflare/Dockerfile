FROM cloudflare/cloudflared:2024.9.0

# Copy configuration
COPY config.yml /etc/cloudflared/config.yml

# Expose cloudflared port
EXPOSE 8080

# Run cloudflared tunnel with token authentication
CMD ["tunnel", "--config", "/etc/cloudflared/config.yml", "run", "--token", "$TUNNEL_TOKEN"]
