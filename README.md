# Siemens Train Ticketing System

This project is a RESTful API built with Java and Spring Boot, designed to handle train schedules, routing, and ticket bookings. It serves as the solution for the Siemens Java Developer Trainee assignment.

## 🚀 Technical Highlights & Architecture

* **Framework:** Spring Boot 3 with Java 17.
* **Architecture:** Layered Architecture (Controllers -> Services -> Repositories -> Models) ensuring a clear separation of concerns.
* **Pathfinding Algorithm:** Implemented **Dijkstra's Algorithm** using a `PriorityQueue` to find the absolute *fastest* route between two stations, calculating both travel time and layover/wait times.
* **Persistence:** Utilizes **Spring Data JPA** with an **H2 In-Memory Database**. This ensures the application is highly portable and requires *zero configuration* from the examiner to run.
* **Data Seeding:** Implemented a `CommandLineRunner` that automatically populates the database with initial dummy data (stations, trains, segments) on startup for immediate testing.
* **Data Transfer Objects (DTOs):** Utilized Java `record` types for clean, immutable data transfer between the client and the API.
* **Transaction Management:** Critical operations (like booking tickets) are wrapped in `@Transactional` to prevent data corruption in case of unexpected errors.

---

## 🛠️ How to Run the Application

Since the project uses Gradle and an in-memory database, running it is extremely straightforward. No external database installation is required.

1. Clone the repository.
2. Open a terminal in the root directory of the project.
3. Run the application using the Gradle Wrapper:
   * **Windows:** `gradlew bootRun`
   * **Mac/Linux:** `./gradlew bootRun`
*(Alternatively, simply open the project in IntelliJ IDEA and run the main `TrainTicketingApplication` class).*

Once started, the console will display: `database successfully populated` indicating the seeder has successfully injected the test network.

---
## 🧪 Examiner's Testing Guide / Tutorial

To make testing as easy as possible, **Swagger UI (OpenAPI)** has been integrated. 
Once the application is running, open your browser and navigate to:
👉 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

Below are step-by-step examples of how to test all required functionalities using the Swagger interface.

### Scenario 1: Find the Fastest Route (Direct or Changeover)
* **Endpoint:** `GET /api/routes/fastest`
* **Goal:** Find a route from Cluj-Napoca (CJ) to Constanța (CT). The algorithm will route you through Brașov (BV) with a changeover.
* **Input Parameters:**
  * `startStationId`: `CJ`
  * `endStationId`: `CT`
* **Expected Output (HTTP 200 OK):** A JSON array detailing the travel segments, showing the arrival in Brașov and the connecting train departure.

### Scenario 2: Book Tickets & Prevent Overbooking
* **Endpoint:** `POST /api/bookings`
* **Goal:** Book tickets and trigger the confirmation email.
* **Input Body (JSON):**

{
  "email": "examiner@siemens.com",
  "trainId": "IR-100",
  "departureStationId": "CJ",
  "arrivalStationId": "BV",
  "numberOfTickets": 2
}

* **Expected Output:**
  1. The API returns the saved Booking object with a generated UUID.
  2. **Look at the IDE console:** You will see a clearly formatted mock email sent to `examiner@siemens.com`.
* *Note on Overbooking:* The `IR-100` train has a seeded capacity of 50. If you try to book `51` tickets, the API will return an `HTTP 400 Bad Request` with an overbooking error message.

### Scenario 3: Admin - Report a Train Delay & Notify Customers
* **Endpoint:** `POST /api/admin/trains/{trainId}/delay`
* **Goal:** Mark a train as delayed and automatically email all passengers who hold a ticket for that specific train. *(Make sure you completed Scenario 2 first!)*
* **Input Parameters:**
  * `trainId`: `IR-100`
  * `delayMinutes`: `45`
* **Expected Output:**
  1. The API returns a success message.
  2. **Look at the IDE console:** You will see a mock email sent to the passenger(s) apologizing for the 45-minute delay on train IR-100.

### Scenario 4: Admin - View Bookings for a Train
* **Endpoint:** `GET /api/admin/trains/{trainId}/bookings`
* **Goal:** See all reservations made for a specific train.
* **Input Parameter:**
  * `trainId`: `IR-100`
* **Expected Output:** A JSON array of all bookings, including the one created in Scenario 2.
### Scenario 5: Admin - Add a New Train
* **Endpoint:** `POST /api/admin/trains`
* **Goal:** Insert a new train into the fleet.
* **Input Body (JSON):**

{
  "id": "IC-500",
  "name": "InterCity 500",
  "capacity": 150
}

* **Expected Output:** The saved train object. Trying to add it again will result in an `HTTP 400` error, preventing duplicate IDs.
### Scenario 6: Admin - Delete a Train
* **Endpoint:** `DELETE /api/admin/trains/{trainId}`
* **Goal:** Remove a train from the system.
* **Input Parameter:**
  * `trainId`: `IC-500` (or any other ID previously created)
* **Expected Output:** A success message confirming the train has been deleted. If you try to delete a non-existent train, the API will return `400 Bad Request`.

### Scenario 7: Admin - Modify a Train
* **Endpoint:** `PUT /api/admin/trains/{trainId}`
* **Goal:** Update the details of an existing train (e.g., change its name or capacity).
* **Input Parameter:**
  * `trainId`: `IR-100`
* **Input Body (JSON):**

{
  "name": "InterRegio 100 Updated",
  "capacity": 200
}

* **Expected Output:** The updated train object reflecting the new name and capacity.

### Scenario 8: Admin - Add a New Station
* **Endpoint:** `POST /api/admin/stations`
* **Goal:** Add a new station to the railway network.
* **Input Body (JSON):**

{
  "id": "TM",
  "name": "Timisoara"
}

* **Expected Output:** The saved station object.
### Scenario 9: Admin - Add a New Route Segment
* **Endpoint:** `POST /api/admin/routes`
* **Goal:** Create a new link (route) between stations for a specific train.
* **Input Body (JSON):**

{
  "train": { "id": "IR-100" },
  "departureStation": { "id": "CJ" },
  "arrivalStation": { "id": "TM" },
  "departureTime": "10:00:00",
  "arrivalTime": "14:00:00"
}

* **Expected Output:** The saved route segment linking Cluj-Napoca (CJ) to Timisoara (TM).
---
## 🌟 Problem 2 (Optional) - Solved: Concurrent Booking Stress-Test

**The Problem:** In a real-world scenario (e.g., tickets for a major festival), 100 users might click the "Book" button at the exact same millisecond. Without proper database locking, all 100 threads would read the same available capacity and successfully book, leading to massive overbooking despite validation logic.

**The Solution:** Implemented **Pessimistic Write Locking** (`@Lock(LockModeType.PESSIMISTIC_WRITE)`) in the Spring Data JPA Repository. This forces the database to lock the specific `Train` row during the transaction, guaranteeing that subsequent concurrent threads must wait in line and re-evaluate the true capacity.

**Programmatic Implementation & Testing:**
A dedicated API endpoint was created to programmatically demonstrate this edge case using Java Concurrency utilities (`ExecutorService`, `CountDownLatch`).

* **Endpoint:** `POST /api/bonus/stress-test`
* **How it works:** 1. It creates a temporary train with a capacity of **5 seats**.
  2. It spawns **100 concurrent threads** acting as users.
  3. It forces all 100 threads to execute the booking service simultaneously.
* **Expected Result:** The response will explicitly show that exactly 5 bookings succeeded and 95 were successfully rejected, proving the system is 100% thread-safe against race conditions.

---

## ✍️ Author
Developed with passion by **Cosmagiu Mihai-Constantin** as part of the Siemens Java Developer Trainee application process.

---
