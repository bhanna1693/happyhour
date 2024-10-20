package gpt

import (
	"context"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"log"
	"os"

	"github.com/bhanna1693/happyhour-go/internal/scraper"
	"github.com/sashabaranov/go-openai"
	"github.com/sashabaranov/go-openai/jsonschema"
)

type Special struct {
	Description    string `json:"description"`
	Cost           string `json:"cost"`
	When           string `json:"when,omitempty"`
	DateStart      string `json:"date_start,omitempty"`
	DateEnd        string `json:"date_end,omitempty"`
	TimeStart      string `json:"time_start,omitempty"`
	TimeEnd        string `json:"time_end,omitempty"`
	Weekday        string `json:"weekday,omitempty"`
	AdditionalInfo string `json:"additional_info,omitempty"`
}
type GPTResponse struct {
	Specials []Special `json:"specials"`
	Message  string    `json:"message"`
}

// Function to encode an image to base64
func encodeImageToBase64(imagePath string) (string, error) {
	file, err := os.ReadFile(imagePath)
	if err != nil {
		return "", fmt.Errorf("unable to read image file: %v", err)
	}
	return base64.StdEncoding.EncodeToString(file), nil
}

// Function to send the image to GPT-4 Turbo with Vision for structured output
func AnalyzeImageForSpecials(scrapeResp scraper.ScrapeResponse) (GPTResponse, error) {
	// Initialize OpenAI client
	client := openai.NewClient(os.Getenv("OPENAI_API_KEY"))
	ctx := context.Background()

	imageBase64, err := encodeImageToBase64(scrapeResp.ImgPath)
	if err != nil {
		return GPTResponse{}, err
	}
	messages := []openai.ChatCompletionMessage{
		{
			Role: openai.ChatMessageRoleUser,
			MultiContent: []openai.ChatMessagePart{
				{
					Type: openai.ChatMessagePartTypeText,
					Text: "You are an assistant that extracts restaurant specials from images and provides structured JSON.",
				},
				{
					Type: openai.ChatMessagePartTypeText,
					Text: `
					Please analyze the following image of a restaurant menu or promotional banner for any restaurant specials.
					Extract only details related to food or drink offers, discounts, or special combos. 
					Output the results in JSON format, with fields: description, cost, date, time, weekday, and any additional information. 
					The image is provided below as base64:
				`,
				},
				{
					Type: openai.ChatMessagePartTypeImageURL,
					ImageURL: &openai.ChatMessageImageURL{
						URL:    fmt.Sprintf("data:image/%s;base64,%s", scrapeResp.ImgType, imageBase64),
						Detail: openai.ImageURLDetailLow,
					},
				},
			},
			FunctionCall: &openai.FunctionCall{},
		},
	}

	var result GPTResponse
	schema, err := jsonschema.GenerateSchemaForType(result)
	if err != nil {
		return GPTResponse{}, fmt.Errorf("GenerateSchemaForType error: %v", err)
	}
	log.Printf("Schema Generated: %v", schema)

	responseFormat := &openai.ChatCompletionResponseFormat{
		Type: openai.ChatCompletionResponseFormatTypeJSONSchema,
		JSONSchema: &openai.ChatCompletionResponseFormatJSONSchema{
			Name:        "restaurant_specials",
			Schema:      schema,
			Description: "Get the details related to food or drink offers, discounts, or special combos.",
			Strict:      false,
		},
	}

	// schema2 := openai.FunctionDefinition{
	// 	Name: "get_restaurant_specials",
	// 	Parameters: jsonschema.Definition{
	// 		Type: jsonschema.Object,
	// 		Properties: map[string]jsonschema.Definition{
	// 			"specials": {
	// 				Type: jsonschema.Array,
	// 				Items: &jsonschema.Definition{
	// 					Type: jsonschema.Object,
	// 					Properties: map[string]jsonschema.Definition{
	// 						"description": {
	// 							Type:        jsonschema.String,
	// 							Description: "A brief description of the special or deal offered.",
	// 						},
	// 						"cost": {
	// 							Type:        jsonschema.String,
	// 							Description: "The cost or price of the special.",
	// 						},
	// 						"when": {
	// 							Type:        jsonschema.String,
	// 							Description: "General description of when the special is available (e.g., Happy Hour, Lunch Time).",
	// 						},
	// 						"date_start": {
	// 							Type:        jsonschema.String,
	// 							Description: "Start date for the special, in YYYY-MM-DD format.",
	// 						},
	// 						"date_end": {
	// 							Type:        jsonschema.String,
	// 							Description: "End date for the special, in YYYY-MM-DD format.",
	// 						},
	// 						"time_start": {
	// 							Type:        jsonschema.String,
	// 							Description: "Start time for the special, in HH:MM format.",
	// 						},
	// 						"time_end": {
	// 							Type:        jsonschema.String,
	// 							Description: "End time for the special, in HH:MM format.",
	// 						},
	// 						"weekday": {
	// 							Type:        jsonschema.String,
	// 							Enum:        []string{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"},
	// 							Description: "Day of the week when the special is available.",
	// 						},
	// 						"additional_info": {
	// 							Type:        jsonschema.String,
	// 							Description: "Additional information about the special, such as conditions or restrictions.",
	// 						},
	// 					},
	// 					Required: []string{"description", "cost", "when"},
	// 				},
	// 			},
	// 			"message": {
	// 				Type:        jsonschema.String,
	// 				Description: "Message indicating the status of the analysis.",
	// 			},
	// 			"specials_found": {
	// 				Type:        jsonschema.String,
	// 				Enum:        []string{"NO", "YES", "MAYBE"},
	// 				Description: "Indicates whether any specials were found.",
	// 			},
	// 		},
	// 		Required: []string{"specials", "message", "specials_found"},
	// 	},
	// }
	// tools := []openai.Tool{
	// 	{
	// 		Type:     openai.ToolTypeFunction,
	// 		Function: &schema2,
	// 	},
	// }

	resp, err := client.CreateChatCompletion(ctx, openai.ChatCompletionRequest{
		Model:    openai.GPT4oMini,
		Messages: messages,
		// Tools:          tools,
		ResponseFormat: responseFormat,
	})
	if err != nil {
		return GPTResponse{}, fmt.Errorf("error calling openai: %v", err)
	}
	log.Printf("GPT Response Content: %s", resp.Choices[0].Message.Content)

	// var result GPTResponse
	// err = schema.Unmarshal(resp.Choices[0].Message.Content, &result)
	err = json.Unmarshal([]byte(resp.Choices[0].Message.Content), &result)
	if err != nil {
		return GPTResponse{}, fmt.Errorf("unmarshal schema error: %v", err)
	}

	return result, nil
}
