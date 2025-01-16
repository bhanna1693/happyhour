import HappyHourForm from "@/app/happy-hour-form";
import { ThemeToggle } from "@/components/theme-toggle";
import { HappyHourProvider } from "@/context/HappyHourContext";

export default function Home() {
  return (
    <HappyHourProvider>
      <div>
        <div className="flex justify-between items-center">
          <span>Happy Hour</span>
          <ThemeToggle />
        </div>
        <HappyHourForm></HappyHourForm>
      </div>
    </HappyHourProvider>
  );
}
