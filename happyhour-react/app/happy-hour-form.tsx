"use client";
import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { useHappyHourContext } from "@/context/HappyHourContext";
import { apiClient } from "@/lib/apiClient";
import { catchErrorTyped } from "@/lib/catchErrorsTyped";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";

const formSchema = z.object({
  restaurantUrl: z.string().url("Must be a valid URL."),
});

export default function HappyHourForm() {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      restaurantUrl: "https://www.vocellipizza.com/shaler_pa",
    },
  });
  const happyHourContext = useHappyHourContext();

  async function onSubmit(values: z.infer<typeof formSchema>) {
    // Do something with the form values.
    // ✅ This will be type-safe and validated.
    const params = new URLSearchParams();
    params.set("restaurantUrl", encodeURIComponent(values.restaurantUrl));
    const url = "/scrape-website?" + params.toString();
    happyHourContext.setActive(values.restaurantUrl);
    const [resp, err] = await catchErrorTyped(apiClient.get(url));
    if (err) {
      console.error(err);
      return;
    }
    happyHourContext.setActiveDetails(resp);
  }

  return (
    <>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
          <FormField
            control={form.control}
            name="restaurantUrl"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Restaurant URL</FormLabel>
                <FormControl>
                  <Input placeholder="Enter url..." {...field} />
                </FormControl>
                <FormDescription>
                  Enter full url where deals are located.
                </FormDescription>
                <FormMessage />
              </FormItem>
            )}
          />

          <Button>Get Specials</Button>
        </form>
      </Form>
      {happyHourContext.activeRestaurantDetails && (
        <div>
          <ul>
            {happyHourContext.activeRestaurantDetails.specials.map((special, index) => (
              <li key={index}>
                <h3>{special.description}</h3>
                <p>Cost: {special.cost}</p>
                <p>When: {special.whenDescription}</p>
                {special.date_start && <p>Start Date: {special.date_start}</p>}
                {special.date_end && <p>End Date: {special.date_end}</p>}
                {special.time_start && <p>Time Start: {special.time_start}</p>}
                {special.time_end && <p>Time End: {special.time_end}</p>}
                {special.weekday && <p>Available on: {special.weekday}</p>}
                {special.additional_info && (
                  <p>Additional Info: {special.additional_info}</p>
                )}
              </li>
            ))}
          </ul>
        </div>
      )}
    </>
  );
}
