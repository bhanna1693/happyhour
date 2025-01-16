package handlers

import (
	"fmt"
	"log"
	"net/http"
	"net/url"

	"github.com/bhanna1693/happyhour-go/internal/gpt"
	"github.com/bhanna1693/happyhour-go/internal/scraper"
	"github.com/labstack/echo/v4"
)

// ScrapeWebsiteHandler handles website scraping and GPT processing
func ScrapeWebsiteHandler(c echo.Context) error {
	// Get restaurantUrl from query params
	encodedRestaurantUrl := c.QueryParam("restaurantUrl")

	// If restaurantUrl is missing, return error
	if encodedRestaurantUrl == "" {
		return c.JSON(http.StatusBadRequest, map[string]string{"error": "Missing restaurantUrl query parameter"})
	}

	restaurantUrl, err := url.QueryUnescape(encodedRestaurantUrl)
	if err != nil {
		return c.JSON(http.StatusInternalServerError, map[string]string{"error": "Could not decode url"})
	}

	// Scrape the website
	scrapeResp, err := scraper.ScrapeWebsite(restaurantUrl)
	if err != nil {
		return c.JSON(http.StatusInternalServerError, map[string]string{"error": fmt.Sprintf("Error scraping website: %v", err)})
	}

	log.Printf("Screenshot taken from %s, saved at path: %s", scrapeResp.RestaurantUrl, scrapeResp.ImgPath)

	// Analyze the screenshot with GPT
	gptResponse, err := gpt.AnalyzeImageForSpecials(scrapeResp)
	if err != nil {
		return c.JSON(http.StatusInternalServerError, map[string]string{"error": fmt.Sprintf("Error processing with GPT: %v", err)})
	}

	// Return successful response
	return c.JSON(http.StatusOK, gptResponse)
}
