package main

import (
	"fmt"
	"log"
	"net/http"
	"os"

	"github.com/bhanna1693/happyhour-go/internal/handlers"
	"github.com/joho/godotenv"
	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
)

func main() {
	// Load environment variables from .env file
	err := godotenv.Load()
	if err != nil {
		log.Fatalf("Error loading .env file: %v", err)
	}
	frontendURL := os.Getenv("FRONTEND_URL")

	// Create a new Echo instance
	e := echo.New()

	// Middleware
	e.Use(middleware.Logger())
	e.Use(middleware.Recover())
	e.Use(middleware.CORSWithConfig(middleware.CORSConfig{
		AllowOrigins: []string{frontendURL},                                                  // Frontend URL (e.g., http://localhost:3000)
		AllowMethods: []string{http.MethodGet, http.MethodPost, http.MethodOptions},          // Allowed HTTP methods
		AllowHeaders: []string{echo.HeaderOrigin, echo.HeaderContentType, echo.HeaderAccept}, // Allowed headers
	}))

	// Define route and handler
	e.GET("/scrape-website", handlers.ScrapeWebsiteHandler)

	// Start the server
	fmt.Println("Server is running on port 8080...")
	log.Fatal(e.Start(":8080"))
}
