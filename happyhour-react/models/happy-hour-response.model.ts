export interface Special {
  description: string; // Brief description of the special or deal
  cost: string; // Cost or price of the special
  whenDescription: string; // General time of availability (e.g., Happy Hour, Lunch Time)
  date_start?: string; // Start date for the special (optional, format: YYYY-MM-DD)
  date_end?: string; // End date for the special (optional, format: YYYY-MM-DD)
  time_start?: string; // Start time for the special (optional, format: HH:MM)
  time_end?: string; // End time for the special (optional, format: HH:MM)
  weekday?: string; // Day of the week when the special is available (optional)
  additional_info?: string; // Any extra information about the special (optional)
}

export enum SpecialsFound {
  NO = "NO",
  YES = "YES",
  MAYBE = "MAYBE",
}

export interface HappyHourResponse {
  specials: Special[]; // List of specials
  message: string; // General message regarding the response
  specials_found: SpecialsFound; // Enum to indicate if specials were found
}
