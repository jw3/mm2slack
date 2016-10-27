Mattermost to Slack
===================

Supports converting a Mattermost v1.4 - v2.2 dataset into Slack v? format.


### A Docker Approach

Using [mm4s](https://github.com/jw3/mm4s) creates a Docker based bot that can read from Mattermost.

See bot examples at https://github.com/jw3/mm4s-examples

The bot read and converts the channels indicated and makes the result available via a download.


The imagined flow

`docker run -p 80:80 jwiii/mm2slack channel-name`

`... various logged info`

`completed: download [files.tgz](http://172.17.0.1:80/download)`


### Development

#### Init

Running `docker-compose up` should provide you with an intiailized Mattermost 2.2 server.

##### Default Admin User
User: `admin`
Pass: `password`
Email: `admin@mm4s.com`

##### Default Teams:
 * [bots](http://localhost:8080/bots)
 * [humans](http://localhost:8080/humans)

See the [init scripts](https://github.com/jw3/mm4s/tree/master/initializer) for further details.
