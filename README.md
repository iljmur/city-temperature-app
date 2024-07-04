# City Temperature Application

This is a Spring Boot application for querying and managing the temperatures of various cities. The application retrieves city names from an external API and their corresponding temperatures from the OpenWeatherMap API.

## Prerequisites

- Java 17
- Maven
- An API key from OpenWeatherMap

## Getting Started

1. **Clone the repository:**
   ```bash
   git clone git@github.com:iljmur/city-temperature-app.git
   cd city-temperature-app
   ```

2. **Configure the API Key:**
   Replace the `API_KEY` in `CityService` with your OpenWeatherMap API key.

3. **Build the project:**
   ```bash
   mvn clean install
   ```

4. **Run the application:**
   ```bash
   java -jar target/citytemperature-0.0.1-SNAPSHOT.jar
   ```

## API Documentation

### Get All Cities

**URL:** `/api/cities`

**Method:** `GET`

**Query Parameters:**
- `page` (optional): Page number (default is 0)
- `size` (optional): Number of records per page (default is 20)
- `sort` (optional): Sorting criteria in the format `property,direction` (e.g., `name,asc`)

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "London",
      "temperature": 15.0
    },
    {
      "id": 2,
      "name": "Paris",
      "temperature": 18.0
    }
  ],
  "pageable": {
    "sort": {
      "sorted": false,
      "unsorted": true,
      "empty": true
    },
    "pageNumber": 0,
    "pageSize": 20,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 2,
  "last": false,
  "size": 20,
  "number": 0,
  "sort": {
    "sorted": false,
    "unsorted": true,
    "empty": true
  },
  "numberOfElements": 2,
  "first": true,
  "empty": false
}
```

### Delete All Cities

**URL:** `/api/cities`

**Method:** `DELETE`

**Response:** `204 No Content`

### Reload Cities

**URL:** `/api/cities/reload`

**Method:** `POST`

**Response:** `200 OK`


## Example Usage

**Get All Cities (with paging and sorting):**
```sh
curl -X GET "http://localhost:8080/api/cities?page=0&size=10&sort=name,asc"
```

**Delete All Cities:**
```sh
curl -X DELETE "http://localhost:8080/api/cities"
```

**Reload Cities:**
```sh
curl -X POST "http://localhost:8080/api/cities/reload"
```
