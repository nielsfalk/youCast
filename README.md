youCast
=======

allows you to subscribe sources like vimeo or youtube as podcast.

json subscription
-----------------
create your own podcast be writing easy json like [this](https://www.wuala.com/nielsfalk/myCast/nielsFalk.json/?dl=1)
people can subscribe it by using

[(the youCast deployment)/json/https%3A%2F%2Fcontent.wuala.com%2Fcontents%2Fnielsfalk%2FmyCast%2FnielsFalk.json%2F%3Fdl%3D1](https://gentle-depths-8524.herokuapp.com/json/https%3A%2F%2Fcontent.wuala.com%2Fcontents%2Fnielsfalk%2FmyCast%2FnielsFalk.json%2F%3Fdl%3D1)

youtube subscription
--------------------
you can subscribe to any youtube-user by using

[(the youCast deployment)/youtube/(the user)](http://localhost:8080/youtube/flow)

vimeo subscription
------------------
you can subscribe to any vimeo-user by using

[(the youCast deployment)/vimeo/(the user)](http://localhost:8080/vimeo/sinco)

local installation
------------------
cumming soon

cloud deployment
----------------
For a deployment on Heroku you need:
* a heroku-account
* ssh
* git

please type in your console
* git clone https://github.com/nielsfalk/youCast.git
* cd youCast
* heroku login
* heroku create
* git push heroku master

In the log the is a Link for your application (for example https://limitless-basin-4741.herokuapp.com ).

The Icon
--------
By Yagraph (Own work) [GFDL (http://www.gnu.org/copyleft/fdl.html) or CC BY-SA 3.0 (http://creativecommons.org/licenses/by-sa/3.0)], via Wikimedia Commons
http://commons.wikimedia.org/wiki/File%3APodcast-icon.svg