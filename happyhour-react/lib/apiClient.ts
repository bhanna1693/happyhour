const BASE_URL = "http://localhost:8080";
export const apiClient = {
  get: async (url: string, options: RequestInit = {}) => {
    const response = await fetch(BASE_URL + url, { ...options, method: "GET" });
    return handleResponse(response);
  },

  post: async <T>(url: string, data: T, options: RequestInit = {}) => {
    const response = await fetch(BASE_URL + url, {
      ...options,
      method: "POST",
      headers: { "Content-Type": "application/json", ...options.headers },
      body: JSON.stringify(data),
    });
    return handleResponse(response);
  },

  delete: async (url: string, options: RequestInit = {}) => {
    const response = await fetch(BASE_URL + url, {
      ...options,
      method: "DELETE",
    });
    return handleResponse(response);
  },
};

// Helper function to handle responses
const handleResponse = async (response: Response) => {
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "An error occurred");
  }
  return response.json();
};
