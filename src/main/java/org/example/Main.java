package org.example;

import org.example.dao.BlogDao;
import org.example.dao.CategoryDao;
import org.example.dao.UserDao;
import org.example.domain.Blog;
import org.example.domain.Category;
import org.example.domain.User;

import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static User loggedInUser = null;

    public static void main(String[] args) {
        System.out.println("************ !! Welcome to Blog Management System !! ************");
        while (true) {
            if (loggedInUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }
    private static void showLoginMenu() {
        System.out.println("\n *****  Login Menu  ***** ");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Show all Users");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 3:
                showAllUsers();
                break;
            case 0:
                System.out.println("Thank you for using us, Bye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void showMainMenu() {
        System.out.println("\n*********** Main Menu ***********");
        System.out.println("Welcome, " + loggedInUser.getName() + "!");
        System.out.println("1. Show Category");
        System.out.println("2. Add category");
        System.out.println("3. Create Blog");
        System.out.println("4. Display All Blogs");
        System.out.println("5. Show My Blogs");
        System.out.println("6. Delete Blogs");
        System.out.println("7. Read Blog Contents");
        System.out.println("8. Edit Blog");
        System.out.println("9. Search Blog");
        System.out.println("0. Logout");
        System.out.print("Enter Your Choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        System.out.println("\n*********** Main Menu ***********");
        
        switch (choice) {
            case 1:
                viewAllCategories();
                break;
            case 2:
                addCategory();
                break;
            case 3:
                createBlog();
                break;
            case 4:
                viewAllBlogs();
                break;
            case 5:
                viewMyBlogs();
                break;
            case 6:
                deleteBlog();
                break;
            case 7:
                readBlogContent();
                break;
            case 8:
                editBlog();
                break;
            case 9:
                searchBlogs();
                break;
            case 0:
                System.out.println("Hackathon Completed Thanks for Evaluating...");
                logout();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    private static void login() {
        System.out.println("\n*********** Login ***********");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        try (UserDao userDao = new UserDao()) {
            User user = userDao.validateUser(email, password);
            if (user != null) {
                loggedInUser = user;
                System.out.println("[Login successful! Welcome, " + loggedInUser.getName() + "!]");
            } else {
                System.out.println("Something is wrong! Please check your email and password.");
            }
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
        }
    }
    private static void register() {
        System.out.println("\n*********** Register ***********");
        System.out.print("Enter name:");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Mobile: ");
        String mobile = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        User user = new User(name, email, password, mobile, address);
        try (UserDao userDao = new UserDao()) {
            int result = userDao.registerUser(user);
            if (result > 0) {
                System.out.println("Registration successful! You can now log in.");
            } else {
                System.out.println("Registration failed!");
            }
        } catch (Exception e) {
            System.out.println("Error during registration: " + e.getMessage());
        }
    }
    private static void logout() {
        loggedInUser = null;
        System.out.println("Logged out successfully!");
    }
    private static void viewAllBlogs() {
        System.out.println("\n*********** All Blogs ***********");
        System.out.println("---------------------------------------------------------------------------------");
        System.out.printf("%-6s %-15s %-15s %-15s %-25s\n", "B ID", "TITLE", "CATEGORY", "USER-ID", "CREATION-TIME");
        System.out.println("---------------------------------------------------------------------------------");
        
        try (BlogDao blogDao = new BlogDao()) {
            List<Blog> blogs = blogDao.getAllBlogs();
            if (blogs.isEmpty()) {
                System.out.println("No blogs found.");
            } else {
                for (Blog blog : blogs) {
                    System.out.printf("%-6d %-10s %-10s %-10s %-20s\n", 
                        blog.getId(), 
                        blog.getTitle(), 
                        blog.getCategoryTitle(),
                        blog.getUserId() + "-" + blog.getAuthorName(), 
                        blog.getCreationTime());
                }
            }
            System.out.println("------------------------------------------------------------------");
        } catch (Exception e) {
            System.out.println("Error retrieving blogs: " + e.getMessage());
        }
    }
    private static void viewMyBlogs() {
        System.out.println("\n*********** My Blogs ***********");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("%-6s %-10s %-10s %-10s %-20s\n", "B ID", "TITLE", "CATEGORY", "USER-ID", "CREATION-TIME");
        System.out.println("--------------------------------------------------------------------------------");
        try (BlogDao blogDao = new BlogDao()) {
            List<Blog> blogs = blogDao.getBlogsByUser(loggedInUser.getUserId());
            if (blogs.isEmpty()) {
                System.out.println("You haven't created any blogs yet.");
            } else {
                for (Blog blog : blogs) {
                    System.out.printf("%-6d %-10s %-10s %-10s %-20s\n", 
                        blog.getId(), 
                        blog.getTitle(), 
                        blog.getCategoryTitle(),
                        loggedInUser.getUserId() + "-" + loggedInUser.getName(), 
                        blog.getCreationTime());
                }
            }
            System.out.println("------------------------------------------------------------------");
        } catch (Exception e) {
            System.out.println("Error retrieving your blogs: " + e.getMessage());
        }
    }
    private static void createBlog() {
        System.out.println("\n===== Create Blog =====");
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.println("Select Category:");
        System.out.println("-------------------");
        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-6s %-15s %-50s %-20s\n", "C-ID", "C-TITLE", "C-DESC", "CREATION-TIME");
        System.out.println("------------------------------------------------------------------");
        try (CategoryDao categoryDao = new CategoryDao()) {
            List<Category> categories = categoryDao.getAllCategories();
            if (categories.isEmpty()) {
                System.out.println("No categories available. Please create a category first.");
                return;
            }
            for (Category category : categories) {
                System.out.printf("%-6d %-10s %-30s %-20s\n", 
                    category.getId(), 
                    category.getTitle(), 
                    category.getDescription(), 
                    category.getCreatedTime());
            }
            System.out.println("------------------------------------------------------------------");
            System.out.print("Type Category id: ");
            int categoryId = scanner.nextInt();
            scanner.nextLine();
            Category selectedCategory = categoryDao.getCategoryById(categoryId);
            if (selectedCategory == null) {
                System.out.println("Invalid category ID.");
                return;
            }
            
            System.out.println("Enter the content of your blog (type 'end' this to finish):");
            StringBuilder contentBuilder = new StringBuilder();
            String line;
            while (!(line = scanner.nextLine()).equals("end")) {
                contentBuilder.append(line).append("\n");
            }
            String content = contentBuilder.toString().trim();
            Blog blog = new Blog(title, categoryId, content, loggedInUser.getUserId());
            try (BlogDao blogDao = new BlogDao()) {
                int result = blogDao.createBlog(blog);
                if (result > 0) {
                    System.out.println("Blog created successfully!");
                } else {
                    System.out.println("Failed to create blog. Please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error creating blog: " + e.getMessage());
        }
    }
    private static void editBlog() {
        System.out.println("\n===== Edit Blog =====");
        try (BlogDao blogDao = new BlogDao()) {
            System.out.print("Enter the id of the blog to be edited: ");
            int blogId = scanner.nextInt();
            scanner.nextLine();
            if (!blogDao.isAuthorizedUser(blogId, loggedInUser.getUserId())) {
                System.out.println("You are not authorized to edit this blog");
                return;
            }
            System.out.println("Found blog for editing...");
            Blog blogToEdit = blogDao.getBlogById(blogId);
            System.out.println("Current title: " + blogToEdit.getTitle());
            System.out.print("Enter new title (or press Enter to keep current): ");
            String title = scanner.nextLine();
            if (!title.isEmpty()) {
                blogToEdit.setTitle(title);
            }
            System.out.println("Current category: " + blogToEdit.getCategoryTitle());
            try (CategoryDao categoryDao = new CategoryDao()) {
                List<Category> categories = categoryDao.getAllCategories();
                System.out.println("Available categories:");
                System.out.println("------------------------------------------------------------------");
                System.out.printf("%-6s %-10s %-30s\n", "C-ID", "C-TITLE", "C-DESC");
                System.out.println("------------------------------------------------------------------");
                for (Category category : categories) {
                    System.out.printf("%-6d %-10s %-30s\n", 
                        category.getId(), 
                        category.getTitle(),
                        category.getDescription());
                }
                System.out.println("------------------------------------------------------------------");
                System.out.print("Enter new category ID (or 0 to keep current): ");
                int categoryId = scanner.nextInt();
                scanner.nextLine();
                if (categoryId > 0) {
                    Category selectedCategory = categoryDao.getCategoryById(categoryId);
                    if (selectedCategory != null) {
                        blogToEdit.setCategoryId(categoryId);
                    } else {
                        System.out.println("Invalid category ID, keeping current category.");
                    }
                }
            }
            System.out.println("Current content: ");
            System.out.println("--------------------");
            System.out.println(blogToEdit.getContent());
            System.out.println("--------------------");
            System.out.println("Enter new content (type 'end' on a new line to finish, or just 'end' to keep current):");
            StringBuilder contentBuilder = new StringBuilder();
            String line;
            if (!(line = scanner.nextLine()).equals("end")) {
                contentBuilder.append(line).append("\n");
                while (!(line = scanner.nextLine()).equals("end")) {
                    contentBuilder.append(line).append("\n");
                }
                blogToEdit.setContent(contentBuilder.toString().trim());
            }
            boolean result = blogDao.editBlog(blogToEdit);
            if (result) {
                System.out.println("Blog updated successfully!");
            } else {
                System.out.println("Failed to update blog. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Error editing blog: " + e.getMessage());
        }
    }
    private static void deleteBlog() {
        System.out.println("\n===== Delete Blog =====");
        try (BlogDao blogDao = new BlogDao()) {
            List<Blog> blogs = blogDao.getBlogsByUser(loggedInUser.getUserId());
            if (blogs.isEmpty()) {
                System.out.println("You haven't created any blogs to delete.");
                return;
            }
            System.out.println("Your blogs:");
            displayBlogs(blogs);
            System.out.print("Enter the ID of the blog you want to delete: ");
            int blogId = scanner.nextInt();
            scanner.nextLine();
//            System.out.print("Are you sure you want to delete this blog? (y/n): ");
//            String confirm = scanner.nextLine();
//            if (confirm.equalsIgnoreCase("y")) {
            boolean result = blogDao.deleteBlog(blogId, loggedInUser.getUserId());
                if (result) {
                    System.out.println("Blog deleted successfully!");
                } else {
                    System.out.println("Failed to delete blog. Please ensure you have the correct blog ID.");
                }
//           } else {
//               System.out.println("Blog deletion cancelled.");
//           }
        } catch (Exception e) {
            System.out.println("Error deleting blog: " + e.getMessage());
        }
    }
    private static void searchBlogs() {
        System.out.println("\n*********** Search Blog ***********");
        System.out.print("Enter the word from title : ");
        String searchWord = scanner.nextLine();
        try (BlogDao blogDao = new BlogDao()) {
            List<Blog> blogs = blogDao.searchBlogsByWord(searchWord);
            if (blogs.isEmpty()) {
                System.out.println("No blogs found matching your search word.");
            } else {
                for (Blog blog : blogs) {
                    System.out.println("Blog id: " + blog.getId() + " - " + blog.getTitle());
                }
            }
        } catch (Exception e) {
            System.out.println("Error searching blogs: " + e.getMessage());
        }
    }
    private static void categoryManagement() {
        System.out.println("\n*********** Category Management ***********");
        System.out.println("1. View all categories");
        System.out.println("2. Add new category");
        System.out.println("0. Back to main menu");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                viewAllCategories();
                break;
            case 2:
                addCategory();
                break;
            case 0:
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    private static void viewAllCategories() {
        System.out.println("\n===== Show Category =====");
        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-6s %-10s %-30s %-20s\n", "C-ID", "C-TITLE", "C-DESC", "CREATION-TIME");
        System.out.println("------------------------------------------------------------------");
        try (CategoryDao categoryDao = new CategoryDao()) {
            List<Category> categories = categoryDao.getAllCategories();
            if (categories.isEmpty()) {
                System.out.println("No categories found.");
            } else {
                for (Category category : categories) {
                    System.out.printf("%-6d %-10s %-30s %-20s\n", 
                        category.getId(), 
                        category.getTitle(), 
                        category.getDescription(), 
                        category.getCreatedTime());
                }
            }
            System.out.println("------------------------------------------------------------------");
        } catch (Exception e) {
            System.out.println("Error retrieving categories: " + e.getMessage());
        }
    }
    private static void addCategory() {
        System.out.println("\n===== Add Category =====");
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Desc: ");
        String description = scanner.nextLine();
        Category category = new Category(title, description);
        try (CategoryDao categoryDao = new CategoryDao()) {
            int result = categoryDao.addCategory(category);
            if (result > 0) {
                System.out.println("Category added successfully!");
            } else {
                System.out.println("Failed to add category. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Error adding category: " + e.getMessage());
        }
    }
    private static void displayBlogs(List<Blog> blogs) {
        for (Blog blog : blogs) {
            System.out.println("ID: " + blog.getId());
            System.out.println("Title: " + blog.getTitle());
            System.out.println("Author: " + blog.getAuthorName());
            System.out.println("Category: " + blog.getCategoryTitle());
            System.out.println("Created: " + blog.getCreationTime());
            if (blog.getContent() != null) {
                String previewContent = blog.getContent().length() > 50 ? 
                    blog.getContent().substring(0, 50) + "..." : 
                    blog.getContent();
                System.out.println("Preview: " + previewContent);
            }
            System.out.println("------------------------------");
        }
        System.out.print("Enter blog ID to view details (or 0 to go back): ");
        int blogId = scanner.nextInt();
        scanner.nextLine();
        if (blogId > 0) {
            try (BlogDao blogDao = new BlogDao()) {
                Blog blog = blogDao.getBlogById(blogId);
                if (blog != null) {
                    System.out.println("\n===== Blog Details =====");
                    System.out.println("ID: " + blog.getId());
                    System.out.println("Title: " + blog.getTitle());
                    System.out.println("Author: " + blog.getAuthorName());
                    System.out.println("Category: " + blog.getCategoryTitle());
                    System.out.println("Created: " + blog.getCreationTime());
                    System.out.println("\nContent:");
                    System.out.println(blog.getContent());
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                } else {
                    System.out.println("Blog not found.");
                }
            } catch (Exception e) {
                System.out.println("Error retrieving blog details: " + e.getMessage());
            }
        }
    }
    private static void readBlogContent() {
        System.out.println("\n*********** Read Blog Contents ***********");
        System.out.print("Enter blog id to read: ");
        int blogId = scanner.nextInt();
        scanner.nextLine();
        try (BlogDao blogDao = new BlogDao()) {
            Blog blog = blogDao.getBlogById(blogId);
            if (blog != null) {
                System.out.println("Blog details:");
                System.out.println("----------------------------");
                System.out.println("Blog ID: " + blog.getId());
                System.out.println("Title: " + blog.getTitle());
//                System.out.println("Category: " + blog.getCategoryTitle());
//                System.out.println("Author: " + blog.getAuthorName());
//                System.out.println("Created: " + blog.getCreationTime());
//                System.out.println("----------------------------");
                System.out.println("Content:");
                System.out.println(blog.getContent());
                System.out.println("----------------------------");
            } else {
                System.out.println("Blog not found.");
            }
        } catch (Exception e) {
            System.out.println("Error reading blog: " + e.getMessage());
        }
    }
    private static void showAllUsers() {
        System.out.println("\n*********** All Users ***********");
        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-6s %-10s %-20s %-10s %-10s %-20s\n", "U-ID", "NAME", "EMAIL", "MOBILE", "ADDRESS", "TIME");
        System.out.println("------------------------------------------------------------------");
        try (UserDao userDao = new UserDao()) {
            List<User> users = userDao.getAllUsers();
            if (users.isEmpty()) {
                System.out.println("No users found.");
            } else {
                for (User user : users) {
                    System.out.printf("%-6d %-10s %-20s %-10s %-10s %-20s\n", 
                        user.getUserId(), 
                        user.getName(), 
                        user.getEmail(), 
                        user.getMobile(), 
                        user.getAddress(), 
                        user.getTime());
                }
            }
            System.out.println("------------------------------------------------------------------");
        } catch (Exception e) {
            System.out.println("Error retrieving users: " + e.getMessage());
        }
    }
}