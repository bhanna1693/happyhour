package handlers

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"

	"github.com/bhanna1693/happyhour-go/internal/gpt"
	"github.com/bhanna1693/happyhour-go/internal/scraper"
)

func ScrapeWebsiteHandler(w http.ResponseWriter, r *http.Request) {
	// Parse the query param "restaurantUrl"
	restaurantUrl := r.URL.Query().Get("restaurantUrl")

	// If restaurantUrl is missing, respond with an error
	if restaurantUrl == "" {
		http.Error(w, "Missing restaurantUrl query parameter", http.StatusBadRequest)
		return
	}

	// Call the scraper logic
	scrapeResp, err := scraper.ScrapeWebsite(restaurantUrl)
	if err != nil {
		http.Error(w, fmt.Sprintf("Error scraping website: %v", err), http.StatusInternalServerError)
		return
	}

	// Generate a description or additional metadata for GPT processing
	log.Printf("Screenshot taken from %s, saved at path: %s", scrapeResp.RestaurantUrl, scrapeResp.ImgPath)

	// Send description to GPT
	gptResponse, err := gpt.AnalyzeImageForSpecials(scrapeResp)
	if err != nil {
		http.Error(w, fmt.Sprintf("Error processing with GPT: %v", err), http.StatusInternalServerError)
		return
	}

	// Success response
	log.Printf("GPT response: %v", gptResponse)
	// Return JSON response with detected specials or no specials found
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(gptResponse)
}
