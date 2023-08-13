# MovieMatch 	:clapper:
Welcome to the MovieMach GitHub repository! MovieMach is a cutting-edge Android application that allows users to explore a vast collection of movies using modern technologies and best practices. This README file provides an overview of the app, its features, and the technologies utilized to bring it to life.

![movie_match_back](https://github.com/carpodok/MovieMatch/assets/64840495/5b7a967e-47d9-4a76-aaea-613d0dbf47bc)


## Features :hotsprings:
MovieMach comes with a range of exciting features that enhance the user experience and provide seamless navigation through the world of movies:

 :small_orange_diamond: Pagination with <a href="https://developer.android.com/topic/libraries/architecture/paging/v3-overview" target="_blank">Paging 3 Library</a> : MovieMach employs the Paging 3 library to efficiently load and display large datasets of movies. This ensures smooth scrolling and optimal memory management while browsing through the movie catalog.

:small_orange_diamond: <a href="https://developer.android.com/topic/libraries/architecture/paging/v3-overview" target="_blank"> MVVM Architecture</a>: The app follows the Model-View-ViewModel (MVVM) architectural pattern. This separation of concerns enhances code organization, maintainability, and testability. It also allows for easier adoption of new features and updates.

:small_orange_diamond: Dependency Injection with <a href="https://dagger.dev/hilt/" target="_blank">Hilt</a> : Hilt, a dependency injection library for Android, is used in MovieMach to manage the injection of components, such as repositories, view models, and network services. This promotes a modular and clean codebase while simplifying unit testing.

:small_orange_diamond: Network Calls with <a href="https://square.github.io/retrofit/" target="_blank">Retrofit</a> : MovieMach leverages Retrofit, a powerful HTTP client for Android, to handle network requests. This ensures efficient communication between the app and the backend servers, facilitating the retrieval of movie data.

:small_orange_diamond: <a href="https://developer.android.com/kotlin/flow" target="_blank"> Coroutines Flow </a> Coroutines Flow for LiveData Updates: To provide real-time updates to the UI, MovieMach utilizes Coroutines Flow in combination with LiveData. This combination allows the app to observe data changes and deliver them to the UI efficiently, resulting in a responsive and up-to-date user experience.

:small_orange_diamond: Image Loading with <a href="https://github.com/bumptech/glide" target="_blank">Glide Library</a> : The Glide library is integrated into MovieMach for smooth and optimized image loading. It automatically handles image caching, resizing, and rendering, contributing to fast and visually appealing movie browsing.

## Getting Started :desktop_computer:
To set up MovieMach on your local machine, follow these steps:

:small_red_triangle: Clone this repository to your preferred directory using the following command:

```
git clone https://github.com/carpodok/MovieMatch
```
:small_red_triangle: Open the cloned project in Android Studio.

:small_red_triangle: In order to fetch movie data, you will need an API key from The Movie Database (TMDb). You can obtain your API key by signing up at <a href="https://www.themoviedb.org/" target="_blank"> here </a>

:small_red_triangle: Once you have your API key, add the key into Constants object class like the following:

```
object Constants {

     const val API_KEY = "YOUR_API_KEY" //Replace YOUR_API_KEY with your actual TMDb API key.

     ...

}

```

:small_red_triangle: Build and run the app on an emulator or a physical device.


### Contact :envelope:
If you have any questions, feedback, or suggestions, feel free to reach out to us. You can contact us via email at cobanalitalha@gmail.com or open an issue in this repository.

Happy movie browsing with MovieMach!
