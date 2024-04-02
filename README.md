
# Milestone 1 - Taste Budzz App (Unit 7)

## Table of Contents

1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)

## Overview

### Description

A restaurant review app that facilitates users in finding nearby restaurants, writing reviews, accessing menus, and saving and editing recipes.

### App Evaluation

- **Category:** Food & Drink, Lifestyle.
- **Mobile:** Uses location info to give on-the-go access to restaurant information. Leverages mobile camera to idenfiy cousine and search for restaurants and for recepies. 
- **Story:** The app extends beyond a normal restaurant locator and reviews to allow users to bring the restaurant experience into their homes. They'll be able see recepies for menu items at restaurants and even edit them to make it thier own!
- **Market:** Appeals to food enthusiasts, travelers, and those seeking dining recommendations.
- **Habit:** Users may use it frequently to discover new dining options or share their culinary experiences.
- **Scope:** Initial version could focus on basic functionality like listing restaurants and reviews. Subsequent versions could expand to include advanced features like recipe sharing and personalized recommendations.

## Product Spec

### 1. User Features (Required and Optional)

**Required Features**

1. List nearby restaurants based off location with filters such as cousine and distance
2. Allow users to enter and view reviews for restaurants 
3. User account and autheticaion for access to saved recepies
4. Show menu items (if avaliable) for restaurants
5. Show recepie for menu items
6. Save and edit recepies for later use
7. Show nutritional information for recepie ingredients and for menu item
   

**Optional Features**

1. Share saved recepies with other users
2. Show dashboard with recent restaurnt reviews and activites 
3. Create point system for activies done in app

### 2. Screen Archetypes

- Login Screen
  - User can login or naviga to registraion screen
- Signup
  - User can create a new account with email, name, and password
- Home screen
  - User can see near by restaurents based on filter (distance, cousine, etc.)
  - User can search for restaurants based on food images
- Image Upload Screen
  - User can take a picture of a food or select a image from device to be used in restaurant search in home screen
- Restaurant Detail Screen
  - User can view restaurent details, reviews, and ratings
  - Users can see the menu and leave a review if desired
- Write Reivew Screen
  - User can leave a rating and review for a restaurant
- Restaurant Menu Screen
  - User can leave a rating and review for a restaurant
- Menu Item Recipe
  - User can see a recipe for a menu item in a resturant with ingredients, instruction, and nutritional info
- Reviews Screen
  - User can see summary of reviews they've left in the past
- Review Detail 
  - User can see the entire reivew along with options to delete or edit review
- Edit Review
  - User edit and save past reviews they've left
- Saved Recipes 
  - User can see summaries of reipes they've saved in the past
- Saved Recipe Detail Screen
  - User can see details of a saved recipe and option to edit the recipe
- Edot Recipe
  - User can edit saved recipes to make it their own!

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Saved Recipes - navigates to screen that lists recipes the user has saved 
* Home Page - navigates to screen which lists various nearby restaurants users can search through
* Your Reviews - navigates to screen which lists all reviews that the user has inputted for any restaurants

**Flow Navigation** (Screen to Screen)

- Log in Screen
  - => Home Page (after successful username and password input)
  - => Sign Up Page (if user does not have a registered account for the application yet)
- Home Screen
  - => Restaurant Detail Screen (after clicking on one of the restaurant buttons)
  - => Image Upload Screen
- Restaurant Detail Screen
  - => Menu Screen (after clicking on Menu button)
  - => Write Review Screen (after click on "Leave Review" button)
- Write Review Screen
  - => Restaurant Detail Screen (after submitting or cancelling a review)
- Menu Screen
  - => Menu Item Recipe Screen (after clicking on one of the menu items)
- User Reviews Screen
  - => Review Details Screen (after clicking on one of the listed reviews)
- Review Details Screen
  - => Edit Review Screen (after clicking on "Edit" button on review details)
  - => User Reviews Screen (on clicking "delete" button)
- Edit Review Screen
  - => Review Details Screen (after clicking "cancel" or "save")

## Wireframes

<img src="https://github.com/TasteBudzz/tasteBudzzApp/blob/main/wireframe.png" width=600>

<br>

<br>

### [BONUS] Digital Wireframes & Mockups
https://www.figma.com/file/4RMktQKNxT1FX0Wrj3xNx2/TasteBudzz?type=design&node-id=0%3A1&mode=design&t=VQXytrP3sOSs4zDF-1

### [BONUS] Interactive Prototype

https://www.figma.com/proto/4RMktQKNxT1FX0Wrj3xNx2/TasteBudzz?embed_host=share&kind=proto&node-id=5-3&page-id=0%3A1&scaling=scale-down&show-proto-sidebar=1&starting-point-node-id=5%3A3&t=5AotGJ1XQFzbDUeA-1&type=design&mode=design
<br>

<br>

# Milestone 2 - Build Sprint 1 (Unit 8)

## GitHub Project board

[Add screenshot of your Project Board with three milestones visible in
this section]
![tastebudzboard.png](https://github.com/TasteBudzz/tasteBudzzApp/blob/main/tastebudzboard.png?raw=true)

## Issue cards


## Issues worked on this sprint
![issues.png](https://github.com/TasteBudzz/tasteBudzzApp/blob/main/issues.png?raw=true)
- Worked on the research for which api to use for our restaurant reviews app. The API in question will be the API we use for retrieving info from nearby restaurants. This is the most important aspect of the app since all the features rely on the information that is retrievable. I have found a couple of API,
  - https://rapidapi.com/restaurantmenus/api/documenu/details - mainly for well known chain restaurants. Not local restaurant friendly. Though it did show menu, address location, and such.
  - https://stupefied-torvalds-88b3ed.netlify.app/ - It seems promising since it has all restaurant information we need. When attempting to test out the API on their website, the page loads forever.
  - https://rapidapi.com/ptwebsolution/api/worldwide-restaurants/pricing - Seems very promising as well as it does show local restaurants when hitting endpoint but the menu is not returned. 
Though each has their drawback. For now, will pursue the second/third option and attempt to connect on our own and see what results we get for second option. For the third option we will see if theres a way we can just get the menu now. 

- Created Database Schema to follow 
![tastebudzboard.png](https://github.com/TasteBudzz/tasteBudzzApp/blob/main/tastebuds%20diagram.png?raw=true)


- [Add giphy that shows current build progress for Milestone 2. Note: We will be looking for progression of work between Milestone 2 and 3. Make sure your giphys are not duplicated and clearly show the change from Sprint 1 to 2.]
- ![tastebudzboard.png](https://github.com/TasteBudzz/tasteBudzzApp/blob/main/tastebudzboard.png?raw=true)
<br>



<br>
- [Add screenshot of your Project Board with the issues that you're working on in the **NEXT sprint**. It should include issues for next unit with assigned owners.] ![issues2.png](https://github.com/TasteBudzz/tasteBudzzApp/blob/main/issues2.png?raw=true)




# Milestone 3 - Build Sprint 2 (Unit 9)

## GitHub Project board

[Add screenshot of your Project Board with the updated status of issues for Milestone 3. Note that these should include the updated issues you worked on for this sprint and not be a duplicate of Milestone 2 Project board.] <img src="YOUR_WIREFRAME_IMAGE_URL" width=600>

## Completed user stories

- List the completed user stories from this unit
- List any pending user stories / any user stories you decided to cut
from the original requirements

[Add video/gif of your current application that shows build progress]
<img src="YOUR_WIREFRAME_IMAGE_URL" width=600>

## App Demo Video

- Embed the YouTube/Vimeo link of your Completed Demo Day prep video
