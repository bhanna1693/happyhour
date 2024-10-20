package main

import (
	"fmt"
	"log"
	"net/http"

	"github.com/bhanna1693/happyhour-go/internal/handlers"
	"github.com/gorilla/mux"
	"github.com/joho/godotenv"
)

func main() {
	// Load environment variables from .env file
	err := godotenv.Load()
	if err != nil {
		log.Fatalf("Error loading .env file: %v", err)
	}

	// Create a new Gorilla Mux router
	r := mux.NewRouter()
	r.Use(mux.CORSMethodMiddleware(r))

	// Define route and handler
	r.HandleFunc("/scrape-website", handlers.ScrapeWebsiteHandler).Methods(http.MethodGet)

	// Start the server
	fmt.Println("Server is running on port 8080...")
	log.Fatal(http.ListenAndServe(":8080", r))
}
