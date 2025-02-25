# Use minimal Nginx image to serve Angular build
FROM nginx:alpine

# Copy Angular build output from Jenkins workspace into the container
COPY ./angular/dist/angular-app /usr/share/nginx/html

# Expose port 4200 and start Nginx
EXPOSE 4200
CMD ["nginx", "-g", "daemon off;"]
