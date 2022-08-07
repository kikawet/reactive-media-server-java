# Reactive Media Server

As for today the Spring framework is not ready for reactive programming since the transactions don't propagate into anonymous functions and the R2DBC ecosystem is not mature. I'm going to stale the project since I think I reached a decent state into the project. So far the test pass but no more promises can be made.


Reactive server using WebFlux to distribute media using functional endpoints

## Usage

Create your own .env using [.env.template](.env.template).

Once you run the server you can see mainly 2 endpoints 
- `/video`: list all the avaliable videos
- `/video/{name}`: using the last endpoint provide a video name to get that video

