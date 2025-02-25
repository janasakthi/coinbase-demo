# Use minimal Nginx image to serve Angular build
FROM nginx:alpine

# Copy Angular build output from Jenkins workspace into the container
COPY ./dist/angular-app/browser /usr/share/nginx/html

# Expose port 80 and start Nginx
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
