package scraper

import (
	"fmt"
	"strings"

	"github.com/playwright-community/playwright-go"
)

type ScrapeResponse struct {
	RestaurantUrl string
	ImgPath       string
	ImgType       string
	ImgByteArr    []byte
}

func ScrapeWebsite(restaurantUrl string) (ScrapeResponse, error) {
	pw, err := playwright.Run()
	if err != nil {
		return ScrapeResponse{}, fmt.Errorf("could not launch Playwright: %v", err)
	}
	defer pw.Stop()

	browser, err := pw.Chromium.Launch()
	if err != nil {
		return ScrapeResponse{}, fmt.Errorf("could not launch Chromium: %v", err)
	}
	defer browser.Close()

	page, err := browser.NewPage()
	if err != nil {
		return ScrapeResponse{}, fmt.Errorf("could not create page: %v", err)
	}

	if _, err = page.Goto(restaurantUrl, playwright.PageGotoOptions{
		WaitUntil: playwright.WaitUntilStateDomcontentloaded,
	}); err != nil {
		return ScrapeResponse{}, fmt.Errorf("could not navigate to the URL: %v", err)
	}

	// Constructing the screenshot path
	imgPath := strings.Replace(restaurantUrl, "https://", "", 1)
	imgPath = strings.Replace(imgPath, "http://", "", 1)
	imgPath = strings.Replace(imgPath, "www.", "", 1)
	imgPath = strings.Replace(imgPath, ".com", "", 1)
	imgPath = strings.ReplaceAll(imgPath, "/", "__")
	imgType := "jpeg"

	fullImgPath := fmt.Sprintf("screenshots/%s.%s", imgPath, imgType)

	imgByteArr, err := page.Screenshot(playwright.PageScreenshotOptions{
		Path:     playwright.String(fullImgPath),
		FullPage: playwright.Bool(true),
		Type:     playwright.ScreenshotTypeJpeg,
		Quality:  playwright.Int(10),
	})

	if err != nil {
		return ScrapeResponse{}, fmt.Errorf("could not create screenshot: %v", err)
	}

	return ScrapeResponse{
		RestaurantUrl: restaurantUrl,
		ImgPath:       fullImgPath,
		ImgType:       imgType,
		ImgByteArr:    imgByteArr,
	}, nil
}
