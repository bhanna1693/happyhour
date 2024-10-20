"use client";
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
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { Button } from "./ui/button";

const formSchema = z.object({
  restaurantUrl: z.string().url("Must be a valid URL."),
});

export default function HappyHourForm() {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      restaurantUrl: "https://vocellipizza.com/shaler_pa",
    },
  });
  function onSubmit(values: z.infer<typeof formSchema>) {
    // Do something with the form values.
    // ✅ This will be type-safe and validated.
    console.log(values);
  }
  return (
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
  );
}
