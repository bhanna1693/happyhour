import HappyHourForm from "@/components/happy-hour-form";
import { ThemeToggle } from "@/components/theme-toggle";

export default function Home() {
  return (
    <div>
      <div className="flex justify-between items-center">
        <span>Happy Hour</span>
        <ThemeToggle />
      </div>
      <HappyHourForm></HappyHourForm>
    </div>
  );
}
