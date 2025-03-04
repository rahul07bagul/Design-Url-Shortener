# URL-Shortener

A robust, scalable URL shortening service built with Java and Spring Boot.

## Demo
![Demo](https://github.com/rahul07bagul/URL-shortener/blob/main/assets/url-shortener.gif)

## High Level Design
![HLD](https://github.com/rahul07bagul/URL-shortener/blob/main/assets/Url-shortener-design.png)

## Class Diagram
![LLD](https://github.com/rahul07bagul/URL-shortener/blob/main/assets/Url-shortener-class-Page.png)

## Features
- Generate short, unique URLs for any valid web address
- Configurable short code generation strategies (random Base62 or sequential counter)
- Multiple storage options (in-memory, Redis, MySQL)
- RESTful API for URL management
- Redirect service with analytics tracking
- Scalable architecture ready for high-traffic deployment

## Running the application
1. Clone the repository
   ```sh
   https://github.com/rahul07bagul/URL-shortener.git
   cd url-shortener
   ```
2. Configure database (if using MySQL):
   ```sh
   mysql -u root -p
   CREATE DATABASE urlshortener;
   ```
3. Build and run:
   ```sh
   mvn clean install
   ```

## API Usage
- Create a Short URL
  ```sh
  POST /api/v1/shorten
  Content-Type: application/json
  
  {
    "longUrl": "https://example.com/very/long/url/that/needs/shortening"
  }
  ```

- Get Original URL Info
  ```sh
  GET /api/v1/info/{shortCode}
  ```

- Get Original URL Info
  ```sh
  GET /{shortCode}
  ```

## Configuration Options
- url.shortener.generator.type: ID generation strategy (base62 or counter)
- url.shortener.code.length: Length of generated short codes (default: 6)
- url.shortener.base.url: Base URL for the shortener service
  




  

