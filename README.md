Report: My project is an event ticketing app that aims to enable users to purchase tickets, make payments, and view their purchased tickets.

The project starts with the login and signup pages. Users are prompted to enter their email and password, and both pages require the necessary information to be filled out. In the signup process, the entered information is saved in Firebase, while in the login process, the entered email and password are checked against the stored data in Firebase.

Upon successful login, the user is directed to the City Selection page. In this part, there is a search bar consisting of an EditText. Below it, there is a grid RecyclerView displaying cities. I used the CityAdapter class and the city item XML file to populate the RecyclerView with cities. The "OK" button remains disabled until a city is selected, and upon selecting a city, the user is directed to the HomeFragment.

In the HomeFragment, I used three RecyclerViews, each containing items with an ImageView and TextView wrapped in a CardView.

To fetch events, I utilized the Ticketmaster API and used the API key I obtained. I implemented LiveData and coroutines, creating separate LiveData instances for each RecyclerView. In the API client, I made GET requests to the Ticketmaster API. Then, in the repository, I processed the retrieved data to make it suitable for use. Additionally, I ensured that only events with unique names were retrieved from the Event Master API in the repository class. This allowed for a more diverse display of events in the HomeFragment.

In the MainViewModel, I used the repository class and also utilized the selected city from the city selection.

In the HomeFragment, I implemented the functionality for opening the Event Details page and displaying the details of the selected event when each item in the RecyclerView is clicked. I made another API query in this section, using the event IDs this time. In the Event Details page, I implemented the functionality to redirect the user to the Ticket Selection page when they click on the "Buy a Ticket" button. Using an intent, I passed the basic information of the event to the Ticket Selection page, where I displayed it within a CardView.

For the ticket category selection, I used a Spinner, and for displaying the selected tickets, I used a ListView. To remove a ticket from the ticket list view, I utilized a dialog.

I implemented size control for the list of selected tickets, allowing a maximum of five ticket selections.

For the payment section, I integrated Razorpay API into my application and used the API key I obtained. I set up an account to handle the payments and ensured that the payments were made to this account. Additionally, I stored the selected tickets under the user's ID in the Firebase Realtime Database, allowing the user's tickets to be displayed under their respective ID. I performed the Firebase database saving operation in the "onPaymentSuccess" method to ensure that the payment was successful. I also ensured that when a user selects multiple tickets in a single transaction, all of them are saved in the database.

In the Home Fragment, the bottom app bar contains search and user icons. When the user clicks on the search icon, the Search Page opens. The Search Page features a SearchView that allows the user to perform their desired search. After entering a search query, the user can click on "Sort By" and "Filter" to apply sorting and filtering using the dialog boxes. To implement this functionality, I utilized the filtering and sorting functions in the SearchViewModel, which are called within a when statement in the Search Fragment.

In the User Page, the user can view their ID, email address, and previous tickets. I retrieved this information from the Firebase Realtime Database, using the current user's ID. I displayed the previous tickets in a RecyclerView. Additionally, I linked the "Log Out" button to the Log In Page, allowing the user to log out of the app.

https://www.figma.com/proto/4gw75sHr95dbUagOtAafae/Event-Tickets-App?type=design&node-id=77-6073&scaling=scale-down&page-id=77%3A4621&starting-point-node-id=77%3A6073

When the users first enter the application, a log in screen appears. If they do not have an account, they can create one on the sign-up screen. After logging in/signing up, they are taken to the home screen.

On the home screen, when users click on the place icon located on the top app bar's right side, they are directed to the city page. From this page, they can select or change their current city and press the OK button to return to the home page again On the home page, they can see the three most trending events in their city and this events can be from different categories. Below that, they can see the events of the artists they follow in their city. Underneath those, they can view the categorized versions of the events based on events type. By swiping left, they can view more events. They can directly open the page with the event's information by clicking on the event poster. Clicking on the "See all" option redirects them to the category page.

On the category page, the three most trending events of that category are displayed at the top, and all the events of that category can be viewed below. The users can sort the events according to the criteria they have set. Clicking on an event takes the users to the page where the event's information is displayed.

On the event information page, if there is a trailer, it can be viewed, otherwise, the event poster is displayed. In this page users can see information about the event, a summary, the fee, and when the scroll down they can see the artists in the event.

Also when users click on an artist, the artist page opens, and on this page, users can see a summary of the artist and their previous events, as well as follow the artist.

When the users click the "buy a ticket" button, they are redirected to the session selection page. On that page, the users select the time. On the session selection page, when the date icon in the input date picker is clicked, a modal date picker opens, and users can select the date from there. In this modal date picker, there are OK and Cancel buttons to return to the session selection page. Additionally, when this modal screen is open, if you click outside the modal, it will also return to the session selection page. After that the users select the place and then they are directed to the seat selection page. After selecting their seats, the users can complete their purchase by entering their card information on the payment page. On the payment page, when the payment button is clicked, an information screen opens indicating the success of the payment. By clicking the "Go to Home Page" button on this screen, you can go to the home page. Also, if you click anywhere outside the information screen, you can return to the payment page.

From the bottom app bar on the home page, the users can click the search icon to go to the search page. On this page, they can use the search bar or select a category to go to the category page. If the users use the search bar and click the search icon, the results are listed, and they can filter and sort them.

When the users click the notification icon on the bottom app bar, they are redirected to the notifications page, where they can see notifications related to their tickets and account. Clicking on the "for you" tab on this page allows the users to view the events of the artists they follow.When they click on one of the notifications on that page, they are directed to the event information page.

When the users click on the user icon on the bottom app bar, they are redirected to the user page, where they can view their account information, the artists they follow, their previous tickets, their settings, and they can log out. If the users logs out, they redirects to the log in page.
