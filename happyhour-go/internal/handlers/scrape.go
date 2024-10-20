package handlers

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"net/url"

	"github.com/bhanna1693/happyhour-go/internal/gpt"
	"github.com/bhanna1693/happyhour-go/internal/scraper"
)

// ScrapeWebsiteHandler handles website scraping and GPT processing
func ScrapeWebsiteHandler(w http.ResponseWriter, r *http.Request) {
	// Set CORS headers for the response
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.Header().Set("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
	w.Header().Set("Access-Control-Allow-Headers", "Content-Type")

	// Handle preflight requests
	if r.Method == http.MethodOptions {
		w.WriteHeader(http.StatusOK)
		return
	}

	// Get restaurantUrl from query params
	encodedRestaurantUrl := r.URL.Query().Get("restaurantUrl")

	// If restaurantUrl is missing, return error
	if encodedRestaurantUrl == "" {
		respondWithError(w, http.StatusBadRequest, "Missing restaurantUrl query parameter")
		return
	}
	restaurantUrl, err := url.QueryUnescape(encodedRestaurantUrl)
	if err != nil {
		respondWithError(w, http.StatusInternalServerError, "Could not decode url")
		return
	}

	// Scrape the website
	scrapeResp, err := scraper.ScrapeWebsite(restaurantUrl)
	if err != nil {
		respondWithError(w, http.StatusInternalServerError, fmt.Sprintf("Error scraping website: %v", err))
		return
	}

	log.Printf("Screenshot taken from %s, saved at path: %s", scrapeResp.RestaurantUrl, scrapeResp.ImgPath)

	// Analyze the screenshot with GPT
	gptResponse, err := gpt.AnalyzeImageForSpecials(scrapeResp)
	if err != nil {
		respondWithError(w, http.StatusInternalServerError, fmt.Sprintf("Error processing with GPT: %v", err))
		return
	}

	// Return successful response
	respondWithJSON(w, http.StatusOK, gptResponse)
}

// Helper function to respond with JSON and error status
func respondWithError(w http.ResponseWriter, code int, message string) {
	respondWithJSON(w, code, map[string]string{"error": message})
}

// Helper function to respond with JSON and status code
func respondWithJSON(w http.ResponseWriter, code int, payload interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(code)
	json.NewEncoder(w).Encode(payload)
}
