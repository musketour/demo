package com.librarymanagement.demo.controller;

import com.librarymanagement.demo.main.Book;
import com.librarymanagement.demo.main.Item;
import com.librarymanagement.demo.main.Journal;
import com.librarymanagement.demo.main.Movie;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LibraryController {

    private final List<Item> itemList = new ArrayList<>();

    public LibraryController() {
        // Initialize with some data
        itemList.add(new Book(0, "Dune", 1965, true, "Frank Herbert", "978-0441172719", "Science Fiction", 412));
        itemList.add(new Book(1, "Harry Potter", 1997, true, "J.K. Rowling", "978-0747532743", "Fantasy", 223));
        itemList.add(new Journal(2, "Nature", 2020, true, "Editor Name", "1234-5678"));
        itemList.add(new Movie(3, "Harry Potter (Movie)", 2001, true, "Chris Columbus", 152));
    }

    @GetMapping("/items")
    public List<Item> getItems() {
        return itemList;
    }

    @PostMapping("/items")
    public Response handleAction(@RequestBody ActionRequest request) {
        String action = request.getAction();
        String title = request.getTitle();
        String itemType = request.getItemType();

        for (Item item : itemList) {
            if (item.getTitle().equalsIgnoreCase(title) && item.getClass().getSimpleName().equalsIgnoreCase(itemType)) {
                if ("checkout".equals(action)) {
                    if (item.isAvailable()) {
                        item.checkOut();
                        return new Response("Checked out successfully");
                    } else {
                        return new Response("Item is not available for checkout");
                    }
                } else if ("checkin".equals(action)) {
                    item.checkIn();
                    return new Response("Checked in successfully");
                }
            }
        }
        return new Response("Item not found");
    }

    @GetMapping("/item-details")
    public Response getItemDetails(@RequestParam String title, @RequestParam String itemType) {
        for (Item item : itemList) {
            if (item.getTitle().equalsIgnoreCase(title) && item.getClass().getSimpleName().equalsIgnoreCase(itemType)) {
                return new Response(item);
            }
        }
        return new Response("Item not found");
    }

    static class ActionRequest {
        private String action;
        private String title;
        private String itemType;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getItemType() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }
    }

    public static class Response {
        private String message;
        private Object item;

        public Response(String message) {
            this.message = message;
        }

        public Response(Object item) {
            this.item = item;
            this.message = "Item found";
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getItem() {
            return item;
        }

        public void setItem(Object item) {
            this.item = item;
        }
    }
}
