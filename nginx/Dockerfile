FROM nginx:1.25-alpine

# Remove default nginx config and create directories with permissions
RUN rm /etc/nginx/conf.d/default.conf && \
    mkdir -p /etc/nginx/ssl /var/log/nginx && \
    chown -R nginx:nginx /var/log/nginx /etc/nginx/ssl

# Expose ports
EXPOSE 80 443

# Health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s \
  CMD wget --no-verbose --tries=1 --spider https://localhost/nginx_status --no-check-certificate || exit 1

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
