"use client"

import { HappyHourResponse } from "@/models/happy-hour-response.model";
import {
  createContext,
  ReactElement,
  ReactNode,
  useContext,
  useState,
} from "react";

export interface HappyHourContextType {
  activeRestaurant: string;
  activeRestaurantDetails: HappyHourResponse | undefined;
  setActive: React.Dispatch<React.SetStateAction<string>>;
  setActiveDetails: React.Dispatch<
    React.SetStateAction<HappyHourResponse | undefined>
  >;
}
// Create a provider component and define the props
interface HappyHourProviderProps {
  children: ReactNode;
}
// Create the HappyHour context
const HappyHourContext = createContext<HappyHourContextType | undefined>(
  undefined
);

// Create a provider component
export const HappyHourProvider = ({
  children,
}: HappyHourProviderProps): ReactElement => {
  // Define state and updater function
  const [activeRestaurant, setActive] = useState<string>("");
  const [activeRestaurantDetails, setActiveDetails] =
    useState<HappyHourResponse>();

  return (
    <HappyHourContext.Provider
      value={{
        activeRestaurant,
        activeRestaurantDetails,
        setActive,
        setActiveDetails,
      }}
    >
      {children}
    </HappyHourContext.Provider>
  );
};

// Create a custom hook to access the context
export const useHappyHourContext = (): HappyHourContextType => {
  const context = useContext(HappyHourContext);
  if (!context) {
    throw new Error(
      "useHappyHourContext must be used within a HappyHourProvider"
    );
  }
  return context;
};
