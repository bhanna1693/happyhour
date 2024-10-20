package main

import (
	"fmt"
	"log"
	"net/http"

	"github.com/bhanna1693/happyhour-go/internal/handlers"
	"github.com/joho/godotenv"
)

func main() {
	err := godotenv.Load()
	if err != nil {
		log.Fatalf("Error loading .env file: %v", err)
	}

	http.HandleFunc("/scrape-website", handlers.ScrapeWebsiteHandler)

	fmt.Println("Server is running on port 8080...")
	log.Fatal(http.ListenAndServe(":8080", nil))
}
