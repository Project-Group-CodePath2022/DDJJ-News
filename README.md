
## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)

## Overview
### Description
**DDJJ News** is a local publishing news application, allowing to stay informed about the situations in the country...

### App Evaluation
- **Category:** News / Education
- **Mobile:** This app would be primarily developed for mobile, also including the administration parts for adding news post.
- **Story:** While staying informed about news, users have possibility for reading articles, got alerted about an event, request blood, etc.
- **Market:** Any individual could choose to use this app.
- **Habit:** This app could be used as often or unoften as the user wanted.
- **Scope:** First we would start with creating the administration parts(Super-user, staff) and allowing standard users to receive news posts even no logged in in the app.

## Product Spec
### 1. User Stories (Required and Extended / Optional)

**Required Must-have Stories**

- [ ] Launch the app, shows the latest 21 news post including image, category, title, relative date format. 
- [ ] Clicking on a post should take to a detail page.
  - [ ] Displaying (image, title, date created, date updated, text, number of views, number of  comments, author name, category).
- [ ] On scrolling more posts must be added (Infinite scrolling).
- [ ] User can pull to refresh the last 21 posts.
- [ ] User can comment a post on detail page (Realtime).
- [ ] User can save post to see later.
  - [ ] User can delete saved post.
  - [ ] All saved post must be listed within an another page. 
- [ ] User can log in, log out, sign up to have more functionalities like read blog, request for donation blood.
- [ ] User can filter.
  - [ ] By categories.
  - [ ] By date.
- [ ] User can search for posts by title.
- [ ] User can read blog posts.
- [ ] User can request donation blood.
- [ ] When user launch without connection internet saved posts should display in the first page also (Feed).

***Only for admin, superuser, staff.****

- [ ] Management news and blog posts.
  - [ ] Create, read (Markdown for content).
  - [ ] Delete and update.
  - [ ] Only superuser can add staff users.
  - [ ] Only superuser can crud whatever news, blog posts.
  - [ ] staff users can only crud posts they added.



**Extended / Optional Stories**
- [ ] Forgot password.
- [ ] Email verification.
- [ ] Push notifications (When the app isn't running).
  - [ ] When admin add news post.
  - [ ] When receiving request blood.
  - [ ] When receiving alert.
- [ ] Authentication using oauth services (Facebook, google, ...). 
- [ ] Settings (Allowed notifications).
- [ ] System theming.
- [ ] Radio online
- [ ] User can alert other users about events (including location of the device) .
- [ ] Include an ecommerce.

***Only for admin, superuser, staff.****
- [ ] Offline editing for news and blog posts.


### 2. Screen Archetypes

* Login 
* Register - User signs up or logs into their account
   * Upon Download/Reopening of the application, the user is prompted to log in to gain access to their profile information to be properly matched with another person. 
   * ...
* Messaging Screen - Chat for users to communicate (direct 1-on-1)
   * Upon selecting music choice users matched and message screen opens
* Profile Screen 
   * Allows user to upload a photo and fill in information that is interesting to them and others
* Song Selection Screen.
   * Allows user to be able to choose their desired song, artist, genre of preference and begin listening and interacting with others.
* Settings Screen
   * Lets people change language, and app notification settings.

### 3. Navigation

**Navigation Bottom**

* News
* Favorite
* Settings

**Navigation Drawer**

* Home
* Dashboard (when admin loggedin)
* Request donation blood
* Alert
* Blog


***optional***

* Radio online
* Ecommerce

**Flow Navigation** (Screen to Screen)
* Forced Log-in -> Account creation if no log in is available
* Music Selection (Or Queue if Optional) -> Jumps to Chat
* Profile -> Text field to be modified. 
* Settings -> Toggle settings

## Wireframes
<img src=" width=800><br>

### [BONUS] Digital Wireframes & Mockups
<img src="" height=200>

### [BONUS] Interactive Prototype
<img src="" width=200>
