FROM nginx:1.25-alpine

# Remove default nginx config
RUN rm /etc/nginx/conf.d/default.conf

# Copy custom nginx configuration
COPY nginx.conf /etc/nginx/nginx.conf

# Create SSL directory
RUN mkdir -p /etc/nginx/ssl

# Create logs directory
RUN mkdir -p /var/log/nginx

# Set proper permissions
RUN chown -R nginx:nginx /var/log/nginx /etc/nginx/ssl

# Expose ports
EXPOSE 80 443

# Health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s \
  CMD wget --no-verbose --tries=1 --spider http://localhost/actuator/health || exit 1

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
